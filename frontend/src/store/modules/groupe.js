import * as types from "../types";
import { make } from "vuex-pathify";
import { Alerte } from "../../models/Alerte";
import axios from "axios";
import * as constants from "../../constants/constants";

const state = () => ({
  message: {},
  groupeList: []
});

const mutations = make.mutations(state);

const actions = {
  ...make.actions("groupe"),
  async [types.createGroupe]({ dispatch, rootState }, group) {
    console.log(group)
    rootState.user.user.id = 5 //Attente JWTToken
    let request = { "groupe": group.groupName, "isPrivateChat": 0, "userId": rootState.user.user.id};
    axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
    axios
    .post(constants.API_URL + 'api/groupe/add/groupe', request).
    then(function(response){
      /* TODO : try catch */
      console.log(response)
      dispatch((`interfaceControl/${types.setGroupDialog}`), false, { root: true })
      dispatch((`alerte/${types.setAlerte}`), new Alerte('success', response.data), { root: true })
    })
  },
  async [types.getGroupes]({state, rootState}){
    rootState.user.user.id = 5 //Attente JWTToken
    axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
    axios.get(`${constants.API_URL}api/groupe/find/groups/user/${rootState.user.user.id}`)
    .then(function (response) {
      state.groupeList = response.data
    })
  }
};

export const groupe = {
  namespaced: true,
  state,
  mutations,
  actions
};
