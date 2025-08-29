import { actions } from '../../../../src/stores/products/actions'
import {
  getAllProducts,
  addNewProduct,
  deleteProductById,
} from '../../../../src/services/productService'
import type { ActionContext } from 'vuex'
import type { ProductsState } from '../../../../src/stores/products/state'
import type { RootState } from '../../../../src/stores'
import { Product } from '../../../../src/types/Product'

jest.mock('../../../../src/services/productService')

describe('Product Actions', () => {
  let commit: jest.Mock
  let dispatch: jest.Mock

  beforeEach(() => {
    commit = jest.fn()
    dispatch = jest.fn()
    jest.clearAllMocks()
  })

  afterEach(() => {
    jest.clearAllMocks()
  })

  it('load products correctly', async () => {
    const context = {
      commit,
      dispatch,
      state: {},
      getters: {},
      rootState: {},
      rootGetters: {},
    } as ActionContext<ProductsState, RootState>
    const products = [
      {
        product: 'NP-1',
        flavor: 'Vanilla',
        size: '10 g',
        minBatchTime: 100,
      },
      {
        product: 'NP-2',
        flavor: 'Strawberry',
        size: '20 g',
        minBatchTime: 110,
      },
    ]

    ;(getAllProducts as jest.Mock).mockResolvedValue({ data: products })

    const loadProducts = actions.loadProducts as (
      context: ActionContext<ProductsState, RootState>,
    ) => Promise<void>
    await loadProducts(context)
    expect(commit).toHaveBeenCalledWith('SET_LOADING', true)
    expect(commit).toHaveBeenCalledWith('SET_ERROR', null)
    expect(commit).toHaveBeenCalledWith('LOAD_PRODUCTS', products)
    expect(commit).toHaveBeenCalledWith('SET_LOADING', false)
  })

  it('handles errors when loading products', async () => {
    const errorMessage = 'Failed to fetch products'
    ;(getAllProducts as jest.Mock).mockRejectedValue(new Error(errorMessage))

    const context = {
      commit,
      dispatch,
      state: {},
      getters: {},
      rootState: {},
      rootGetters: {},
    } as ActionContext<ProductsState, RootState>

    const loadProducts = actions.loadProducts as (
      context: ActionContext<ProductsState, RootState>,
    ) => Promise<void>
    await loadProducts(context)

    expect(commit).toHaveBeenCalledWith('SET_LOADING', true)
    expect(commit).toHaveBeenCalledWith('SET_ERROR', errorMessage)
    expect(commit).toHaveBeenCalledWith('SET_LOADING', false)
  })

  it('adds a product successfully', async () => {
    const context = {
      commit,
      dispatch,
      state: {},
      getters: {},
      rootState: {},
      rootGetters: {},
    } as ActionContext<ProductsState, RootState>

    const newProduct = {
      product: 'NP-3',
      flavor: 'Chocolate',
      size: '30 g',
      minBatchTime: 120,
    }

    ;(addNewProduct as jest.Mock).mockResolvedValue({ data: newProduct })

    const addProductAction = actions.addProduct as (
      context: ActionContext<ProductsState, RootState>,
      product: Product,
    ) => Promise<void>

    await addProductAction(context, newProduct)

    expect(commit).toHaveBeenCalledWith('SET_LOADING', true)
    expect(commit).toHaveBeenCalledWith('SET_ERROR', null)
    expect(commit).toHaveBeenCalledWith('ADD_PRODUCT', newProduct)
    expect(commit).toHaveBeenCalledWith('SET_LOADING', false)
  })

  it('handles errors when adding a product', async () => {
    const errorMessage = 'Failed to add product'
    ;(addNewProduct as jest.Mock).mockRejectedValue(new Error(errorMessage))

    const newProduct = {
      product: '',
      flavor: 'Chocolate',
      size: '30 g',
      minBatchTime: 120,
    }
    const context = {
      commit,
      dispatch,
      state: {},
      getters: {},
      rootState: {},
      rootGetters: {},
    } as ActionContext<ProductsState, RootState>

    const addProductAction = actions.addProduct as (
      context: ActionContext<ProductsState, RootState>,
      product: Product,
    ) => Promise<void>

    await addProductAction(context, newProduct)

    expect(commit).toHaveBeenCalledWith('SET_LOADING', true)
    expect(commit).toHaveBeenCalledWith('SET_ERROR', errorMessage)
    expect(commit).toHaveBeenCalledWith('SET_LOADING', false)
  })

  it('deletes a product successfully', async () => {
    const context = {
      commit,
      dispatch,
      state: {
        products: [],
      },
      getters: {},
      rootState: {},
      rootGetters: {},
    } as ActionContext<ProductsState, RootState>

    // add a product first
    const newProduct = {
      product: 'NP-1',
      flavor: 'Chocolate',
      size: '30 g',
      minBatchTime: 120,
    }
    // ;(addNewProduct as jest.Mock).mockResolvedValue({ data: newProduct });
    const addProductAction = actions.addProduct as (
      context: ActionContext<ProductsState, RootState>,
      product: Product,
    ) => Promise<void>

    await addProductAction(context, newProduct)

    ;(deleteProductById as jest.Mock).mockResolvedValue({})
    const deleteProductAction = actions.deleteProduct as (
      context: ActionContext<ProductsState, RootState>,
      productId: string,
    ) => Promise<void>

    // expect(context.state.products.length).toBe(1);
    await deleteProductAction(context, newProduct.product)

    expect(commit).toHaveBeenCalledWith('SET_LOADING', true)
    expect(commit).toHaveBeenCalledWith('SET_ERROR', null)
    expect(commit).toHaveBeenCalledWith('DELETE_PRODUCT', 'NP-1')
    expect(commit).toHaveBeenCalledWith('SET_LOADING', false)
    expect(context.state.products.length).toBe(0)
  })
})
