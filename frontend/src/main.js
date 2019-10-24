import Vue from 'vue';
import vuetify from './plugins/vuetify';
import VueRouter from 'vue-router';

// load compenents
import App from './App';
import HelloWorld from './components/HelloWorld.vue';
import PageTest from './components/PageTest.vue';
import LoginPage from './components/Login.vue';

Vue.use(VueRouter)
Vue.config.productionTip = false

const router = new VueRouter({
  mode: 'history',
  routes : [
    {path : "/", component : HelloWorld},
    {path : "/test", component : PageTest},
    {path : "/login", component : LoginPage},
  ],
});

new Vue({
  vuetify,
  router,
  render: h => h(App)
}).$mount('#app')
