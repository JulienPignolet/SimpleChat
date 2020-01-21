import * as types from "../types";
import { make } from "vuex-pathify";
import axios from "axios";
import * as constants from "../../constants/constants";

const state = () => ({
  sondage: {}
});

const mutations = make.mutations(state);

const actions = {
  ...make.actions(state),
  async [types.sendSondage]({ rootState }, sondage) {
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.post(constants.API_URL+"api/sondage/add", {
      "question": sondage.question,
      "dateFin": sondage.dateFin,
      "reponsesSondage": sondage.reponseSondages,
      "groupeId": rootState.groupe.groupe.id,
      "userId": rootState.user.user.id,
      "votesAnonymes": sondage.votesAnonymes
    }).then(function (){
      console.log('sondage envoyé');
      // TODO
    })
  }
};

export const sondage = {
  namespaced: true,
  state,
  mutations,
  actions
};
