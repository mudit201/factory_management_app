<script setup lang="ts">
import { RouterView } from 'vue-router'
// import HelloWorld from './components/HelloWorld.vue'
import NavBar from './components/NavBar.vue';
import SideBar from './components/SideBar.vue';
import { VBanner } from '@vonage/vivid-vue';
import { computed, ref } from 'vue';
import { useBannerStore } from './stores/bannerStore';

const bannerStore = useBannerStore();
const showBanner = computed(() => bannerStore.showbanner.value);
const text = computed(() => bannerStore.text.value);
const isError = computed(() => bannerStore.isError.value);
console.log("bannerStore----", bannerStore.showbanner.value);

</script>

<template>
  <header>
    <NavBar/>
  </header>
  <VBanner v-if="showBanner" :text="text" :connotation="isError ? 'alert' : 'success'" ></VBanner>
  <div class="main-content">
    <div class="sidebar-container">
      <SideBar/>
    </div>
    <div class="router-view">
      <RouterView />
    </div>
  </div>

</template>
<style scoped>
.main-content{
  display: flex;
  flex-direction: row;
  gap: 20px;
  height: 100vh;
  overflow: hidden;
}

.sidebar-container{
  position: sticky;
  top: 0;
  height: 100vh;
  overflow-y: auto;
  flex-shrink: 0;
  background: #fff;
  z-index: 1;
}

.router-view {
  flex: 1;
  overflow-y: auto;
  height: 100vh;
  padding-right: 10px;
}

</style>
