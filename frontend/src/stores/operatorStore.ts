import { getAllOperator } from "@/services/batchService";
import type { Operator } from "@/types/Operator";
import { defineStore } from "pinia";

export const useOperatorStore = defineStore("operatorStore", {
  state: () => ({
    operators: [] as Operator[],
    loading: false,
    error: null as string | null,
  }),
  getters: {
    operatorCount: (state) => state.operators.length
  },
  actions: {
    async loadOperators() {
      this.loading = true;
      this.error = null;
      try {
        const response = await getAllOperator();
        this.operators = response.data;
      } catch (error) {
        this.error = (error as Error).message || "failed to fetch operators"
      }finally{
        this.loading = false;
      }
    },
  }
});
