import Vue from 'vue';
import App from './App';
import router from "./router/router";
import store from "@/store";
import vuetify from './plugins/vuetify';
import "roboto-fontface/css/roboto/roboto-fontface.css";
import "@mdi/font/css/materialdesignicons.css";
import { i18n } from './plugins/i18n';
import FlagIcon from 'vue-flag-icon';

Vue.use(FlagIcon);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  vuetify,
  i18n,
  render: h => h(App)
}).$mount("#app");
