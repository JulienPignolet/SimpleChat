import * as types from "../types";
import { make } from "vuex-pathify";
import { Message } from "../../models/Message";

const state = () => ({
  message: {},
  messageList: [
    { header: 'Aujourd\'hui' },
    {
      pseudonyme: 'Etudiant #1',
      message: "Test",
    },
    { divider: true, inset: true },
    {
      pseudonyme: 'Etudiant #2',
      message: "Test",
    },
    { divider: true, inset: true },
    {
      pseudonyme: 'Etudiant #3',
      message: "Test",
    },
    { divider: true, inset: true },
    {
      pseudonyme: 'Etudiant #4',
      message: "Test",
    },
    { divider: true, inset: true },
    {
      pseudonyme: 'Etudiant #5',
      message: "Test",
    },
  ],
});

const mutations = make.mutations(state);

const actions = {
  ...make.actions('message'),
  async [types.sendMessage]({state, dispatch, rootState}, message) {
      dispatch(types.setMessage, new Message(rootState.loginForm.user.username, message))
      state.messageList.push(state.message)
  }
};

export const chat = {
  namespaced: true,
  state,
  mutations,
  actions
};
