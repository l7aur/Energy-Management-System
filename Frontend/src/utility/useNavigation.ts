import { useRouter } from 'vue-router'

export function useNavigation() {
  const router = useRouter()

  const navigateTo = (routeName: string) => {
    router.push({ name: routeName }).catch((err) => {
      console.error('Navigation error to route:', routeName, err)
    })
  }

  return {
    navigateTo,
  }
}
