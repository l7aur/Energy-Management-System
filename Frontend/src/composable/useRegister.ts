import { ref } from 'vue'
import { registerUsernamePassword } from '../services/authService'

export function useRegister() {
  const isLoading = ref(false)

  async function register(username: string, password: string, passwordConfirmation: string) {
    if (password != passwordConfirmation) return 'Passwords mismatch!'

    isLoading.value = true
    try {
      const reply = await registerUsernamePassword({ username, password, role: 'CLIENT' })
      return reply
    } catch {
      return 'unknown error'
    } finally {
      isLoading.value = false
    }
  }

  return { register, isLoading }
}
