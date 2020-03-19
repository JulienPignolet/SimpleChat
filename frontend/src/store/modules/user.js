import * as types from "../types";
import { User } from "../../models/User";
import { Alerte } from "../../models/Alerte";
import { make } from "vuex-pathify";
import axios from "axios";
import * as constants from "../../constants/constants";
import Router from "../../router/router"
import { i18n } from '../../plugins/i18n';

const state = () => ({
  user: new User(localStorage.username, localStorage.token, localStorage.id, localStorage.roles),
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
  ...make.mutations(state),
  SET_ROLE(state, roles){
    state.user.roles = roles
  },
  SET_ACTIVE(state, user){
    state.userList.find(x => x.id === user.id).active = user.active
  },
}

const actions = {
  ...make.actions(state),
  async [types.connexion]({ state, dispatch }, user) {
    let request = { "username": user.username, "password": user.password };
    axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
    axios
      .post(constants.API_URL + 'authentication', request)
      .then(response => {
        dispatch(types.setUser, new User(user.username, response.data.user_key, response.data.user_id));
        dispatch(types.getRole)
        dispatch((`alerte/${types.setAlerte}`), new Alerte('success', i18n.t('store.user.connected', { username: state.user.username })), { root: true });
        Router.push('/chat');
      })
      .catch(() => {
        dispatch((`alerte/${types.setAlerte}`), new Alerte('error', i18n.t('store.user.wrong_credentials')), { root: true })
      });
  },

  async [types.deconnexion]({ state, dispatch }) {
    localStorage.clear()
    dispatch((`alerte/${types.setAlerte}`), new Alerte('error', i18n.t('store.user.disconnected', { username: state.user.username })), { root: true });
    dispatch(types.setUser, new User());
    Router.push('/login');
  },

  // Inscription
  async [types.register]({ dispatch }, registeringUser) {
    let request = { "username": registeringUser.username, "password": registeringUser.password, "passwordConfirm": registeringUser.passwordConfirm };
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
  async [types.getUsers]({ dispatch, rootState }) {
    axios.defaults.headers.get['user_key'] = rootState.user.user.token;
    axios.get(`${constants.API_URL}api/buddy/findAll/user`)
      .then(function (response) {
        dispatch("user/setUserList", response.data, { root: true })
      })
  },

  // Récupération rôle
  async [types.getRole]({commit,rootState }) {
    axios.defaults.headers.get['user_key'] = rootState.user.user.token;
    axios.get(`${constants.API_URL}getRole/${rootState.user.user.id}`)
      .then(function (response) {
        commit('SET_ROLE', response.data)
        localStorage.setItem('roles', JSON.stringify(response.data))
      })
  },

  async [types.getUserFriends]({ dispatch, rootState }) {
    Router.push('/chat/friends')
    axios.defaults.headers.get['user_key'] = rootState.user.user.token;
    return axios.get(`${constants.API_URL}api/buddy/${rootState.user.user.id}`)
      .then(function (response) {
        // Temporairement pour enlever les amis dupliqués 
        const amis = Array.from(new Set(response.data.map(a => a.id)))
          .map(id => {
            return response.data.find(a => a.id === id)
          })
        dispatch("user/setFriendList", amis, { root: true })
      })

  },

  async [types.deleteFriend]({ dispatch, rootState }, friendId) {
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
    axios.post(`${constants.API_URL}api/buddy/${rootState.user.user.id}/remove`, friendId)
      .then(function () {
        dispatch("user/getUserFriends", null, { root: true })
      })
  },

  async [types.addFriend]({ dispatch, rootState }, friendId) {
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
    axios.post(`${constants.API_URL}api/buddy/${rootState.user.user.id}/add`, friendId)
      .then(function () {
        dispatch("user/getUserFriends", null, { root: true })
        dispatch(`groupe/${types.getGroupesCommun}`, friendId, { root: true })
      })
  },

  async [types.blockUser]({ dispatch, rootState }, userId) {
    const request = {
      userId: rootState.user.user.id,
      blockId: userId
    };

    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(`${constants.API_URL}api/blockList/add`, request)
      .then(function () {
        dispatch(`groupe/${types.getGroupeBlockUsers}`, null, { root: true })
        // Mettre une alerte utilisateur
        // console.log('user bloqué');
      })
  },

  async [types.unblockUser]({ dispatch, rootState }, userId) {
    const request = {
      userId: rootState.user.user.id,
      blockId: userId
    };

    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(`${constants.API_URL}api/blockList/remove`, request)
      .then(function () {
        dispatch(`groupe/${types.getGroupeBlockUsers}`, null, { root: true })
        // Mettre une alerte utilisateur 
        // console.log('user débloqué');
      })
  },
  async [types.deleteUser]({  commit, rootState }, userId) {
    if (rootState.user.user.id !== "undefined") {
      axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
      axios.defaults.headers.post['user_key'] = rootState.user.user.token;
      axios.post(`${constants.API_URL}user/manage/${userId}`, false)
        .then(function () {
          commit('SET_ACTIVE', {id : userId, active: false})
        })
    }
  },
  async [types.restoreUser]({  commit, rootState }, userId) {
    if (rootState.user.user.id !== "undefined") {
      axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
      axios.defaults.headers.post['user_key'] = rootState.user.user.token;
      axios.post(`${constants.API_URL}user/manage/${userId}`, true)
        .then(function () {
          commit('SET_ACTIVE', {id : userId, active: true})
        })
    }
  },
}

export const user = {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}
