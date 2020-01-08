import * as types from "../types";
import { make } from "vuex-pathify";
import { Alerte } from "../../models/Alerte";
import axios from "axios";
import * as constants from "../../constants/constants";
import Router from "../../router/router"

const state = () => ({
  groupe:{},
  groupeList: [],
  groupeName: "",
  groupeCommunList: []
});

const mutations = make.mutations(state);

const actions = {
  ...make.actions(state),
  
  async [types.createGroupe]({ state, dispatch, rootState }) {
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    if(rootState.user.selectedUserList && rootState.user.selectedUserList.length > 0){
      let request = { "adminGroupeId": rootState.user.user.id, "groupeName": state.groupeName, "isPrivateChat": 0, "members": rootState.user.selectedUserList}; 
      axios
      .post(constants.API_URL + 'api/groupe/add/groupe-and-members', request).
      then(function(response){
        dispatch((`interfaceControl/${types.setGroupDialog}`), false, { root: true })
        dispatch((`groupe/${types.getGroupes}`), null, { root: true})
        dispatch((`alerte/${types.setAlerte}`), new Alerte('success', response.data), { root: true })
      })
    }else {
      let request = { "groupe": state.groupeName, "isPrivateChat": 0, "userId": rootState.user.user.id};
      axios
      .post(constants.API_URL + 'api/groupe/add/groupe', request).
      then(function(response){
        dispatch((`interfaceControl/${types.setGroupDialog}`), false, { root: true })
        dispatch((`groupe/${types.getGroupes}`), null, { root: true})
        dispatch((`alerte/${types.setAlerte}`), new Alerte('success', response.data), { root: true })
      })
    }
    dispatch('user/setSelectedUserList', [], { root: true})
    dispatch('groupe/setGroupeName', "", {root: true})

  },
  async [types.getGroupes]({dispatch, rootState}){
    axios.defaults.headers.get['user_key'] = rootState.user.user.token;
    axios.get(`${constants.API_URL}api/groupe/find/groups/user/${rootState.user.user.id}`)
    .then(function (response) {
      dispatch("groupe/setGroupeList", response.data, {root: true})
    })
  },
  // Nul nul nul, voir avec front si jpeux avoir une requête déjà plutôt
  async [types.getGroupesCommun]({state, rootState, rootGetters}){
    console.log(rootGetters['user/friendList'])
    console.log(rootState.user.friendList)
    for(const friend of rootState.user.friendList){
      let userGroups = []
      let friendGroups = []
      axios.defaults.headers.get['user_key'] = rootState.user.user.token;
      await axios.get(`${constants.API_URL}api/groupe/find/groups/user/${rootState.user.user.id}`)
      .then(function (response) {
        userGroups = response.data
       
      })
      await axios.get(`${constants.API_URL}api/groupe/find/groups/user/${friend.id}`)
      .then(function (response) {
        friendGroups = response.data
      })
      state.groupeCommunList[friend.id] = userGroups.filter(userGroup => friendGroups.some(friendGroup => userGroup.id === friendGroup.id))
    }

    
  },
  async [types.chooseGroup]({dispatch, rootState}, group){
    Router.push(`/chat/group/${group.id}`);
    dispatch(types.setGroupe, group);
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(`${constants.API_URL}api/message/add/${group.id}/${rootState.user.user.id}/`)
    // .then(function (response) {
    //   console.log(response.data)
    //   response.data.buffer.forEach(message => {
    //     state.messageList.push({"pseudonyme": message.user_id, "message": message.message})
    //   })
    // })
    dispatch('chat/setMessageList', [], {root: true})
    dispatch((`chat/${types.getSavedMessages}`), null, { root: true })
  }
};


export const groupe = {
  namespaced: true,
  state,
  mutations,
  actions
};
