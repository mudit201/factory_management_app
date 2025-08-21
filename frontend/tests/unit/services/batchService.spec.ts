// batchService.test.ts
import * as batchService from '../../../src/services/batchService'
import api from "../../../src/services/api";

jest.mock("../../../src/services/api"); // Mock the entire api module

describe("batchService", () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it("calls api.get with /batches on getAllBatches", () => {
    batchService.getAllBatches();
    expect(api.get).toHaveBeenCalledWith("/batches");
  });

  it("calls api.get with /operators on getAllOperator", () => {
    batchService.getAllOperator();
    expect(api.get).toHaveBeenCalledWith("/operators");
  });

  it("calls api.get with correct operator batches URL on getBatchesByOperatorName", () => {
    const operatorName = "john";
    batchService.getBatchesByOperatorName(operatorName);
    expect(api.get).toHaveBeenCalledWith(`/operators/${operatorName}/batches`);
  });

  it("calls api.post with /batches and data on addBatch", async () => {
    const data = { name: "batch1" };
    await batchService.addBatch(data);
    expect(api.post).toHaveBeenCalledWith("/batches", data);
  });

  it("calls api.delete with correct batch id URL on deleteBatch", async () => {
    const batchId = 123;
    await batchService.deleteBatch(batchId);
    expect(api.delete).toHaveBeenCalledWith(`/batch/${batchId}`);
  });
});
