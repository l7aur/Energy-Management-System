<!-- eslint-disable vue/valid-v-slot -->
<template>
  <NavigationBar />
  <NotificationBar />
  <div class="page">
    <div class="card">
      <div class="navbar">
        <h1>Users</h1>
        <v-btn @click="addItem">Add User</v-btn>
      </div>
      <div v-if="isLoading">Loading...</div>
      <v-data-table v-else :items="users" :headers="headers">
        <template #item.actions="{ item }">
          <v-icon small @click="editItem(item)">mdi-pencil</v-icon>
          <v-icon small color="red" @click="deleteItem(item)">mdi-delete</v-icon>
        </template>
      </v-data-table>

      <v-dialog class="page" max-width="50%" v-model="showForm" location="center center">
        <UserForm :mode="formMode" :formData="formData" @cancel="cancelForm" @saved="handleSave" />
      </v-dialog>
    </div>
    <v-img :src="greenbulb" class="image-with-highlight" :max-width="400" :max-height="400" />
  </div>
</template>

<script setup lang="ts">
import greenbulb from '@/assets/images/greenbulb.svg'
import NavigationBar from '@/components/NavigationBar.vue'
import NotificationBar from '@/components/NotificationBar.vue'
import UserForm from '@/components/UserForm.vue'
import { useFindAllUsers } from '@/composable/useFindAllUsers'
import type UserModel from '@/model/UserModel'
import { remove } from '@/services/authService'
import { onMounted, ref } from 'vue'

const { users, isLoading, findAllUsers } = useFindAllUsers()
onMounted(findAllUsers)

const headers = ref([
  { title: 'Username', value: 'u.username' },
  { title: 'Password', value: 'a.password' },
  { title: 'Role', value: 'a.role' },
  { title: 'Actions', value: 'actions', sortable: false },
])

const showForm = ref(false)
const formMode = ref<'add' | 'update'>('add')
const formData = ref<UserModel>({
  u: { id: 0, username: '' },
  a: { id: 0, username: '', password: '', role: 'CLIENT' },
})

const addItem = () => {
  formMode.value = 'add'
  formData.value = {
    u: { id: 0, username: '' },
    a: { id: 0, username: '', password: '', role: 'CLIENT' },
  }
  showForm.value = true
  findAllUsers()
}

const editItem = (item: UserModel) => {
  formMode.value = 'update'
  formData.value = { ...item }
  showForm.value = true
  findAllUsers()
}

const deleteItem = (item: UserModel) => {
  remove([item])
    .then((response) => {
      if (!response) throw 'err'
      findAllUsers()
    })
    .catch((e) => console.log(e))
}

const cancelForm = () => {
  showForm.value = false
}

const handleSave = () => {
  showForm.value = false
  findAllUsers()
}
</script>
