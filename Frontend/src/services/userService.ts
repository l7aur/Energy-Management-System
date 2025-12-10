import { AUTH_ENDPOINTS, USER_ENDPOINTS } from '@/api/EndPoints'
import type { MicroAuth, MicroUser } from '@/model/UserModel'
import type UserModel from '@/model/UserModel'
import { useAuthStore } from '@/stores/auth'
import axios, { HttpStatusCode } from 'axios'

export async function findAll(): Promise<UserModel[]> {
  const auth = useAuthStore()
  try {
    const response1 = await axios.get<MicroUser[]>(USER_ENDPOINTS.FIND_ALL, {
      headers: {
        Authorization: `Bearer ${auth.token}`,
      },
    })
    if (response1.status !== HttpStatusCode.Ok) throw new Error('call 1')
    const response2 = await axios.get<MicroAuth[]>(AUTH_ENDPOINTS.FIND_ALL, {
      headers: {
        Authorization: `Bearer ${auth.token}`,
      },
    })
    if (response2.status !== HttpStatusCode.Ok) throw new Error('call 2')

    const combined: UserModel[] = response1.data.map((u) => {
      const a = response2.data.find((auth) => auth.username === u.username)
      return { u, a: a ?? { id: 0, username: '', password: '', role: '' } }
    })

    return combined
  } catch (error: unknown) {
    console.error('Find all users failed: ', error)
    return []
  }
}
