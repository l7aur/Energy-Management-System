import type Message from '@/model/Message'
import { ref } from 'vue'

export interface WSMessageEvent {
  type: 'message'
  data: Message
}

export interface WSOpenEvent {
  type: 'open'
}

export interface WSCloseEvent {
  type: 'close'
}

export interface WSErrorEvent {
  type: 'error'
}

export type WSEvent = WSMessageEvent | WSOpenEvent | WSCloseEvent | WSErrorEvent
export type WSListener = (ev: WSEvent) => void

export class WebSocketService {
  private url: string | null
  private socket: WebSocket | null = null
  private listeners = new Set<WSListener>()

  // reactive state: 'closed' | 'connecting' | 'open'
  state = ref<'closed' | 'connecting' | 'open'>('closed')

  constructor(url: string | null) {
    this.url = url
  }

  connect() {
    if (!this.url) {
      // MOCK MODE
      this.mockConnect()
      return
    }

    if (this.state.value === 'open' || this.state.value === 'connecting') return

    this.state.value = 'connecting'
    this.socket = new WebSocket(this.url)

    this.socket.onopen = () => {
      this.state.value = 'open'
      this.emit({ type: 'open' })
    }

    this.socket.onmessage = (e) => {
      let data = null
      try {
        data = JSON.parse(e.data)
      } catch {
        data = e.data
      }
      this.emit({ type: 'message', data })
    }

    this.socket.onerror = () => {
      this.emit({ type: 'error' })
    }

    this.socket.onclose = () => {
      this.state.value = 'closed'
      this.emit({ type: 'close' })
    }
  }

  disconnect() {
    if (this.socket) {
      this.socket.close()
      this.socket = null
    }
    this.state.value = 'closed'
  }

  send(data: Message) {
    if (!this.socket || this.state.value !== 'open') return
    this.socket.send(JSON.stringify(data))
  }

  on(listener: WSListener) {
    this.listeners.add(listener)
  }

  off(listener: WSListener) {
    this.listeners.delete(listener)
  }

  private emit(ev: WSEvent) {
    for (const listener of this.listeners) listener(ev)
  }

  // ========== MOCK MODE ==========
  private mockConnect() {
    this.state.value = 'open'
    this.emit({ type: 'open' })

    // simulate server sending messages every 4 seconds
    setInterval(() => {
      this.emit({
        type: 'message',
        data: {
          username: 'Support',
          message: 'This is a mock reply from the server.',
        },
      })
    }, 4000)
  }
}
