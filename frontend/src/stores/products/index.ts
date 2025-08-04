import type { Module } from "vuex/types/index.js";
import type { ProductsState } from "./state";
import { mutations } from "./mutations";
import { actions } from "./actions";
import { state } from "./state";
import { getters } from "./getters";
import type { RootState } from "..";

export const productStore: Module<ProductsState, RootState> = {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
}
