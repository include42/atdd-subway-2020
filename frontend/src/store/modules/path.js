import { SET_PATH } from '@/store/shared/mutationTypes'
import { SEARCH_PATH, SEARCH_PATH_WITH_LOGIN } from '@/store/shared/actionTypes'
import PathService from '@/api/modules/path'

const state = {
  pathResult: null
}

const getters = {
  pathResult(state) {
    return state.pathResult
  }
}

const mutations = {
  [SET_PATH](state, pathResult) {
    state.pathResult = pathResult
  }
}

const actions = {
  async [SEARCH_PATH]({ commit }, params) {
    return PathService.get(params).then(({ data }) => {
      commit('setPath', data)
    })
  },
  async [SEARCH_PATH_WITH_LOGIN]({ commit }, params) {
    return PathService.getWithLogin(params).then(({ data }) => {
      commit('setPath', data)
    })
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
