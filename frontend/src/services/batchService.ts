import type { Batch, BatchInput } from "@/types/Batch"
import api from "./api"
import type { Operator } from "@/types/Operator";

export const getAllBatches = () => api.get<Batch[]>("/batches");
export const getAllOperator = () => api.get<Operator[]>("/operators");
export const getBatchesByOperatorName = (operatorName: string) =>
  api.get<Batch[]>(`/operators/${operatorName}/batches`)

export const addBatch= async (data: BatchInput) => await api.post<string>("/batches", data)
export const deleteBatch = async (batchId: number) => await api.delete(`/batch/${batchId}`)

export const getBatchById = async (batchId: number) => await api.get<Batch>(`/batches/${batchId}`)
