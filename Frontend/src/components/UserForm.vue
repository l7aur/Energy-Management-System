<template>
  <form @submit.prevent="submitForm" class="space-y-3">
    <div>
      <label for="username">Username</label>
      <input id="username" v-model="username" type="username" required />
    </div>

    <div>
      <label for="password">Password</label>
      <input id="password" v-model="formData.a.password" type="password" required />
    </div>

    <div>
      <label for="role">Role</label>
      <v-select id="role" v-model="formData.a.role" :items="['CLIENT', 'ADMIN']" required />
    </div>

    <div class="horizontal-button-group">
      <v-btn type="submit">Save</v-btn>
      <v-btn type="button" @click="emit('cancel')">Cancel</v-btn>
    </div>
  </form>
</template>

<script setup lang="ts">
import type UserModel from '@/model/UserModel'
import { registerUsernamePassword, update } from '@/services/authService'
import { computed, ref, watch } from 'vue'
import type { PropType } from 'vue'

const props = defineProps({
  mode: { type: String, default: 'add' },
  formData: { type: Object as PropType<UserModel>, required: true },
})
const emit = defineEmits(['cancel', 'saved'])

const formData = ref<UserModel>({ ...props.formData })
const username = computed({
  get: () => formData.value.a.username,
  set: (value) => {
    formData.value.u.username = value
    formData.value.a.username = value
  },
})

watch(
  () => props.formData,
  (newVal) => {
    formData.value = { ...newVal }
  },
)

function submitForm() {
  if (props.mode === 'update') {
    update(formData.value)
  } else {
    const response = registerUsernamePassword({
      username: formData.value.a.username,
      password: formData.value.a.password,
      role: 'CLIENT',
    })
    if (response !== null) console.log(response)
  }
  emit('saved')
}
</script>
