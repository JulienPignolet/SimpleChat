import { make } from "vuex-pathify";

const state = () => ({
  groupDialog: false,
});

const mutations = make.mutations(state);

const actions = {
  ...make.actions("GroupDialog"),
};

export const interfaceControl = {
  namespaced: true,
  state,
  mutations,
  actions
};
