import * as types from "../types";
import { make } from "vuex-pathify";
import { Message } from "../../models/Message";
import axios from "axios";
import * as constants from "../../constants/constants";

const state = () => ({
  message: {},
  messageList: [],
});

const mutations = {
  ...make.mutations(state),
  CLEAR_MESSAGE_LIST(state) {
    state.messageList = []
  },
  ADD_TO_MESSAGE_LIST(state, message) {
    state.messageList.push(new Message(message.userName, message.contenu, message.type, message.active, message.id))
  },
  SET_ACTIVE(state, message){
    state.messageList.find(x => x.id === message.id).active = message.active
  },
}
const actions = {
  ...make.actions(state),
  async [types.sendMessage]({ dispatch, rootState }, messageParams) {
    if (messageParams.type === undefined) {
      messageParams.type = "message"
    }
    dispatch(types.setMessage, new Message(rootState.user.user.username, messageParams.message, messageParams.type)
    );
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(constants.API_URL + "api/message/send", {
      "groupId": rootState.groupe.groupe.id,
      "contenu": messageParams.message,
      "userId": rootState.user.user.id,
      "type": messageParams.type
    }).then(function () {
      dispatch((`chat/${types.getLiveMessages}`), null, { root: true })
    })
  },
  async [types.getLiveMessages]({ commit, rootState }) {
    if (rootState.user.user.id !== undefined && rootState.groupe.groupe.id !== undefined) {
      axios.defaults.headers.get['user_key'] = rootState.user.user.token;
      axios.get(`${constants.API_URL}api/message/live/${rootState.groupe.groupe.id}/${rootState.user.user.id}/`)
        .then(function (response) {
          response.data.buffer.forEach(message => {
             commit('ADD_TO_MESSAGE_LIST', message)
          })
        })
    }
  },
  async [types.getSavedActMessages]({ commit, rootState }) {
    commit('CLEAR_MESSAGE_LIST')
    axios.defaults.headers.get['user_key'] = rootState.user.user.token;
    axios.get(`${constants.API_URL}api/message/savedActifs/${rootState.groupe.groupe.id}/${rootState.user.user.id}/`)
      .then(function (response) {
        response.data.buffer.forEach(message => {
          commit('ADD_TO_MESSAGE_LIST', message)
        })

      })
  },
  async [types.getSavedMessages]({ commit, rootState }) {
    commit('CLEAR_MESSAGE_LIST')
    axios.defaults.headers.get['user_key'] = rootState.user.user.token;
    axios.get(`${constants.API_URL}api/message/saved/${rootState.groupe.groupe.id}/${rootState.user.user.id}/`)
      .then(function (response) {
        response.data.buffer.forEach(message => {
          commit('ADD_TO_MESSAGE_LIST', message)
        })
      })
  },
  async [types.deleteMessage]({  commit, rootState }, messageId) {
    if (rootState.user.user.id !== "undefined") {
      axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
      axios.defaults.headers.post['user_key'] = rootState.user.user.token;
      axios.post(`${constants.API_URL}api/message/message/manage/${messageId}`, false)
        .then(function () {
          commit('SET_ACTIVE', {id : messageId, active: false})
        })
    }
  },
  async [types.restoreMessage]({  commit, rootState }, messageId) {
    if (rootState.user.user.id !== "undefined") {
      axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
      axios.defaults.headers.post['user_key'] = rootState.user.user.token;
      axios.post(`${constants.API_URL}api/message/message/manage/${messageId}`, true)
        .then(function () {
          commit('SET_ACTIVE', {id : messageId, active: true})
        })
    }
  },
};


export const chat = {
  namespaced: true,
  state,
  mutations,
  actions
};
