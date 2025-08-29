<template>
  <div>
    <div class="header-container">
      <h2>Products ({{ productCount }})</h2>
          <VButton
          shape="pill"
          appearance="outlined"
          icon="fax-add-solid"
          label="Add New Product"
          @click="toggleModal()"
          >
          </VButton>
    </div>
    <!-- <div v-if="loading" class="spinner"></div> -->
    <VProgressRing
      v-if="loading"
      data-testid="progress-ring"
      />


    <div v-if="error" class="error">{{ error }}</div>

    <table v-if="products.length">
      <thead>
        <tr>
          <th>Product</th>
          <th>Flavor</th>
          <th>Size</th>
          <th>Min Batch Time</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="prod in products" :key="prod.product">
          <td>
            {{ prod.product }}
            <VIcon name="delete-solid"
            data-testid="delete-icon"
            @click="handleDelete(prod.product)"
            />
          </td>
          <td>{{ prod.flavor }}</td>
          <td>{{ prod.size }}</td>
          <td>{{ prod.min_batch_time }}</td>
        </tr>
      </tbody>
    </table>

    <p v-else>No products found.</p>
  </div>
  <ProductFormModal v-if="showModal" @close="showModal=false"></ProductFormModal>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { useStore } from 'vuex';
import { VButton, VIcon, VProgressRing } from '@vonage/vivid-vue';
import ProductFormModal from '@/components/ProductFormModal.vue';
import { useBannerStore } from '@/stores/bannerStore';

const store = useStore();
const loading = computed(()=> store.state.productStore.loading);
const error = computed(()=> store.state.productStore.error);
const products = computed(()=>  store.state.productStore.products);
const productCount =  computed(()=>  store.getters['productStore/getProductCount']);

onMounted(async ()=>{
  await store.dispatch("productStore/loadProducts");
})

const showModal= ref(false);

function toggleModal() {
  if(!showModal.value){
    console.log(showModal.value);
    showModal.value = true;
  }else{
    console.log(showModal.value);
    showModal.value = false;
  }
}

const handleDelete = async (productId: string) => {
  await store.dispatch("productStore/deleteProduct", productId);
  const msg = store.state.productStore.error || "Product deleted successfully"; // Ensure 'msg' is always a string
  const isError = !!store.state.productStore.error;
  useBannerStore().showBannerMessage(msg, isError);
};


</script>

<style scoped>

/* Error message */
.error {
  color: red;
  margin-top: 10px;
}

.header-container{
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 0;
}
</style>
