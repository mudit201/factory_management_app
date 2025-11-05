<template>
  <main class="main-container">
    <div class="header-section">
      <h1 class="page-title">Dashboard Summary</h1>
      <p class="page-subtitle">Real-time manufacturing analytics and insights</p>
    </div>
    <div class="content-wrapper">
      <section class="cards-section">
        <div class="cards-grid">
          <CountCard :numLen="productCount" valName="Products" class="card-item" />
          <CountCard :numLen="batchCount" valName="Batches" class="card-item" />
          <CountCard :numLen="operatorCount" valName="Operators" class="card-item" />
          <div class="status-card card-item">
            <div class="status-header">
              <h3>System Status</h3>
            </div>
            <div class="status-indicator">
              <div class="status-dot active"></div>
              <span class="status-text">Operational</span>
            </div>
          </div>
        </div>
      </section>

      <section class="charts-section">
        <div class="chart-wrapper">
          <LineChart :data="lineDowntime" />
        </div>

        <div class="metrics-row">
          <div class="metric-card">
            <h4 class="metric-title">Current Batch</h4>
            <p class="metric-value">{{ currentBatch }}</p>
            <VSearchableSelect
              placeholder="Select a batch..."
              icon="group-5-solid"
              :v-model="currentBatch"
            >
              <VOption
                v-for="batch in batches"
                :key="batch.value"
                :label="batch.label"
                :value="batch.label"
                :text="batch.label"
                @click="handleBatchChange(batch.value)"
              />
            </VSearchableSelect>
          </div>
          <div class="metric-card">
            <h4 class="metric-title">Total Downtime</h4>
            <p class="metric-value">{{ totalDowntime }} min</p>
          </div>
          <div class="metric-card">
            <h4 class="metric-title">Efficiency</h4>
            <p class="metric-value">{{ efficiency }}%</p>
          </div>
        </div>
      </section>
    </div>
  </main>
</template>

<script setup lang="ts">
import { useBatchStore } from '@/stores/batchStore'
import CountCard from '../components/CountCard.vue'
import { useProductStore } from '@/stores/productStore'
import { computed, onMounted, ref } from 'vue'
import { useOperatorStore } from '@/stores/operatorStore'
import { useLineDowntimeStore } from '@/stores/lineDowntimeStore'
import LineChart from '@/components/LineChart.vue'
import { VSearchableSelect, VOption } from '@vonage/vivid-vue'

const productStore = useProductStore()
const productCount = computed(() => productStore.productCount)

const batchStore = useBatchStore()
const batchCount = computed(() => batchStore.batchCount)
// const batches = computed(() => {
//   const list = [];
//   for (const batch of batchStore.batches) {
//     list.push(batch.batch);
//   }
//   console.log('Batches:', list);
//   return list;
// });
const batches = computed(() => {
  return batchStore.batches.map((batch) => ({
    label: String(batch.batch),
    value: batch.batch,
  }))
})
const currentBatch = ref(422111)

const operatorStore = useOperatorStore()
const operatorCount = computed(() => operatorStore.operatorCount)

const lineDowntimeStore = useLineDowntimeStore()
const lineDowntime = computed(() => lineDowntimeStore.lineDowntime)



// Calculate total downtime from all failure modes
const totalDowntime = computed(() => {
  if (!lineDowntime.value) return 0
  return Object.keys(lineDowntime.value)
    .filter((key): key is string => typeof key === 'string' && key.startsWith('f'))
    .reduce(
      (sum, key) => sum + (lineDowntime.value?.[key as keyof typeof lineDowntime.value] || 0),
      0,
    )
})

const maxTime = ref<number>(0)

const fetchMaxTime = async (batchId: number) => {
  const time = await batchStore.getBatchTotalTimeById(batchId)
  maxTime.value = typeof time === 'number' ? time : 0
}

// Calculate efficiency based on downtime 
const efficiency = computed(() => {
  const total = totalDowntime.value
  // const maxTime = 480 // 8 hours in minutes as example
  // const maxTime = await batchStore.getBatchTotalTimeById(currentBatch.value)
  console.log('Max time for batch', currentBatch.value, ':', maxTime)

  return total > 0 ? Math.max(0, Math.round(((maxTime.value - total) / maxTime.value) * 100)) : 100
})

const handleBatchChange = async (newBatch: number) => {
  currentBatch.value = newBatch
  await lineDowntimeStore.getLineDowntime(currentBatch.value)
  await fetchMaxTime(newBatch)
}

onMounted(async () => {
  try {
    await Promise.all([
      productStore.loadProducts(),
      batchStore.loadBatches(),
      operatorStore.loadOperators(),
      lineDowntimeStore.getLineDowntime(currentBatch.value),
      fetchMaxTime(currentBatch.value),
    ])
    console.log('All data loaded successfully')
    console.log('Line downtime:', lineDowntimeStore.lineDowntime)
  } catch (error) {
    console.error('Error loading dashboard data:', error)
  }
})
</script>

<style lang="postcss" scoped>
.main-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

.header-section {
  margin-bottom: 32px;
  padding-bottom: 24px;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 8px;
  background: rgb(31, 47, 74);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-subtitle {
  font-size: 1.1rem;
  color: #64748b;
  font-weight: 400;
  margin: 0;
}

.content-wrapper {
  max-width: 1400px;
  /* margin: 0 auto; */
}

.cards-section {
  margin-bottom: 32px;
}

.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
}

.card-item {
  transition: all 0.3s ease;
  border-radius: 16px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.card-item:hover {
  transform: translateY(-4px);
  box-shadow:
    0 20px 25px -5px rgba(0, 0, 0, 0.1),
    0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.status-card {
  background: white;
  padding: 24px;
  border-radius: 16px;
  border: 1px solid #e2e8f0;
}

.status-header h3 {
  font-size: 1.1rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 16px;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 12px;
}

.status-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: #ef4444;
  animation: pulse 2s infinite;
}

.status-dot.active {
  background-color: #10b981;
}

.status-text {
  font-weight: 500;
  color: #374151;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.charts-section {
  background: white;
  border-radius: 20px;
  padding: 32px;
  box-shadow:
    0 10px 15px -3px rgba(0, 0, 0, 0.1),
    0 4px 6px -2px rgba(0, 0, 0, 0.05);
  border: 1px solid #e2e8f0;
}

.chart-wrapper {
  margin-bottom: 32px;
}

.metrics-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 24px;
}

.metric-card {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  padding: 20px;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  text-align: center;
  transition: all 0.3s ease;
}

.metric-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 15px -3px rgba(0, 0, 0, 0.1);
}

.metric-title {
  font-size: 0.9rem;
  font-weight: 600;
  color: #64748b;
  margin-bottom: 8px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.metric-value {
  font-size: 1.8rem;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
}

@media (max-width: 768px) {
  .main-container {
    padding: 16px;
  }

  .page-title {
    font-size: 2rem;
  }

  .cards-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .charts-section {
    padding: 20px;
  }

  .metrics-row {
    grid-template-columns: 1fr;
    gap: 16px;
  }
}

@media (max-width: 480px) {
  .page-title {
    font-size: 1.8rem;
  }

  .page-subtitle {
    font-size: 1rem;
  }
}
</style>
