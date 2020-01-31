import * as types from "../types";
import { make } from "vuex-pathify";
import { Message } from "../../models/Message";
import axios from "axios";
import * as constants from "../../constants/constants";

const state = () => ({
  message: {},
  messageList: [],
});

const mutations = make.mutations(state);

const actions = {
  ...make.actions(state),
  async [types.sendMessage]({ dispatch, rootState }, message) {
    dispatch(types.setMessage, new Message(rootState.user.user.username, message)
    );
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(constants.API_URL+"api/message/send", {
      "groupId": rootState.groupe.groupe.id,
      "contenu": message,
      "userId": rootState.user.user.id
    }).then(function (){
    dispatch((`chat/${types.getLiveMessages}`), null, { root: true })
    })
  },
  async [types.getLiveMessages]({ state, rootState}){
    axios.defaults.headers.get['user_key'] = rootState.user.user.token;
    axios.get(`${constants.API_URL}api/message/live/${rootState.groupe.groupe.id}/${rootState.user.user.id}/`)
    .then(function (response) {
      console.log(response)
      response.data.buffer.forEach(message => {
        state.messageList.push({"pseudonyme": message.user_name, "message": message.message})
      })
    })
  },
  async [types.getSavedMessages]({ state, rootState}){
    axios.defaults.headers.get['user_key'] = rootState.user.user.token;
    axios.get(`${constants.API_URL}api/message/saved/${rootState.groupe.groupe.id}/${rootState.user.user.id}/`)
    .then(function (response) {
      response.data.buffer.forEach(message => {
        state.messageList.push({"pseudonyme": message.user_name, "message": message.message})
      })
    })
  }
};

export const chat = {
  namespaced: true,
  state,
  mutations,
  actions
};
