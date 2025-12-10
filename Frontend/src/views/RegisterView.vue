<template>
  <NavigationBar />
  <div class="page">
    <div class="card">
      <h1>Register</h1>
      <RegisterForm />
    </div>
    <v-img :src="sunButton" class="image-with-highlight" :max-width="400" :max-height="400" />
  </div>
</template>

<script setup lang="ts">
import sunButton from '@/assets/images/sunbutton.svg'
import RegisterForm from '@/components/RegisterForm.vue'
import '../assets/page.css'
import NavigationBar from '@/components/NavigationBar.vue'
</script>
