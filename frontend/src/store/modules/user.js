import * as types from "../types";
import { User } from "../../models/User";
import { Alerte } from "../../models/Alerte";
import { make } from "vuex-pathify";
import axios from "axios";
import * as constants from "../../constants/constants";
import Router from "../../router/router"
import { i18n } from '../../plugins/i18n';

const state = () => ({
  user: new User(localStorage.username, localStorage.token, localStorage.id),
  userList: [],
  friendList: [],
  selectedUserList: [],
})


const getters = {
  ...make.getters(state),
  userList: state => {
    return state.userList.filter(user => user.id != state.user.id)
  }
}

const mutations = {
    ...make.mutations(state)
}

const actions = {
  ...make.actions(state),
  async [types.connexion]({ state, dispatch }, user) {
    let request = { "username": user.username,"password": user.password };
    axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
    axios
    .post(constants.API_URL + 'authentication', request)
    .then(response => {
      dispatch(types.setUser, new User(user.username, response.data.user_key, response.data.user_id));
      dispatch((`alerte/${types.setAlerte}`), new Alerte('success', i18n.t('store.user.connected', { username: state.user.username })), { root: true });
      Router.push('/chat');
    })
    .catch(() => {
      dispatch((`alerte/${types.setAlerte}`), new Alerte('error', i18n.t('store.user.wrong_credentials')), { root: true })
    });
  },

    async [types.deconnexion]({ state, dispatch}) {
        dispatch((`alerte/${types.setAlerte}`), new Alerte('error', i18n.t('store.user.disconnected', { username: state.user.username })), { root: true });
        dispatch(types.setUser, new User());
        Router.push('/login');
    },

  // Inscription
  async [types.register]({ state, dispatch }, registeringUser) {
    console.log(state);
    let request = { "username": registeringUser.username,"password": registeringUser.password,"passwordConfirm": registeringUser.passwordConfirm };
    axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
    axios
    .post(constants.API_URL + 'registration', request)
    .then(() => {
      Router.push('/login');
      dispatch((`alerte/${types.setAlerte}`), new Alerte('success', i18n.t('store.user.registered')), { root: true })
    })
    .catch(error => {
      let errorMessage = error.response.data.errorMessage || i18n.t('error.has_occurred');
      dispatch((`alerte/${types.setAlerte}`), new Alerte('error', errorMessage), { root: true })
    });
  },

  // Récupération liste utilisateur
  async [types.getUsers]({dispatch, rootState}){
    axios.defaults.headers.get['user_key'] = rootState.user.user.token;
    axios.get(`${constants.API_URL}api/buddy/findAll/user`)
    .then(function (response) {
      dispatch("user/setUserList", response.data, {root: true})
    })
  },

  async [types.getUserFriends]({dispatch, rootState}){
    axios.defaults.headers.get['user_key'] = rootState.user.user.token;
    axios.get(`${constants.API_URL}api/buddy/${rootState.user.user.id}`)
    .then(function (response) {
      dispatch("user/setFriendList", response.data, {root : true})
    })
    Router.push('/chat/friends')
  },

  async [types.deleteFriend]({dispatch, rootState}, friendId){
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(`${constants.API_URL}api/buddy/${rootState.user.user.id}/remove`, friendId)
    .then(function () {
      dispatch("user/getUserFriends", null, {root : true});
    })
  },

  async [types.addFriend]({dispatch, rootState}, friendId){
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(`${constants.API_URL}api/buddy/${rootState.user.user.id}/add`, friendId)
    .then(function () {
      dispatch("user/getUserFriends", null, {root : true})
    })
  },

  async [types.blockUser]({dispatch, rootState}, userId){
    const request = {
      userId: rootState.user.user.id,
      blockId: userId
    };

    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(`${constants.API_URL}api/blockList/add`, request)
      .then(function () {
        dispatch(`groupe/${types.getGroupeBlockUsers}`, null, {root: true})
        console.log('user bloqué');
      })
  },

  async [types.unblockUser]({dispatch, rootState}, userId){
    const request = {
      userId: rootState.user.user.id,
      blockId: userId
    };

    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(`${constants.API_URL}api/blockList/remove`, request)
      .then(function () {
        dispatch(`groupe/${types.getGroupeBlockUsers}`, null, {root: true})
        console.log('user débloqué');
      })
  },
}

export const user = {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}
