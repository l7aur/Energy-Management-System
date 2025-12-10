<template>
  <NavigationBar />
  <div v-if="auth.isAuthenticated"><NotificationBar /></div>
  <div class="page">
    <div v-if="!auth.isAuthenticated" class="card">
      <h1>Welcome to l7aur's Energy Management System</h1>
      <div class="horizontal-button-group">
        <button @click="navigateTo('login')">Login</button>
        <button @click="navigateTo('register')">Register</button>
      </div>
    </div>
    <div v-else class="card">
      <h1>Hello, {{ auth.name }}</h1>
    </div>
    <v-img :src="leafBulb" class="image-with-highlight" :max-width="400" :max-height="400" />
  </div>
</template>

<script setup lang="ts">
import leafBulb from '@/assets/images/leaf-bulb.svg'
import { useNavigation } from '@/utility/useNavigation'
import NavigationBar from '@/components/NavigationBar.vue'
import { useAuthStore } from '@/stores/auth'
import NotificationBar from '@/components/NotificationBar.vue'

const { navigateTo } = useNavigation()
const auth = useAuthStore()
</script>
