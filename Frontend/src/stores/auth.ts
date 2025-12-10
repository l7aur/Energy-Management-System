import type Auth from '@/model/Auth'
import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    auth: null as Auth | null,
  }),
  getters: {
    isAuthenticated: (state) => !!state.auth,
    isAdmin: (state) => state.auth?.role === 'ADMIN' || state.auth?.role === 'admin',
    name: (state) => state.auth?.username,
    token: (state) => state.auth?.token,
  },
  actions: {
    login(authData: Auth) {
      this.auth = authData
    },
    logout() {
      this.auth = null
    },
  },
})
