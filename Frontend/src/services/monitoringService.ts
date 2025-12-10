import { MONITORING_ENDPOINTS } from '@/api/EndPoints'
import type SensorData from '@/model/SensorData'
import { useAuthStore } from '@/stores/auth'
import axios, { HttpStatusCode } from 'axios'

export async function getSensorData(deviceId: number, date: string): Promise<SensorData[]> {
  try {
    const response = await axios.get<SensorData[]>(
      `${MONITORING_ENDPOINTS.FIND_ONE}/${deviceId}?date=${date}`,
      {
        headers: {
          Authorization: `Bearer ${useAuthStore().auth?.token}`,
        },
      },
    )
    if (response.status !== HttpStatusCode.Ok) throw new Error('error')
    return response.data
  } catch (error: unknown) {
    console.log('Failed to retrieve sensor data associated to id: ', deviceId, error)
    return []
  }
}
