import { ref } from 'vue'
import type Device from '@/model/Device'
import { findAll, findAllByUsername } from '@/services/deviceService'
import { useAuthStore } from '@/stores/auth.ts'

export function useFindAllDevices() {
  const isLoading = ref(false)
  const devices = ref<Device[]>([])
  const auth = useAuthStore()

  async function findAllDevices() {
    isLoading.value = true
    try {
      const result = await (auth.isAdmin
        ? findAll(auth.token)
        : findAllByUsername(auth.name, auth.token))
      devices.value = result
    } catch (error: unknown) {
      console.log('Failed to find all devices:', error)
      devices.value = []
    } finally {
      isLoading.value = false
    }
  }

  return { devices, isLoading, findAllDevices }
}
