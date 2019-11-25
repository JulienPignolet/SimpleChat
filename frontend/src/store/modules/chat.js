import * as types from "../types";
import { make } from "vuex-pathify";
import { Message } from "../../models/Message";
import axios from "axios";
import * as constants from "../../constants/constants";

const state = () => ({
  message: {},
  messageList: []
});

const mutations = make.mutations(state);

const actions = {
  ...make.actions("message"),
  async [types.sendMessage]({ state, dispatch, rootState }, message) {
    dispatch(types.setMessage, new Message(rootState.user.user.username, message)
    );

    axios.post(constants.API_URL+"api/message/", {
      "group_id": rootState.groupe.groupe.id,
      "message": message,
      "user_id": rootState.user.user.id
    })
    .then(function (response) {
      console.log(response)
    })
    state.messageList.push(state.message);
  },
  async [types.getMessages]({ rootState}){
    axios.get(`${constants.API_URL}/message/${rootState.groupe.groupe.id}/${rootState.user.user.id}`, {
      userId: 5,
      groupId: rootState.groupe.groupe.id
    })
    .then(function (response) {
      console.log(response)
    })
  }
};

export const chat = {
  namespaced: true,
  state,
  mutations,
  actions
};
