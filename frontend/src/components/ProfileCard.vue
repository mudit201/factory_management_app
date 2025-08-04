<template>
  <div class="card-body">
    <div
      class="icon"
      :style="{ backgroundColor: iconColor }"
      aria-label="icon"
    >
      {{ firstLetter }}
    </div>
    <div class="name">{{ name }}</div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
  name: {
    type: String,
    required: true,
  },
});

const firstLetter = computed(() => props.name.charAt(0).toUpperCase());

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
  if (!props.name) return '#ccc';
  const charCode = props.name.charCodeAt(0);
  const colorIndex = (charCode - 65) % colors.length;
  return colors[colorIndex];
});
</script>

<style scoped>
.card-body {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  max-width: 250px;
  background-color: #fff;
  box-shadow: 0 2px 6px rgb(0 0 0 / 0.1);
  font-family: Arial, sans-serif;
}

.icon {
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

.name {
  font-size: 16px;
  color: #333;
}
</style>
