<template>
  <div>
    <div class="header-container">
      <h2>Batches ({{ batchCount }})</h2>
      <VButton
        shape="pill"
        appearance="outlined"
        icon="fax-add-solid"
        label="Add New Batch"
        @click="toggleModal()"
      >
      </VButton>
    </div>

    <div v-if="loading" class="spinner"></div>

    <div v-if="error" class="error">{{ error }}</div>

    <table v-if="batches.length">
      <!-- <caption>Batches ({{ batchCount }})</caption> -->
      <thead>
        <tr>
          <th scope="col">Batch</th>
          <th scope="col">Product</th>
          <th scope="col">Operator</th>
          <th scope="col">Start Time</th>
          <th scope="col">End Time</th>
          <th scope="col">Date</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="batch in batches" :key="batch.batch">
          <th scope="row" style="position: relative">
            {{ batch.batch }}
            <VIcon
              name="delete-solid"
              style="
                position: absolute;
                right: 0;
                top: 50%;
                transform: translateY(-50%);
                padding-right: 10px;
              "
              @click="handleDelete(batch.batch)"
            />
          </th>
          <td data-title="Product">{{ batch.product }}</td>
          <td data-title="Operator">{{ batch.operator }}</td>
          <td data-title="Start Time">{{ batch.start_time }}</td>
          <td data-title="End Time">{{ batch.end_time }}</td>
          <td data-title="Date">{{ batch.date }}</td>
        </tr>
      </tbody>
    </table>

    <p v-else>No batches found.</p>
  </div>

  <BatchFormModal v-if="showModal" @close="showModal = false"></BatchFormModal>
</template>

<script setup lang="ts">
import { useBatchStore } from '@/stores/batchStore'
import { computed, onMounted, ref } from 'vue'
import { VButton, VIcon } from '@vonage/vivid-vue'
import BatchFormModal from '@/components/BatchFormModal.vue'
import { useBannerStore } from '@/stores/bannerStore'

const batchStore = useBatchStore()
const { loading, error } = batchStore
const batches = computed(() => batchStore.batches)
const batchCount = computed(() => batchStore.batchCount)

const showModal = ref(false)

function toggleModal() {
  if (!showModal.value) {
    console.log(showModal.value)
    showModal.value = true
  } else {
    console.log(showModal.value)
    showModal.value = false
  }
}

const handleDelete = async (batchId: number) => {
  await batchStore.deleteBatch(batchId)
  const msg = batchStore.message || batchStore.error || 'Batch deleted successfully' // Ensure 'msg' is always a string
  const isError = !!batchStore.error 
  useBannerStore().showBannerMessage(msg, isError)
}

onMounted(async () => {
  await batchStore.loadBatches()
})
</script>

<style scoped>
/* Error message */
.error {
  color: red;
  margin-top: 10px;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 0;
}
</style>
