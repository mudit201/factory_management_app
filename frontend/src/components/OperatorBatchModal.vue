<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal">
      <button class="close-button" @click="closeModal">&times;</button>

      <h2>Batches of {{ operatorName }}</h2>

      <!-- <div v-if="loading" class="spinner"></div> -->
      <VProgressRing v-if="loading" ></vProgressRing>

      <div v-if="error" class="error">{{ error }}</div>

      <table v-if="batches.length">
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
            <th scope="row">{{ batch.batch }}</th>
            <td>{{ batch.product }}</td>
            <td>{{ batch.operator }}</td>
            <td>{{ batch.start_time }}</td>
            <td>{{ batch.end_time }}</td>
            <td>{{ batch.date }}</td>
          </tr>
        </tbody>
      </table>

      <p v-else>No batches found.</p>
    </div>
  </div>
</template>



<script setup lang="ts">
import { computed } from 'vue';
import type { Batch } from '@/types/Batch';
import { VProgressRing } from '@vonage/vivid-vue';

const props = defineProps<{
  name: string,
  batches: Batch[],
  loading: boolean,
  error: string | null
}>();

const emit = defineEmits(['close']);

const batches = computed(() => props.batches);
const operatorName = props.name;

const closeModal = () => {
  emit('close');
};
</script>


<style>
.modal {
  background: white;
  padding: 2rem;
  border-radius: var(--border-radius);
  width: 90%;
  max-width: 800px;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.3);
  position: relative;
}

.error {
  color: red;
  margin-bottom: 1em;
}

</style>
