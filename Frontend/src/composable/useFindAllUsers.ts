import type UserModel from '@/model/UserModel'
import { findAll } from '@/services/userService'
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth.ts'

export function useFindAllUsers() {
  const isLoading = ref(false)
  const users = ref<UserModel[]>([])
  const auth = useAuthStore()

  async function findAllUsers() {
    isLoading.value = true
    try {
      const result = await findAll()
      users.value = result.filter((user) => {
        return user.u.username !== auth.name
      })
    } catch (error: unknown) {
      console.log('Failed to find all users', error)
      users.value = []
    } finally {
      isLoading.value = false
    }
  }

  return { users, isLoading, findAllUsers }
}
