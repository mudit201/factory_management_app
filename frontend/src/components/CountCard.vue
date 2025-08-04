<template>
  <div class="card-body">
    <div class="num-background">
      {{ numLen }}
    </div>
    <div class="val-name">{{ valName }}</div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  numLen:{
    type: Number,
    required: true
  },
  valName: {
    type: String,
    required: true
  }
})
</script>

<style lang="postcss">
.card-body {
  background-color: white;
  padding: 1rem;      /* equivalent to p-4 */
  border-radius: 0.375rem; /* rounded */
  box-shadow: 0 1px 3px rgba(0,0,0,0.1); /* shadow-md approx */
  text-align: center;

}

.num-background {
  font-size: 2.25rem; /* text-4xl */
  font-weight: 700;   /* font-bold */
  color: #2563eb;     /* text-blue-600 */
}

.val-name {
  margin-top: 0.5rem; /* mt-2 */
  font-size: 0.875rem; /* text-sm */
  color: black;
}

</style>
