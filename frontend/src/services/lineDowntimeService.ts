import api from "./api";

export const getLineDowntimeByBatch = async (batchId: number) =>
  await api.get(`/line-downtime/${batchId}`);

export const getLineDowntimeDescription = async (fator: number) =>
  await api.get(`/line-downtime/${fator}/description`);
