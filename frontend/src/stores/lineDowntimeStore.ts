import { getLineDowntimeByBatch, getLineDowntimeDescription } from "@/services/lineDowntimeService"
import type { LineDowntime } from "@/types/LineDowntime"
import { defineStore } from "pinia"

export const useLineDowntimeStore = defineStore("lineDowntimeStore", {
  state: () => ({
    lineDowntime: null as LineDowntime | null,
    loading: false,
    error: null as string | null,
  }),
  actions: {
    getLineDowntime: async function (batchId: number) {
      this.loading = true
      try {
        const response = await getLineDowntimeByBatch(batchId)
        console.log(response.data)
        this.lineDowntime = response.data
      } catch (error) {
        this.error = (error as Error).message || "Failed to fetch line downtime";
      } finally {
        this.loading = false
      }
    },
    getLineDowntimeFactorDescription: async function (factor: number) {
      this.loading = true
      this.error = null
      try {
        const response = await getLineDowntimeDescription(factor)
        return response.data
      } catch (error) {
        this.error = (error as Error).message || "Failed to fetch line downtime description";
      } finally {
        this.loading = false
      }
    },
  }
})
