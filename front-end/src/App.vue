<script setup>
import Footer from './components/Footer.vue'
import Header from './components/Header.vue';
import Sidebar from './components/Sidebar.vue';
</script>

<template>
  <Header />
  <Sidebar />
  <main>
    <router-view />
  </main>
  <Footer />
</template>

<style scoped></style>