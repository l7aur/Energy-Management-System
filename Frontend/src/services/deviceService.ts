import { DEVICE_ENDPOINTS } from '@/api/EndPoints'
import type Device from '@/model/Device'
import type { MicroUser } from '@/model/UserModel'
import { useAuthStore } from '@/stores/auth'
import axios, { HttpStatusCode } from 'axios'

export async function findAll(token: string | undefined): Promise<Device[]> {
  try {
    if (token === undefined) return []
    const response = await axios.get<Device[]>(DEVICE_ENDPOINTS.FIND_ALL, {
      headers: {
        Authorization: `Bearer ${useAuthStore().auth?.token}`,
      },
    })
    if (response.status !== HttpStatusCode.Ok) throw new Error('error')
    return response.data
  } catch (error: unknown) {
    console.error('Find all devices failed: ', error)
    return []
  }
}

export async function findAllUsersOfDevices(): Promise<MicroUser[]> {
  try {
    const response = await axios.get<MicroUser[]>(DEVICE_ENDPOINTS.FIND_ALL_USERS, {
      headers: {
        Authorization: `Bearer ${useAuthStore().auth?.token}`,
      },
    })
    if (response.status !== HttpStatusCode.Ok) throw new Error('error')
    return response.data
  } catch (error: unknown) {
    console.error('Find all users-d failed: ', error)
    return []
  }
}

export async function findMeD(username: string): Promise<MicroUser> {
  try {
    const response = await axios.get<MicroUser>(`${DEVICE_ENDPOINTS.FIND_ME}/${username}`, {
      headers: {
        Authorization: `Bearer ${useAuthStore().auth?.token}`,
      },
    })
    if (response.status !== HttpStatusCode.Ok) throw new Error('error')
    return response.data
  } catch (error: unknown) {
    console.error('Find all users-d failed: ', error)
    return { id: 0, username: '' }
  }
}

export async function findAllByUsername(
  username: string | undefined,
  token: string | undefined,
): Promise<Device[]> {
  try {
    if (username === undefined || token === undefined) return []
    const response = await axios.get<Device[]>(`${DEVICE_ENDPOINTS.FIND_BY_USERNAME}/${username}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    if (response.status !== HttpStatusCode.Ok) throw new Error('error')
    return response.data
  } catch (error: unknown) {
    console.error('Find all devices failed: ', error)
    return []
  }
}

export async function add(device: Device): Promise<Device> {
  try {
    const response = await axios.post(
      DEVICE_ENDPOINTS.ADD_ONE,
      {
        name: device.name,
        maximumConsumptionValue: device.maximumConsumptionValue,
        user: device.user,
      },
      {
        headers: {
          Authorization: `Bearer ${useAuthStore().auth?.token}`,
        },
      },
    )

    if (response.status !== HttpStatusCode.Created) throw new Error('error')
    return response.data
  } catch (error: unknown) {
    console.error('Adding new device failed: ', error)
    return {
      id: 0,
      name: '',
      maximumConsumptionValue: 0.0,
      user: { id: 0, username: '' },
    }
  }
}

export async function update(device: Device): Promise<Device> {
  try {
    const response = await axios.put(
      DEVICE_ENDPOINTS.UPDATE,
      {
        id: device.id,
        name: device.name,
        maximumConsumptionValue: device.maximumConsumptionValue,
        user: device.user,
      },
      {
        headers: {
          Authorization: `Bearer ${useAuthStore().auth?.token}`,
        },
      },
    )
    if (response.status !== HttpStatusCode.Ok) throw new Error('error')
    return response.data
  } catch (error: unknown) {
    console.error('Updating device failed: ', error)
    return {
      id: 0,
      name: '',
      maximumConsumptionValue: 0.0,
      user: { id: 0, username: '' },
    }
  }
}

export async function remove(devices: Device[]): Promise<boolean> {
  try {
    const response = await axios.delete(DEVICE_ENDPOINTS.DELETE, {
      data: { ids: devices.map((d) => d.id) },
      headers: {
        Authorization: `Bearer ${useAuthStore().auth?.token}`,
      },
    })
    if (response.status !== HttpStatusCode.Ok) throw new Error('error')
    return true
  } catch (error: unknown) {
    console.error('Updating device failed: ', error)
    return false
  }
}
