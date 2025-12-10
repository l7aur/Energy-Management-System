<!-- eslint-disable vue/valid-v-slot -->
<template>
  <NavigationBar />
  <NotificationBar />
  <div class="page">
    <div class="card">
      <div class="navbar">
        <h1>Devices</h1>
        <v-btn @click="addItem">Add Device</v-btn>
      </div>
      <MonitoringForm
        v-if="showDetails"
        :device-id="deviceDetails.id"
        :device-name="deviceDetails.name"
      >
      </MonitoringForm>
      <div v-if="isLoading">Loading...</div>
      <v-data-table v-else :items="devices" :headers="headers">
        <template #item.openDetailedView="{ item }">
          <v-icon class="mx-auto d-block" small @click="openDetails(item)">mdi-open-in-new</v-icon>
        </template>
        <template #item.actions="{ item }">
          <v-icon class="mx-auto d-block" small @click="editItem(item)">mdi-pencil</v-icon>
          <v-icon class="mx-auto d-block" color="red" small @click="deleteItem(item)"
            >mdi-delete</v-icon
          >
        </template>
      </v-data-table>

      <v-dialog class="page" max-width="50%" v-model="showForm" location="center center">
        <DeviceForm
          :mode="formMode"
          :formData="formData"
          @cancel="cancelForm"
          @saved="handleSave"
        />
      </v-dialog>
    </div>
    <v-img :src="earthhand" class="image-with-highlight" :max-width="300" :max-height="300" />
  </div>
</template>

<script setup lang="ts">
import earthhand from '@/assets/images/earthhand.svg'
import DeviceForm from '@/components/DeviceForm.vue'
import MonitoringForm from '@/components/MonitoringForm.vue'
import NotificationBar from '@/components/NotificationBar.vue'
import NavigationBar from '@/components/NavigationBar.vue'
import { useFindAllDevices } from '@/composable/useFindAllDevices'
import type Device from '@/model/Device'
import { remove } from '@/services/deviceService'
import { onMounted, ref } from 'vue'

const { devices, isLoading, findAllDevices } = useFindAllDevices()
onMounted(findAllDevices)

const headers = ref([
  { title: 'Open Detailed View', value: 'openDetailedView', sortable: false },
  { title: 'Id', value: 'id' },
  { title: 'Name', value: 'name' },
  { title: 'MaximumConsumptionValue', value: 'maximumConsumptionValue' },
  { title: 'BelongerUsername', value: 'user.username' },
  { title: 'Actions', value: 'actions', sortable: false },
])

const showForm = ref(false)
const formMode = ref<'add' | 'update'>('add')
const formData = ref<Device>({
  id: 0,
  name: 'some device...',
  maximumConsumptionValue: 0,
  user: { id: 0, username: '' },
})

const showDetails = ref(false)
const deviceDetails = ref<Device>({
  id: 0,
  name: '',
  maximumConsumptionValue: 0,
  user: { id: 0, username: '' },
})

const addItem = () => {
  formMode.value = 'add'
  formData.value = { id: 0, name: '', maximumConsumptionValue: 0, user: { id: 0, username: '' } }
  showForm.value = true
}

const editItem = (item: Device) => {
  formMode.value = 'update'
  formData.value = { ...item }
  showForm.value = true
}

const deleteItem = (item: Device) => {
  showDetails.value = false
  remove([item])
    .then((response) => {
      if (!response) throw 'err'
      findAllDevices()
    })
    .catch((err) => console.log('Error while deleting: ', err))
}

const cancelForm = () => {
  showForm.value = false
}

const handleSave = () => {
  showForm.value = false
  findAllDevices()
}

const openDetails = (item: Device) => {
  showDetails.value = item !== deviceDetails.value
  if (showDetails.value === true) deviceDetails.value = item
  else
    deviceDetails.value = {
      id: 0,
      name: '',
      maximumConsumptionValue: 0,
      user: { id: 0, username: '' },
    }
}
</script>
