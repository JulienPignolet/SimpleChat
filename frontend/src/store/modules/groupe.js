import * as types from "../types";
import { make } from "vuex-pathify";
import { Alerte } from "../../models/Alerte";
import axios from "axios";
import * as constants from "../../constants/constants";
import Router from "../../router/router"
import Vue from 'vue';

const state = () => ({
  groupe: {},
  groupeList: [],
  allGroupeList: [],
  groupeName: "",
  groupeCommunList: [],
  groupeMembers: [],
  groupeFriends: [],
  groupeBlockUsers: [],
});

const getters = {
  ...make.getters(state),
}


const mutations = {
  ...make.mutations(state),
  ADD_TO_GROUPE_COMMUN(state, payload) {
    Vue.set(state.groupeCommunList, payload.friendId, payload.groupesCommuns)
  }
}

const actions = {
  ...make.actions(state),

  async [types.createGroupe]({ state, dispatch, rootState }) {
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    if (rootState.user.selectedUserList && rootState.user.selectedUserList.length > 0) {
      let request = { "adminGroupeId": rootState.user.user.id, "groupeName": state.groupeName, "isPrivateChat": 0, "members": rootState.user.selectedUserList };
      axios
        .post(constants.API_URL + 'api/groupe/add/groupe-and-members', request).
        then(function (response) {
          dispatch((`interfaceControl/${types.setGroupDialog}`), false, { root: true })
          dispatch((`groupe/${types.getGroupes}`), null, { root: true })
          dispatch((`alerte/${types.setAlerte}`), new Alerte('success', response.data), { root: true })
        })
    } else {
      let request = { "groupe": state.groupeName, "isPrivateChat": 0, "userId": rootState.user.user.id };
      axios
        .post(constants.API_URL + 'api/groupe/add/groupe', request).
        then(function (response) {
          dispatch((`interfaceControl/${types.setGroupDialog}`), false, { root: true })
          dispatch((`groupe/${types.getGroupes}`), null, { root: true })
          dispatch((`alerte/${types.setAlerte}`), new Alerte('success', response.data), { root: true })
        })
    }
    dispatch('user/setSelectedUserList', [], { root: true })
    dispatch('groupe/setGroupeName', "", { root: true })

  },
  async [types.createGroupeWithFriend]({ dispatch, rootState }, friend) {
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    let request = { "adminGroupeId": rootState.user.user.id, "groupeName": friend.username, "isPrivateChat": 0, "members": [friend] };
    axios
      .post(constants.API_URL + 'api/groupe/add/groupe-and-members', request).
      then(function (response) {
        dispatch((`groupe/${types.getGroupes}`), null, { root: true })
        dispatch((`alerte/${types.setAlerte}`), new Alerte('success', response.data), { root: true })
      })
  },
  async [types.getGroupes]({ dispatch, rootState }) {
    if (rootState.user.user.id !== "undefined") {
      axios.defaults.headers.get['user_key'] = rootState.user.user.token;
      axios.get(`${constants.API_URL}api/groupe/find/groups/user/${rootState.user.user.id}`)
        .then(function (response) {
          dispatch("groupe/setGroupeList", response.data, { root: true })
        })
    }
  },
  async [types.getAllGroupes]({ dispatch, rootState }) {
    if (rootState.user.user.id !== "undefined") {
      axios.defaults.headers.get['user_key'] = rootState.user.user.token;
      axios.get(`${constants.API_URL}api/groupe/findAll/groupe`)
        .then(function (response) {
          dispatch("groupe/setAllGroupeList", response.data, { root: true })
        })
    }
  },
  async [types.getGroupesCommun]({ commit, rootState }, friendId) {
    if (rootState.user.user.id !== "undefined") {
      axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
      axios.defaults.headers.post['user_key'] = rootState.user.user.token;
      axios.post(`${constants.API_URL}${rootState.user.user.id}/findCommun`, friendId)
        .then(function (response) {
          commit('ADD_TO_GROUPE_COMMUN', { friendId: friendId, groupesCommuns: response.data })
        })
    }
  },
  async [types.chooseGroup]({ state, dispatch, rootState }) {
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(`${constants.API_URL}api/message/add/${state.groupe.id}/${rootState.user.user.id}/`)
    dispatch(`groupe/${types.getGroupeMembers}`, null, { root: true })
    dispatch(`groupe/${types.getGroupeFriends}`, null, { root: true })
    dispatch(`groupe/${types.getGroupeBlockUsers}`, null, { root: true })
    dispatch((`chat/${types.setMessageList}`), [], { root: true })
    dispatch((`chat/${types.getSavedMessages}`), null, { root: true })
  },
  //Evitez duplication, mais flemme
  async [types.chooseGroupAdmin]({ dispatch, rootState }, group) {
    Router.push(`/admin/group/${group.id}`);
    dispatch(types.setGroupe, group);
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(`${constants.API_URL}api/message/add/${group.id}/${rootState.user.user.id}/`)
    dispatch(`groupe/${types.getGroupeMembers}`, null, { root: true })
    dispatch('chat/setMessageList', [], { root: true })
    dispatch((`chat/${types.getSavedMessages}`), null, { root: true })
  },
  async [types.getGroupeMembers]({ dispatch, rootState }) {
    if (rootState.groupe.groupe.id !== undefined) {
      axios.defaults.headers.get['user_key'] = rootState.user.user.token;
      axios.get(`${constants.API_URL}api/groupe/find/Members/groupe/${rootState.groupe.groupe.id}`)
        .then(function (response) {
          dispatch("groupe/setGroupeMembers", response.data, { root: true });
        })
    }
  },
  async [types.getGroupeFriends]({ dispatch, rootState }) {
    if (rootState.groupe.groupe.id !== undefined) {
      axios.defaults.headers.get['user_key'] = rootState.user.user.token;
      axios.get(`${constants.API_URL}api/groupe/find/Members/groupe/amis/${rootState.groupe.groupe.id}/${rootState.user.user.id}`)
        .then(function (response) {
          dispatch("groupe/setGroupeFriends", response.data, { root: true });
        })
    }
  },
  async [types.getGroupeBlockUsers]({ dispatch, rootState }) {
    if (rootState.groupe.groupe.id !== undefined) {
      axios.defaults.headers.get['user_key'] = rootState.user.user.token;
      axios.get(`${constants.API_URL}api/groupe/find/Members/groupe/bloque/${rootState.groupe.groupe.id}/${rootState.user.user.id}`)
        .then(function (response) {
          dispatch("groupe/setGroupeBlockUsers", response.data, { root: true });
        })
    }
  }
};


export const groupe = {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
