<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal">
      <button class="close-button" @click="closeModal">&times;</button>
      <h2>Add New Product</h2>

      <form @submit.prevent="handleSubmit">
        <VLayout style="justify-content: space-between">
          <VTextField
            label="Product code"
            v-model="product"
            helper-text="First two letter of product - size (Ex- CO-600 for cola 600 ml)"
          ></VTextField>
          <VTextField
            label="Flavor"
            v-model="flavor"
            helper-text="Flavor of the product (Ex: Cola)"
          ></VTextField>
          <VTextField
            label="Size"
            v-model="size"
            helper-text="size and quantity (Ex: 600 ml)"
          ></VTextField>
          <VNumberField
            label="Minimum Batch Time"
            :valueAsNumber="minBatchTime"
            @update:valueAsNumber="(val) => (minBatchTime = val)"
            helper-text="in mins"
          ></VNumberField>
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
import type { Product } from '@/types/Product'

import { VTextField, VButton, VLayout, VProgressRing, VNumberField } from '@vonage/vivid-vue'
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

const product = ref<string>('')
const flavor = ref<string>('')
const size = ref<string>('')
const minBatchTime = ref<number | undefined>(undefined)

const data = computed(() => ({
  product: product.value,
  flavor: flavor.value,
  size: size.value,
  minBatchTime: minBatchTime.value, // Corrected property name to match 'Product' type
}))

const loading = computed(() => store.state.productStore.loading)
const error = computed(() => store.state.productStore.error)
const message = computed(() => store.state.productStore.message)

const handleSubmit = async () => {
  if (!product.value || !flavor.value || !size.value || minBatchTime.value === undefined) {
    useBannerStore().showBannerMessage('Please fill all fields', true)
    closeModal()
    return
  }
  console.log('Submitting product data:', data.value)
  await store.dispatch('productStore/addProduct', data.value as Product)

  let msg = message.value || 'Operation completed successfully' // Ensure 'msg' is always a string
  let isError = false
  if (error.value) {
    console.log('error in modal', error.value)
    msg = error.value
    isError = true
  } else {
    console.log('message in modal', message.value)
  }
  product.value = ''
  flavor.value = ''
  size.value = ''
  minBatchTime.value = undefined
  console.log('asfsfs', msg)
  closeModal()
  await store.dispatch('productStore/loadProducts')

  useBannerStore().showBannerMessage(msg, isError)
}

onMounted(async () => {
  await store.dispatch('productStore/loadProducts')
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
