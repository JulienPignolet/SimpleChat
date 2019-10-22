import Vue from 'vue';
// import vuetify from './plugins/vuetify';
import VueRouter from 'vue-router';

// load compenents
import App from './App';
import HelloWorld from './components/HelloWorld.vue';
import PageTest from './components/PageTest.vue';

Vue.use(VueRouter)
Vue.config.productionTip = false

const router = new VueRouter({
  mode: 'history',
  routes : [
    {path : "/", component : HelloWorld},
    {path : "/test", component : PageTest},
  ],
});

new Vue({
  el: '#app',
  router,
  render: h => h(App)
}).$mount('#app')
