import * as types from "../types";
import { make } from "vuex-pathify";
import { Alerte } from "../../models/Alerte";
import axios from "axios";
import * as constants from "../../constants/constants";
import Router from "../../router"

const state = () => ({
  groupe:{},
  groupeList: []
});

const mutations = make.mutations(state);

const actions = {
  ...make.actions(state),
  async [types.createGroupe]({ dispatch, rootState }, group) {
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
    dispatch(types.getGroupes)
  },
  async [types.getGroupes]({state, rootState}){
    axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
    axios.get(`${constants.API_URL}api/groupe/find/groups/user/${rootState.user.user.id}`)
    .then(function (response) {
      state.groupeList = response.data
    })
  },
  async [types.chooseGroup]({dispatch, rootState}, group){
    Router.push(`/chat/group/${group.id}`);
    dispatch(types.setGroupe, group);
    dispatch((`chat/${types.getMessages}`), null, { root: true })
    console.log(rootState.groupe.groupe.id)
  }
};

export const groupe = {
  namespaced: true,
  state,
  mutations,
  actions
};
