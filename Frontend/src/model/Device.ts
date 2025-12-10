import type { MicroUser } from './UserModel'

export default interface Device {
  id: number
  name: string
  maximumConsumptionValue: number
  user: MicroUser
}
