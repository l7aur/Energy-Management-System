<template>
  <NavigationBar />
  <div class="page">
    <div class="card">
      <h1>Login</h1>
      <LoginForm />
    </div>
    <v-img :src="plugLeaf" class="image-with-highlight" :max-width="400" :max-height="400" />
  </div>
</template>

<script setup lang="ts">
import plugLeaf from '@/assets/images/plugleaf.svg'
import LoginForm from '@/components/LoginForm.vue'
import NavigationBar from '@/components/NavigationBar.vue'
</script>
