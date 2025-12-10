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
    <div>
      <label for="password-confirmation">Password Confirmation</label>
      <input id="password-confirmation" v-model="passwordConfirmation" type="password" required />
    </div>
    <div>
      <label for="email">E-mail</label>
      <input id="email" v-model="email" type="email" required />
    </div>

    <v-btn type="submit">Register</v-btn>
  </form>

  <p v-if="error" class="error">{{ error }}</p>
  <p v-if="info" class="info">{{ info }}</p>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRegister } from '../composable/useRegister.ts'

const username = ref('')
const password = ref('')
const passwordConfirmation = ref('')
const email = ref('')
const error = ref('')
const info = ref('')

const { register } = useRegister()

async function submit() {
  error.value = ''
  const err = await register(username.value, password.value, passwordConfirmation.value)
  if (err !== null) error.value = err
  else info.value = 'Success!'
}
</script>
