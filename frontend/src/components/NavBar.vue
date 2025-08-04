<template>
  <div class="nav">

    <h1 class="logo"><VIcon name="vonage-line"></VIcon> FactoryManagement by API-BU</h1>
    <div id="search-bar">
      <input type="text" placeholder="Search..." />
    </div>
    <div class="profile-icon">M</div>
  </div>
</template>

<script setup lang="ts">
import { VIcon } from '@vonage/vivid-vue';
</script>

<style scoped lang="postcss">
.nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: var(--color-bg);
  padding: 0.5rem 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  font-family: var(--font-family);
  color: var(--color-text);

  .logo {
    font-size: 1.25rem;
    font-weight: 700;
    user-select: none;
    color: #ecf0f1;
  }

  #search-bar input {
    width: 250px;
    padding: 0.4rem 0.8rem;
    border-radius: var(--border-radius);
    border: none;
    font-size: 1rem;
    transition: box-shadow 0.3s ease;

    &:focus {
      box-shadow: 0 0 8px var(--color-primary);
    }
  }

  .profile-icon {
    background-color: var(--color-primary);
    width: 38px;
    height: 38px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: bold;
    font-size: 1.1rem;
    color: white;
    cursor: pointer;

    &:hover {
      background-color: var(--color-primary-dark);
    }
  }
}
</style>
