import * as types from "../types";
import { User } from "../../models/User";
import { Alerte } from "../../models/Alerte";
import { make } from "vuex-pathify";

const state = () => ({
  user: {}
});

const mutations = make.mutations(state);

const actions = {
  ...make.actions('user'),
  async [types.connexion]({ state, dispatch }, user) {
      dispatch(types.setUser, new User(user.username, user.password))
      dispatch((`alerte/${types.setAlerte}`), new Alerte('success', `L'utilisateur ${state.user.username} est bien connecté`), { root: true })
  },
  async [types.deconnexion]({ state, dispatch }) {
      dispatch((`alerte/${types.setAlerte}`), new Alerte('error', `L'utilisateur ${state.user.username} est bien déconnecté`), { root: true })
}
};

export const loginForm = {
  namespaced: true,
  state,
  mutations,
  actions
};
