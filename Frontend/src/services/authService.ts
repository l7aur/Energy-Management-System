import { AUTH_ENDPOINTS } from '@/api/EndPoints'
import type Auth from '@/model/Auth'
import type { MicroAuth } from '@/model/UserModel'
import type UserModel from '@/model/UserModel'
import { useAuthStore } from '@/stores/auth'
import axios, { HttpStatusCode } from 'axios'
import { jwtDecode } from 'jwt-decode'

export async function loginUsernamePassword(request: LoginRequest): Promise<Auth> {
  try {
    const response = await axios.post<LoginReply>(AUTH_ENDPOINTS.LOGIN, {
      username: request.username,
      password: request.password,
    })
    if (response.status !== HttpStatusCode.Ok) throw Error('err')

    const decoded: { role: string } = jwtDecode(response.data.jwtToken)
    return {
      username: response.data.username,
      token: response.data.jwtToken,
      role: decoded.role,
    }
  } catch (error: unknown) {
    console.error('Login failed: ', error)
    return { username: '', token: '', role: '' }
  }
}

export async function registerUsernamePassword(request: RegisterRequest): Promise<string> {
  try {
    const response = await axios.post<RegisterReply>(AUTH_ENDPOINTS.REGISTER, {
      id: null,
      username: request.username,
      password: request.password,
      role: request.role,
    })
    if (response.status !== HttpStatusCode.Created) throw new Error('call1')

    return response.data.errorMessage
  } catch (error: unknown) {
    console.error('Registration failed: ', error)
    return 'Error'
  }
}

export async function update(user: UserModel): Promise<UserModel> {
  const auth = useAuthStore()
  try {
    const response = await axios.put<MicroAuth>(
      AUTH_ENDPOINTS.UPDATE,
      {
        id: user.a.id,
        username: user.a.username,
        password: user.a.password,
        role: user.a.role,
      },
      {
        headers: {
          Authorization: `Bearer ${auth.token}`,
        },
      },
    )
    if (response.status !== HttpStatusCode.Ok) throw new Error('call2')
    return {
      u: { id: user.u.id, username: user.a.username },
      a: {
        id: response.data.id,
        username: response.data.username,
        password: response.data.password,
        role: response.data.role,
      },
    }
  } catch (error: unknown) {
    console.error('Updating user failed: ', error)
    return { u: { id: 0, username: '' }, a: { id: 0, username: '', password: '', role: '' } }
  }
}

export async function remove(users: UserModel[]): Promise<boolean> {
  const auth = useAuthStore()
  try {
    const response = await axios.delete<number[]>(AUTH_ENDPOINTS.DELETE, {
      data: { ids: users.map((user) => user.a.id) },
      headers: {
        Authorization: `Bearer ${auth.token}`,
      },
    })
    if (response.status !== HttpStatusCode.Ok) throw new Error('call 2')
    return true
  } catch (error: unknown) {
    console.error('Deleting user failed:', error)
    return false
  }
}

interface LoginRequest {
  username: string
  password: string
}

interface LoginReply {
  username: string
  jwtToken: string
}

interface RegisterRequest {
  username: string
  password: string
  role: string
}

interface RegisterReply {
  errorMessage: string
  token: string
}
