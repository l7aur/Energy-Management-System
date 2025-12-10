<template>
  <form @submit.prevent="submitForm" class="space-y-3">
    <div>
      <label for="name">Device Name</label>
      <input id="name" v-model="formData.name" type="text" required />
    </div>

    <div>
      <label for="maximumConsumptionValue">Maximum Consumption Value</label>
      <input
        id="maximumConsumptionValue"
        v-model="formData.maximumConsumptionValue"
        type="number"
        step="any"
        required
      />
    </div>

    <div v-if="auth.isAdmin">
      <label for="belongerUser">Belonger User</label>
      <v-select
        id="belongerUser"
        v-model="formData.user"
        :items="usersD"
        item-title="username"
        :item-value="(item) => item"
        required
      />
    </div>

    <div class="horizontal-button-group">
      <v-btn type="submit">Save</v-btn>
      <v-btn type="button" @click="emit('cancel')">Cancel</v-btn>
    </div>
  </form>
</template>

<script setup lang="ts">
import type Device from '@/model/Device'
import { add, update } from '@/services/deviceService'
import { onMounted, ref, watch } from 'vue'
import type { PropType } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useFindAllUsersD } from '@/composable/useFindAllUsersD'

const auth = useAuthStore()
const props = defineProps({
  mode: { type: String, default: 'add' },
  formData: { type: Object as PropType<Device>, required: true },
})
const emit = defineEmits(['cancel', 'saved'])

const { usersD, findAllUsersD } = useFindAllUsersD()
onMounted(findAllUsersD)

const formData = ref<Device>({
  id: props.formData.id,
  name: props.formData.name,
  maximumConsumptionValue: props.formData.maximumConsumptionValue,
  user: usersD.value[0] ?? props.formData.user,
})

watch(
  () => props.formData,
  (newVal) => {
    formData.value = { ...newVal }
  },
)

function submitForm() {
  if (!auth.isAdmin) formData.value.user = usersD.value[0] ?? { id: 0, username: '' }
  ;(props.mode === 'update' ? update(formData.value) : add(formData.value))
    .then((response) => {
      if (!response) throw 'err'
      emit('saved')
    })
    .catch((err) => console.log('Error while saving: ', err))
}
</script>
