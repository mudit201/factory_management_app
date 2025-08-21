import { setActivePinia, createPinia } from 'pinia'
import { useBatchStore } from '../../../src/stores/batchStore'
import * as batchService from '../../../src/services/batchService'

jest.mock('../../../src/services/batchService')

describe('batchStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  it('loads batches correctly', async () => {
    const mockData = [
      { batch: 1, product: 'Test', operator: 'Op', start_time: '08:00', end_time: '09:00', date: '2025-08-13' }
    ]
    ;(batchService.getAllBatches as jest.Mock).mockResolvedValue({ data: mockData })

    const store = useBatchStore()
    await store.loadBatches()

    expect(store.batches).toEqual(mockData)
    // expect(store.batches.size()).toEqual(1)
    expect(store.error).toBeNull()
  })

  it('loads batches correctly by operator name', async () => {
    (batchService.getBatchesByOperatorName as jest.Mock).mockImplementation((operatorName: string) => {
      const mockData = [
        { batch: 1, product: 'NP-1', operator: 'Sam', start_time: '08:00', end_time: '09:00', date: '2025-08-13' },
        { batch: 2, product: 'NP-2', operator: 'Sam', start_time: '09:00', end_time: '10:00', date: '2025-08-13' },
      ];
      const filtered = mockData.filter(batch => batch.operator === operatorName);
      return Promise.resolve({ data: filtered });
    });

    const store = useBatchStore();

    await store.loadBatchesByOperatorName('Ram');
    expect(store.batches.length).toEqual(0);
    expect(store.error).toBeNull();

    await store.loadBatchesByOperatorName('Sam');
    expect(store.batches.length).toEqual(2);
    expect(store.error).toBeNull();
  });

})
