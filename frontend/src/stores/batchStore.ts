import {
  addBatch,
  getAllBatches,
  getBatchesByOperatorName,
  deleteBatch,
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
  },
})
