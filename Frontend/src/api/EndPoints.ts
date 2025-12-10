export const AUTH_BASE_URL = 'http://localhost/auth'
export const DEVICE_BASE_URL = 'http://localhost/device/secure'
export const USER_BASE_URL = 'http://localhost/user/secure'
export const MONITORING_BASE_URL = 'http://localhost/monitoring/secure'

export const AUTH_ENDPOINTS = {
  LOGIN: `${AUTH_BASE_URL}/login`,
  REGISTER: `${AUTH_BASE_URL}/register`,
  FIND_ALL: `${AUTH_BASE_URL}/all`,
  UPDATE: `${AUTH_BASE_URL}/`,
  DELETE: `${AUTH_BASE_URL}/`,
}

export const DEVICE_ENDPOINTS = {
  FIND_ALL: `${DEVICE_BASE_URL}/all`,
  FIND_BY_USERNAME: `${DEVICE_BASE_URL}`,
  ADD_ONE: `${DEVICE_BASE_URL}/`,
  UPDATE: `${DEVICE_BASE_URL}/`,
  DELETE: `${DEVICE_BASE_URL}/`,
  ADD_ONE_USER: `${DEVICE_BASE_URL}/user/`,
  DELETE_USER: `${DEVICE_BASE_URL}/user/`,
  FIND_ALL_USERS: `${DEVICE_BASE_URL}/user/all`,
  FIND_ME: `${DEVICE_BASE_URL}/user`,
}

export const USER_ENDPOINTS = {
  FIND_ALL: `${USER_BASE_URL}/all`,
  FIND_ONE: `${USER_BASE_URL}`,
  ADD_ONE: `${USER_BASE_URL}/`,
  UPDATE: `${USER_BASE_URL}/`,
  DELETE: `${USER_BASE_URL}/`,
}

export const MONITORING_ENDPOINTS = {
  FIND_ONE: `${MONITORING_BASE_URL}`,
}
