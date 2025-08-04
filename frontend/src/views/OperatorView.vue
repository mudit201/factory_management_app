<template>
    <VLayout class="card-list">
      <div v-for="(operator, index) in operators" :key="index">
        <!-- <ProfileCard :name="operator.operator" @click="toggleVisibility(operator.operator)" > </ProfileCard> -->
        <!-- {{ operator.operator }} -->
        <VCard
            :headline="operator.operator"
            clickable-card
            v-on:click="toggleVisibility(operator.operator)"
            subtitle="Operator"
        >
          <!-- <div
            class="profile-icon"
            :style="{backgroundColor: iconColor}"
          >
            {{ firstLetter }}
          </div> -->
          <template #icon>
            <VIcon label="profile-solid" :size="5"></VIcon>
          </template>


        </VCard>
      </div>
    </VLayout>

  <OperatorBatchModal v-if="showModal" :name="name" :batches="batchesByoperator" :loading="loading" :error="error" @close="showModal = false"></OperatorBatchModal>

</template>


<script setup lang="ts">
import { ref, computed, onMounted  } from 'vue';
import { useOperatorStore } from '@/stores/operatorStore';
import ProfileCard from '@/components/ProfileCard.vue';
import OperatorBatchModal from '@/components/OperatorBatchModal.vue'
import { useBatchStore } from '@/stores/batchStore';

import { VLayout, VCard, VIcon, VButton, VBanner } from '@vonage/vivid-vue';

const operatorStore = useOperatorStore();
const operators = computed(()=> operatorStore.operators);
const showModal = ref(false);
const name = ref();

const batchStore = useBatchStore();
const batchesByoperator = computed(()=> batchStore.batches);
// const {loading, error} = batchStore
const loading = computed(()=> batchStore.loading)
const error = computed(()=> batchStore.error)

async function toggleVisibility(operatorName: string) {
  console.log(operatorName);
  name.value = operatorName;
  if (!showModal.value) {
    await batchStore.loadBatchesByOperatorName(operatorName);
    console.log(batchesByoperator.value);
    showModal.value = true;
  } else {
    showModal.value = false;
  }
}


onMounted(async ()=>{
  await operatorStore.loadOperators();
  // console.log(operatorStore.operators);
})


const firstLetter = computed(() => name.value.charAt(0).toUpperCase());

const colors = [
  '#e74c3c',
  '#3498db',
  '#27ae60',
  '#f39c12',
  '#8e44ad',
  '#16a085',
  '#d35400',
  '#2c3e50',
];

const iconColor = computed(() => {
  if (!name.value) return '#ccc';
  const charCode = name.value.charCodeAt(0);
  const colorIndex = (charCode - 65) % colors.length;
  return colors[colorIndex];
});
</script>

<style scoped>
.card-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
  padding: 5px;
  /* flex-direction: row;
  flex-wrap: wrap;
  gap: 16px;
  padding: 10px;
  justify-content: space-between; */
}

.profile-icon {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  color: white;
  font-weight: bold;
  font-size: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  user-select: none;
  text-transform: uppercase;
}
</style>
