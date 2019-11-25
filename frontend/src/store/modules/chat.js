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
  async [types.sendMessage]({ dispatch, rootState }, message) {
    dispatch(types.setMessage, new Message(rootState.user.user.username, message)
    );
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(constants.API_URL+"api/message/", {
      "group_id": rootState.groupe.groupe.id,
      "message": message,
      "user_id": rootState.user.user.id
    }).then(function (){
    dispatch((`chat/${types.getMessages}`), null, { root: true })
    })
  },
  async [types.getMessages]({ rootState}){
    axios.defaults.headers.get['user_key'] = rootState.user.user.token;
    axios.get(`${constants.API_URL}api/message/${rootState.groupe.groupe.id}/${rootState.user.user.id}/`)
    .then(function (response) {
      console.log(response.data)
      let messageList = JSON.parse(response.data)
      messageList.forEach(message => {
        state.messageList.push({"pseudonyme": message.user_id, "message": message.message})
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
