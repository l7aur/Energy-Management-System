export default interface UserModel {
  u: MicroUser
  a: MicroAuth
}

export interface MicroUser {
  id: number
  username: string
}

export interface MicroAuth {
  id: number
  username: string
  password: string
  role: string
}
