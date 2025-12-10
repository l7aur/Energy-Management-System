import { useAuthStore } from '@/stores/auth'
import DeviceView from '@/views/DeviceView.vue'
import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import UserView from '@/views/UserView.vue'
import ChatView from '@/views/ChatView.vue'
import { createRouter, createWebHistory } from 'vue-router'

const my_routes = [
  { path: '/', component: HomeView, name: 'home' },
  { path: '/login', component: LoginView, name: 'login', meta: { requiresAuth: false } },
  { path: '/register', component: RegisterView, name: 'register' },
  { path: '/device', component: DeviceView, name: 'device', meta: { requiresAuth: true } },
  {
    path: '/user',
    component: UserView,
    name: 'user',
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: '/chat',
    component: ChatView,
    name: 'chat',
    meta: { requiresAuth: true },
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: my_routes,
})

router.beforeEach((to, from, next) => {
  const auth = useAuthStore()

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return next('/login')
  }
  if (to.meta.requiresAdmin && !auth.isAdmin) {
    return next('/')
  }
  next()
})

export default router
