<template>
  <div class="chart-container">
    <h3 class="chart-title">Line Downtime Analysis</h3>
    <canvas ref="chartCanvas" class="chart-canvas"></canvas>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, onBeforeUnmount } from 'vue';
import { Chart, registerables } from 'chart.js';
import { useLineDowntimeStore } from '@/stores/lineDowntimeStore';

Chart.register(...registerables);
const lineDowntimeStore = useLineDowntimeStore();

interface DowntimeData {
  batch: number;
  f1: number;
  f2: number;
  f3: number;
  f4: number;
  f5: number;
  f6: number;
  f7: number;
  f8: number;
  f9: number;
  f10: number;
  f11: number;
  f12: number;
}

const props = defineProps<{
  data: DowntimeData | null;
}>();

const chartCanvas = ref<HTMLCanvasElement | null>(null);
let chartInstance: Chart | null = null;

const createChart = () => {
  if (!chartCanvas.value || !props.data) return;
  if (chartInstance) {
    chartInstance.destroy();
  }

  const ctx = chartCanvas.value.getContext('2d');
  if (!ctx) return;

  const factors = ['F1', 'F2', 'F3', 'F4', 'F5', 'F6', 'F7', 'F8', 'F9', 'F10', 'F11', 'F12'];
  const downtimeValues = [
    props.data.f1, props.data.f2, props.data.f3, props.data.f4,
    props.data.f5, props.data.f6, props.data.f7, props.data.f8,
    props.data.f9, props.data.f10, props.data.f11, props.data.f12
  ];

  chartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: factors,
      datasets: [{
        label: 'Downtime (minutes)',
        data: downtimeValues,
        borderColor: '#3B82F6',
        backgroundColor: 'rgba(59, 130, 246, 0.1)',
        borderWidth: 3,
        fill: true,
        tension: 0.4,
        pointBackgroundColor: '#3B82F6',
        pointBorderColor: '#ffffff',
        pointBorderWidth: 2,
        pointRadius: 6,
        pointHoverRadius: 8,
        pointHoverBackgroundColor: '#1D4ED8',
        pointHoverBorderColor: '#ffffff',
        pointHoverBorderWidth: 3,
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      aspectRatio: 2.5,
      plugins: {
        legend: {
          display: true,
          position: 'top',
          labels: {
            font: {
              size: 14,
              weight: 'bold'
            },
            color: '#374151',
            usePointStyle: true,
            pointStyle: 'circle'
          }
        },
        tooltip: {
          backgroundColor: '#1F2937',
          titleColor: '#F9FAFB',
          bodyColor: '#F9FAFB',
          borderColor: '#6B7280',
          borderWidth: 1,
          cornerRadius: 8,
          displayColors: true,
          callbacks: {
            label: (context) => {
              const label = context.label;
              const factorIndex = parseInt(label.substring(1), 10) - 1;

              const desc = factorDescriptions.value[factorIndex];
              return `${desc.description}: Operator Error - (${desc.operatorError})`;
            }
          }
        }
      },
      interaction: {
        intersect: false,
        mode: 'index'
      },
      scales: {
        y: {
          beginAtZero: true,
          grid: {
            color: 'rgba(229, 231, 235, 0.8)',
            lineWidth: 1
          },
          ticks: {
            color: '#6B7280',
            font: {
              size: 12
            },
            padding: 8
          },
          title: {
            display: true,
            text: 'Downtime (minutes)',
            color: '#374151',
            font: {
              size: 14,
              weight: 'bold'
            }
          }
        },
        x: {
          grid: {
            display: false
          },
          ticks: {
            color: '#6B7280',
            font: {
              size: 12
            },
            maxRotation: 0
          },
          title: {
            display: true,
            text: 'Failure Modes',
            color: '#374151',
            font: {
              size: 14,
              weight: 'bold'
            }
          }
        }
      },
      animation: {
        duration: 1000,
        easing: 'easeInOutQuart'
      }
    }
  });
};

const factorDescriptions = ref<Array<{ factor: number, description: string, operatorError: string }>>([]);

onMounted(async () => {
  for (let i = 1; i <= 12; i++) {
    const data = await lineDowntimeStore.getLineDowntimeFactorDescription(i);
    factorDescriptions.value.push({
      factor: i,
      description: data.description,
      operatorError: data.operatorError
    });
  }

  createChart();
});

watch(() => props.data, () => {
  createChart();
}, { deep: true });

onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.destroy();
  }
});



</script>

<style scoped>
.chart-container {
  background: white;
  border-radius: 12px;
  padding: 10px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  border: 1px solid #E5E7EB;
  margin: 16px 0;
  transition: all 0.3s ease;
  width: 100%;
  max-width: 100%;
}

.chart-container:hover {
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  transform: translateY(-5px);
}

.chart-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1F2937;
  margin-bottom: 16px;
  text-align: center;
}

.chart-canvas {
  width: 100% !important;
  height: auto !important;
  min-height: 300px;
  max-height: 500px;
}

@media (max-width: 768px) {
  .chart-container {
    padding: 16px;
  }

  .chart-title {
    font-size: 1.1rem;
  }

  .chart-canvas {
    min-height: 250px;
  }
}

@media (max-width: 480px) {
  .chart-container {
    padding: 12px;
  }

  .chart-canvas {
    min-height: 200px;
  }
}
</style>
