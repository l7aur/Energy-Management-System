import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth.ts'
import type { MicroUser } from '@/model/UserModel'
import { findAllUsersOfDevices, findMeD } from '@/services/deviceService'

export function useFindAllUsersD() {
  const isLoading = ref(false)
  const usersD = ref<MicroUser[]>([])
  const auth = useAuthStore()

  async function findAllUsersD() {
    isLoading.value = true
    try {
      if (auth.isAdmin) {
        const result = await findAllUsersOfDevices()
        usersD.value = result || Array.of({ id: 0, username: '' })
      } else {
        const result = await findMeD(auth.name ?? '')
        usersD.value = Array.of(result)
      }
    } catch (error: unknown) {
      console.log('Failed to find all users', error)
      return []
    } finally {
      isLoading.value = false
    }
  }

  return { usersD, isLoading, findAllUsersD }
}
