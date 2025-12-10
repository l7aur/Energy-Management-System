import { ref } from 'vue'
import { loginUsernamePassword } from '../services/authService'

export function useLogin() {
  const isLoading = ref(false)

  async function login(username: string, password: string) {
    isLoading.value = true
    try {
      const authentication = await loginUsernamePassword({ username, password })
      return authentication
    } finally {
      isLoading.value = false
    }
  }

  return { login, isLoading }
}
