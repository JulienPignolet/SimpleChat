import * as types from "../types";
import { make } from "vuex-pathify";
import axios from "axios";
import * as constants from "../../constants/constants";

const state = () => ({

});

const mutations = make.mutations(state);

const actions = {
  ...make.actions("register"),
  async [types.register]({ state }, registeringUser) {
    console.log(state);
    let request = { "username": registeringUser.username,"password": registeringUser.password,"passwordConfirm": registeringUser.passwordConfirm };
    axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
    axios
    .post(constants.API_URL + 'registration', request)
    .then(response => {
      console.log(response.data);
    })
    .catch(error => {
      console.log(error.response.data);
    });
  }
};

export const register = {
  namespaced: true,
  state,
  mutations,
  actions
};
