<template>
  <nav class="side-bar">
    <ul class="items-list">
      <li>
        <button
          class="item"
          :class="{ active: route.path === '/' }"
          @click="navigateTo('/')"
          :autofocus="route.path === '/'"
        >
          <VIcon name="home-solid" style="padding-bottom: 3px;"></VIcon>
          Home
        </button>
      </li>
      <li>
        <button
          class="item"
          :class="{ active: route.path === '/products' }"
          @click="navigateTo('/products')"
          :autofocus="route.path === '/products'"
        >
          <VIcon name="app-store-solid" style="padding-bottom: 3px;"></VIcon>
          Products
        </button>
      </li>
      <li>
        <button
          class="item"
          :class="{ active: route.path === '/batches' }"
          @click="navigateTo('/batches')"
          :autofocus="route.path === '/batches'"
        >
        <VIcon name="group-5-solid" style="padding-bottom: 3px;"></VIcon>
        Batches
        </button>
      </li>
      <li>
        <button
          class="item"
          :class="{ active: route.path === '/operators' }"
          @click="navigateTo('/operators')"
          :autofocus="route.path === '/operators'"
        >
        <VIcon name="user-admin-line" style="padding-bottom: 3px;"></VIcon>
        Operators
        </button>
      </li>
    </ul>
  </nav>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { VIcon } from '@vonage/vivid-vue'

const router = useRouter()
const route = useRoute()

function navigateTo(path: string) {
  router.push(path)
}
</script>

<style scoped lang="postcss">
.side-bar {
  overflow: hidden;
  position: sticky;
  width: 200px;
  background-color: #1f2f4a;
  height: 100vh;
  padding: 1rem 0;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-top: 1px solid white;
}

.items-list {
  list-style: none;
  padding: 0;
  margin: 0;
  width: 100%;
  max-height: 100%;
  overflow: hidden;
}

.items-list li {
  width: 100%;
  margin-bottom: 0.5rem;
}

.item {
  width: 100%;
  padding: 0.75rem 1rem;
  background: none;
  border: none;
  text-align: left;
  color: #ecf0f1;
  font-size: 1rem;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

.item:hover {
  background-color: #34495e;
}

.item.active {
  outline: none;
  background-color: #2980b9;
}
</style>
