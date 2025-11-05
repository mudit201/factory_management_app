<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal">
      <button class="close-button" @click="closeModal">&times;</button>
      <h2>Add New Batch</h2>

      <form @submit.prevent="handleSubmit">
        <VLayout style="justify-content: space-between">
          <VTextField label="Operator" v-model="operator_name"></VTextField>
          <VSearchableSelect
            label="Product"
            placeholder="Select product...."
            v-model="selectedProduct"
          >
            <VOption
              v-for="(product, index) in productList"
              :key="index"
              :text="product.label"
              :value="product.value"
            ></VOption>
          </VSearchableSelect>
          <VTimePicker label="Start Time" v-model="start_time"></VTimePicker>
          <VTimePicker label="End Time" v-model="end_time"></VTimePicker>
          <VButton
            appearance="filled"
            type="submit"
            label="Add"
            icon="add-solid"
            v-if="!loading"
          ></VButton>
          <VProgressRing v-else-if="loading"></VProgressRing>
        </VLayout>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useBannerStore } from '@/stores/bannerStore'
import { useBatchStore } from '@/stores/batchStore'
import { useProductStore } from '@/stores/productStore'
import type { BatchInput } from '@/types/Batch'
import {
  VTextField,
  VSearchableSelect,
  VOption,
  VTimePicker,
  VButton,
  VLayout,
  VProgressRing,
} from '@vonage/vivid-vue'
import { computed, onMounted, ref } from 'vue'

const productStore = useProductStore()

const productList = computed(() => {
  return productStore.products.map((p) => ({
    label: `${p.product} - ${p.flavor} - ${p.size}`,
    value: p.product, 
  }))
})

const selectedProduct = ref<string>('')
const start_time = ref<string>('')
const end_time = ref<string>('')
const operator_name = ref<string>('')

// const operator_name = ('');

// cannot use it as this as it is
// decleraed as undefined and will throw roor for typw mismatch
// const data= computed<BatchInput>(()=>({
const data = computed(() => ({
  operator: operator_name.value,
  product: selectedProduct.value,
  start_time: start_time.value,
  end_time: end_time.value,
}))

const batchStore = useBatchStore()
// const {loading, error, message} = batchStore;
const loading = computed(() => batchStore.loading)
const error = computed(() => batchStore.error)
const message = computed(() => batchStore.message)

// const emit = defineEmits(['message', 'close']);

const handleSubmit = async () => {
  console.log(data.value)
  await batchStore.addBatch(data.value as BatchInput) // Removed unused 'res'
  let msg = message.value || 'Operation completed successfully' // Ensure 'msg' is always a string
  let isError = false

  if (error.value) {
    console.log('error in modal', error.value)
    msg = error.value
    isError = true
  } else {
    console.log('message in modal', message.value)
  }

  operator_name.value = ''
  selectedProduct.value = ''
  start_time.value = ''
  end_time.value = ''
  console.log('asfsfs', msg)
  closeModal()
  useBannerStore().showBannerMessage(msg, isError)
}

onMounted(async () => {
  await productStore.loadProducts()
})

const emit = defineEmits(['close'])

const closeModal = () => {
  emit('close')
}
</script>

<style>
.input-form {
  display: flex;
}
.form-layout {
  justify-content: space-between;
}

.banner {
  z-index: 111;
  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
  animation: fadeIn 0.5s ease-in-out;
}
</style>
