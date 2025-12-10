<template>
  <nav class="navbar">
    <div>
      <v-btn @click="navigateTo('home')">Home</v-btn>
      <v-btn v-if="auth.isAdmin" @click="navigateTo('user')">Users</v-btn>
      <v-btn v-if="auth.isAuthenticated" @click="navigateTo('device')">Devices</v-btn>
      <v-btn v-if="auth.isAuthenticated" @click="navigateTo('chat')">Chat</v-btn>
    </div>
    <div>
      <v-btn v-if="!auth.isAuthenticated" @click="navigateTo('login')">Log in</v-btn>
      <v-btn v-else @click="logout">Log out</v-btn>
      <v-btn v-if="!auth.isAuthenticated" @click="navigateTo('register')">Register</v-btn>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { useNavigation } from '@/utility/useNavigation'
import { useAuthStore } from '@/stores/auth'

const { navigateTo } = useNavigation()
const auth = useAuthStore()

function logout() {
  auth.logout()
  navigateTo('home')
}
</script>
