import { make } from 'vuex-pathify'

const state = () => ({
  alerte: {}
})

const mutations = make.mutations(state)

const actions = {
  ...make.actions('alerte')
}

export const alerte = {
  namespaced: true,
  state,
  mutations,
  actions
}