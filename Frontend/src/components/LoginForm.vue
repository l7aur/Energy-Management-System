<template>
  <form @submit.prevent="submit">
    <div>
      <label for="username">Username</label>
      <input id="username" v-model="username" type="username" required />
    </div>
    <div>
      <label for="password">Password</label>
      <input id="password" v-model="password" type="password" required />
    </div>

    <button type="submit">Login</button>
  </form>

  <p v-if="error" class="error">{{ error }}</p>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useLogin } from '../composable/useLogin'
import { useAuthStore } from '@/stores/auth'
import { useNavigation } from '@/utility/useNavigation'

const username = ref('')
const password = ref('')
const error = ref('')

const { login } = useLogin()
const auth = useAuthStore()

const { navigateTo } = useNavigation()

async function submit() {
  const authentication = await login(username.value, password.value)
  if (authentication.token === '') error.value = 'Invalid credentials!'
  else auth.login(authentication)
  navigateTo('home')
}
</script>
