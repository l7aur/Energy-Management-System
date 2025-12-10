<template>
  <v-card class="mt-8 mx-auto df">
    <v-card-text class="d-flex justify-center">
      <v-date-picker v-model="selectedDate"></v-date-picker>
    </v-card-text>

    <v-card-text>
      <div class="text-h6 font-weight-light mb-2">{{ deviceName }}</div>
      <div class="subheading font-weight-light text-grey">Device Energy Usage (Live Data)</div>
    </v-card-text>

    <v-card-text class="c">
      <v-chart class="chart" :option="chartOptions" autoresize />
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { use } from 'echarts/core'
import { LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, TitleComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import VChart from 'vue-echarts'

import { getSensorData } from '@/services/monitoringService'
import type SensorData from '@/model/SensorData'

use([LineChart, GridComponent, TooltipComponent, TitleComponent, CanvasRenderer])

const props = defineProps<{
  deviceId: number
  deviceName: string
}>()

const labels = ref<string[]>([])
const values = ref<number[]>([])
const lastUpdateMessage = ref('')
const selectedDate = ref('')
const chartOptions = ref({})

function formatDateForBackend(dateString: string): string {
  const date = new Date(decodeURIComponent(dateString)) // decode URL and parse
  const yyyy = date.getFullYear()
  const mm = String(date.getMonth() + 1).padStart(2, '0')
  const dd = String(date.getDate()).padStart(2, '0')

  return `${yyyy}-${mm}-${dd}`
}

async function loadDevicePowerData() {
  try {
    const sensorData: SensorData[] = await getSensorData(
      props.deviceId,
      formatDateForBackend(selectedDate.value),
    )

    labels.value = sensorData.map((sd) => sd.timestamp.substring(11))
    values.value = sensorData.map((sd) => sd.measuredValue)
  } catch (e) {
    console.error('API failed', e)
  }

  updateChart()
}

function updateChart() {
  chartOptions.value = {
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: labels.value,
    },
    yAxis: {
      type: 'value',
      name: 'kWh',
    },
    series: [
      {
        name: 'Power (kWh)',
        type: 'line',
        smooth: true,
        data: values.value,
      },
    ],
    grid: { left: 40, right: 20, bottom: 40, top: 20 },
  }
  lastUpdateMessage.value = labels.value?.[labels.value.length - 1] ?? '???'
}

watch(
  () => props.deviceId,
  () => {
    selectedDate.value = new Date().toISOString().substring(0, 10)
    loadDevicePowerData()
  },
  { immediate: true },
)

watch(
  () => selectedDate.value,
  (newDate) => {
    if (!newDate) return
    loadDevicePowerData()
  },
)
</script>

<style scoped>
.chart {
  width: 100%;
  height: 300px;
}
</style>
