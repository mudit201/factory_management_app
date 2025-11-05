<template>
  <VLayout class="card-list">
    <div v-for="(operator, index) in operators" :key="index" class="card-container">
      <VCard clickable-card v-on:click="openOperatorModal(operator.operator)" class="operator-card">
        <template #media>
          <div class="card-header" :style="{ backgroundColor: getColor(operator.operator) }">
            <div class="profile-img-container">
              <img src="/profile-icon-2.jpg" alt="" class="profile-img" />
            </div>
          </div>
        </template>

        <template #main>
          <div class="operator-info">
            <h3 class="operator-name">{{ operator.operator }}</h3>
            <button class="view-details-btn" @click.stop="openOperatorModal(operator.operator)">
              View Details
            </button>
          </div>
        </template>
      </VCard>
    </div>
  </VLayout>

  <OperatorBatchModal
    v-if="showModal"
    :name="name"
    :batches="batchesByoperator"
    :loading="loading"
    :error="error"
    @close="closeModal"
  ></OperatorBatchModal>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useOperatorStore } from '@/stores/operatorStore'
import ProfileCard from '@/components/ProfileCard.vue'
import OperatorBatchModal from '@/components/OperatorBatchModal.vue'
import { useBatchStore } from '@/stores/batchStore'

import { VLayout, VCard, VIcon } from '@vonage/vivid-vue'
import { useRoute, useRouter } from 'vue-router'

const operatorStore = useOperatorStore()
const operators = computed(() => operatorStore.operators)
// const showModal = ref(false);
const route = useRoute()
const router = useRouter()
const showModal = computed(() => !!route.params.operator_name)
const name = computed(() => route.params.operator_name as string)

// watch(
//   () => route.params.operator_name,
//   async (newName) => {
//     if (newName) {
//       await batchStore.loadBatchesByOperatorName(newName as string);
//     }
//   }
// );

const batchStore = useBatchStore()
const batchesByoperator = computed(() => batchStore.batches)
// const {loading, error} = batchStore
const loading = computed(() => batchStore.loading)
const error = computed(() => batchStore.error)

// async function toggleVisibility(operatorName: string) {
//   console.log(operatorName)
//   name.value = operatorName
//   if (!showModal.value) {
//     await batchStore.loadBatchesByOperatorName(operatorName)
//     console.log(batchesByoperator.value)
//     showModal.value = true
//   } else {
//     showModal.value = false
//   }
// }

async function openOperatorModal(operatorName: string) {
  // showModal.value = true
  await batchStore.loadBatchesByOperatorName(operatorName)
  router.push({ name: 'operator_name', params: { operator_name: operatorName } })
}

function closeModal() {
  // showModal.value = false
  router.push({ name: 'operators' })
}

onMounted(async () => {
  await operatorStore.loadOperators()
  // console.log(operatorStore.operators);
})

// const firstLetter = computed(() => name.value.charAt(0).toUpperCase());

const colors = [
  '#e74c3c',
  '#3498db',
  '#27ae60',
  '#f39c12',
  '#8e44ad',
  '#16a085',
  '#d35400',
  '#2c3e50',
]

function getColor(name: string) {
  // Deterministic color per operator
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  const index = Math.abs(hash) % colors.length
  return colors[index]
}
</script>

<style scoped>
.card-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
  padding: 5px;
}

.card-container {
  perspective: 1000px;
  transition: transform 0.3s;
}

.operator-card {
  display: flex;
  flex-direction: column;
  min-height: 320px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  overflow: hidden;
  background: #fff;
  transform-style: preserve-3d;
}

.operator-card:hover {
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  transform: translateY(-5px);
}

.card-header {
  height: 130px;
  width: 100%;
  display: flex;
  justify-content: center;
  padding-bottom: 50px;
  position: relative;
}

.profile-img-container {
  width: 110px;
  height: 110px;
  border-radius: 50%;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 5px solid white;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
  margin-top: 70px;
  position: relative;
  z-index: 2;
}

.profile-img {
  width: 90px;
  height: 90px;
  border-radius: 50%;
  object-fit: cover;
}

.operator-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 16px 20px;
  background: white;
  flex: 1;
}

.operator-name {
  font-size: 1.75rem;
  font-weight: 700;
  margin: 6px 0 8px 0;
  color: #333;
  text-align: center;
  letter-spacing: -0.01em;
}

.operator-role {
  font-size: 0.95rem;
  color: #666;
  margin-bottom: 22px;
  font-weight: 500;
}

.view-details-btn {
  margin-top: 10px;
  background: none;
  border: 2px solid #3498db;
  color: #3498db;
  padding: 10px 22px;
  border-radius: 50px;
  font-weight: 600;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.2s;
}

.view-details-btn:hover {
  background-color: #3498db;
  color: white;
}
</style>
