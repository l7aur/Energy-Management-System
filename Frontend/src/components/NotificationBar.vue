<template>
  <nav class="navbar">
    <div class="status" :class="connectionState">
      {{ connectionLabel }}
    </div>
    <v-card-text v-if="auth.isAuthenticated" :readonly="true"> {{ message.message }} </v-card-text>
  </nav>
</template>

<script setup lang="ts">
import type Message from '@/model/Message'
import { WebSocketService, type WSListener } from '@/services/WebSocketService'
import { useAuthStore } from '@/stores/auth'
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'

const auth = useAuthStore()

const message = ref<Message>({ username: '', message: '' })

const wsUrl: string | null = 'ws://localhost/notification/public/ws?token=' + auth.token
const ws = new WebSocketService(wsUrl)

const connectionLabel = computed(() => {
  if (ws.state.value === 'connecting') return 'Connecting…'
  if (ws.state.value === 'open') return 'Online'
  return 'Offline'
})

const connectionState = computed(() =>
  ws.state.value === 'open' ? 'online' : ws.state.value === 'connecting' ? 'connecting' : 'offline',
)

let reconnectTimer: number | null = null
let tries = 1

function ensureConnected() {
  if (ws.state.value === 'open' || ws.state.value === 'connecting') return
  ws.connect()
}

onMounted(() => {
  tries = 1
  ensureConnected()

  const listener: WSListener = (ev) => {
    if (ev.type === 'message' && ev.data) {
      message.value = {
        username: ev.data.username || 'notif',
        message: ev.data.message || 'notif',
      }
    }
  }

  ws.on(listener)

  ws.on((ev: { type: string }) => {
    if (tries <= 0) return
    if (ev.type === 'close') {
      if (reconnectTimer) clearTimeout(reconnectTimer)
      reconnectTimer = window.setTimeout(() => {
        ensureConnected()
        tries--
      }, 2000)
    }
  })

  onBeforeUnmount(() => {
    tries = 0
    if (reconnectTimer) clearTimeout(reconnectTimer)
    ws.disconnect()
  })
})
</script>
