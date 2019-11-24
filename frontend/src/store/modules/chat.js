import * as types from "../types";
import { make } from "vuex-pathify";
import { Message } from "../../models/Message";
import axios from "axios";
import * as constants from "../../constants/constants";

const state = () => ({
  message: {},
  messageList: [
    { header: "Aujourd'hui" },
    {
      pseudonyme: "Etudiant #1",
      message: "Test"
    },
    { divider: true, inset: true },
    {
      pseudonyme: "Etudiant #2",
      message: "Test"
    },
    { divider: true, inset: true },
    {
      pseudonyme: "Etudiant #3",
      message: "Test"
    },
    { divider: true, inset: true },
    {
      pseudonyme: "Etudiant #4",
      message: "Test"
    },
    { divider: true, inset: true },
    {
      pseudonyme: "Etudiant #5",
      message: "Test"
    }
  ]
});

const mutations = make.mutations(state);

const actions = {
  ...make.actions("message"),
  async [types.sendMessage]({ state, dispatch, rootState }, message) {
    dispatch(types.setMessage, new Message(rootState.user.user.username, message)
    );

    /*TODO : Recuperer vrai user*/
    axios.post(constants.API_URL+"/message", {
      user_id: 5,
      group_id: 2,
      message: message
    })
    .then(function (response) {
      console.log(response)
    })
    state.messageList.push(state.message);
  },
  async [types.getMessages]({ rootState}){
    axios.get(`${constants.API_URL}/message/${rootState.user}`, {
      user_id: 5,
      group_id: 2,
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
