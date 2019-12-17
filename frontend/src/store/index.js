import Vue from 'vue'
import Vuex from 'vuex'
import pathify from '../plugins/pathify';

Vue.use(Vuex)

const store = new Vuex.Store({
  plugins: [pathify.plugin]
})

export default store
window.store = store