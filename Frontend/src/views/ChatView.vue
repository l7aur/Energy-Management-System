<template>
  <NavigationBar />
  <NotificationBar />
  <div class="page">
    <section class="chat-container">
      <header class="chat-header">
        <h2>Live Chat</h2>
        <div class="status" :class="connectionState">{{ connectionLabel }}</div>
      </header>

      <main class="messages" ref="messagesEl">
        <div
          v-for="(msg, i) in messages"
          :key="i"
          :class="['message', { me: msg.username === useAuthStore().auth?.username }]"
        >
          <div class="meta">
            <span class="author">{{ msg.username }}</span>
            <span class="time">{{ formatTime(new Date().getTime()) }}</span>
          </div>
          <div class="body">{{ msg.message }}</div>
        </div>
      </main>

      <form class="composer" @submit.prevent="sendMessage">
        <input
          v-model="draft"
          @keydown.enter.exact.prevent="sendMessage"
          placeholder="Type a message and press Enter"
          aria-label="Message"
        />
        <button type="button" class="page.button" @click="sendMessage" :disabled="!canSend">
          Send
        </button>
      </form>
    </section>
  </div>
</template>

<script setup lang="ts">
import NavigationBar from '@/components/NavigationBar.vue'
import NotificationBar from '@/components/NotificationBar.vue'
import type Message from '@/model/Message'
import { WebSocketService, type WSListener } from '@/services/WebSocketService'
import { useAuthStore } from '@/stores/auth'
import { ref, onMounted, onBeforeUnmount, computed, nextTick } from 'vue'

const messages = ref<Message[]>([])
const draft = ref('')
const messagesEl = ref<HTMLElement | null>(null)

const wsUrl: string | null = 'ws://localhost/customersupport/public/ws'
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
    if (ev.type === 'open') {
      // optionally fetch initial messages from a REST endpoint here.
    }
    if (ev.type === 'message' && ev.data) {
      messages.value.push({
        username: ev.data.username || 'Customer Support',
        message: ev.data.message || '',
      })
      scrollToBottom()
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

  // cleanup
  onBeforeUnmount(() => {
    tries = 0
    if (reconnectTimer) clearTimeout(reconnectTimer)
    ws.disconnect()
  })
})

function formatTime(ts: number) {
  const d = new Date(ts)
  return `${fmt(d.getHours())}:${fmt(d.getMinutes())}`
}

const canSend = computed(() => draft.value.trim().length > 0 && ws.state.value === 'open')

function sendMessage() {
  const text = draft.value.trim()
  if (!text) return

  const msg: Message = { username: useAuthStore().auth?.username ?? '', message: text }
  messages.value.push(msg)
  scrollToBottom()

  ws.send({ username: useAuthStore().auth?.username ?? '', message: text })

  draft.value = ''
}

function scrollToBottom() {
  nextTick(() => {
    const el = messagesEl.value
    if (!el) return
    el.scrollTop = el.scrollHeight
  })
}

const fmt = (n: number) => n.toString().padStart(2, '0')
</script>
