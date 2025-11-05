import {
  addBatch,
  getAllBatches,
  getBatchesByOperatorName,
  deleteBatch,
  getBatchById,
} from '@/services/batchService'
import type { Batch, BatchInput } from '@/types/Batch'
import { defineStore } from 'pinia'

export const useBatchStore = defineStore('batchStore', {
  state: () => ({
    batches: [] as Batch[],
    loading: false,
    error: null as string | null,
    message: null as string | null,
  }),
  getters: {
    batchCount: (state) => state.batches.length,
  },
  actions: {
    async loadBatches() {
      this.loading = true
      this.error = null
      try {
        const response = await getAllBatches()
        this.batches = response.data
      } catch (error) {
        if (error instanceof Error) {
          this.error = error.message || 'Failed to fetch batches'
        } else {
          this.error = 'An unknown error occurred'
        }
      } finally {
        this.loading = false
      }
    },

    async loadBatchesByOperatorName(operatorName: string) {
      this.loading = true
      this.error = null
      try {
        const response = await getBatchesByOperatorName(operatorName)
        this.batches = response.data
      } catch (error) {
        if (error instanceof Error) {
          this.error = error.message || 'Failed to fetch batches'
        } else {
          this.error = 'An unknown error occurred'
        }
      } finally {
        this.loading = false
      }
    },

    async addBatch(data: BatchInput) {
      this.loading = true
      this.error = null
      this.message = null

      try {
        const response = await addBatch(data)
        this.loadBatches()
        this.message = response.data
      } catch (error) {
        if (error instanceof Error) {
          console.error('Error adding batch:', error.message)
          this.error = 'Unable to add batch: ' + error.message
        } else {
          this.error = 'An unknown error occurred while adding batch'
        }
      } finally {
        this.loading = false
      }
      return this.message
    },

    async deleteBatch(batchId: number) {
      this.loading = true
      this.error = null
      this.message = null

      try {
        await deleteBatch(batchId)
        this.batches = this.batches.filter((batch) => batch.batch !== batchId)
        this.message = 'Batch deleted successfully'
      } catch (error) {
        if (error instanceof Error) {
          console.error('Error deleting batch:', error.message)
          this.error = 'Unable to delete batch: ' + error.message
        } else {
          this.error = 'An unknown error occurred while deleting batch'
        }
      } finally {
        this.loading = false
      }
    },

    async getBatchTotalTimeById(batchId: number) {
      this.loading = true
      this.error = null
      try {
        const response = await getBatchById(batchId)
        const startTime = response.data.start_time // e.g., "11:50:00"
        const endTime = response.data.end_time     // e.g., "13:15:00"

        const [startH, startM, startS] = startTime.split(':').map(Number)
        const [endH, endM, endS] = endTime.split(':').map(Number)

        const startMinutes = startH * 60 + startM + (startS || 0) / 60
        const endMinutes = endH * 60 + endM + (endS || 0) / 60

        // Handle negative results in case time crosses midnight
        const total_time = (endMinutes - startMinutes + 1440) % 1440

        console.log('Total time in minutes:', total_time)
        return total_time
      } catch (error) {
        if (error instanceof Error) {
          this.error = error.message || 'Failed to fetch batch'
        } else {
          this.error = 'An unknown error occurred'
        }
      } finally {
        this.loading = false
      }
    },

  },
})
