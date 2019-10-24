import Vue from "vue";
import Router from "vue-router";
import Home from "./views/Home.vue";
import Chat from "./views/Chat.vue";

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: "/",
      name: "home",
      component: Home
    },
    {
      path: "/chat",
      name: "chat",
      component: Chat
    }
    // {
    //   path: "/bite",
    //   name: "Bite",
    // // Autre méthode c'est d'importer comme ça
    // // Génère un [hash].js différent au build
    // // qui est lazy-loadé quand la routé est visité
    //   component: () =>
    //     import(/* webpackChunkName: "about" */ "./views/Bite.vue")
    // }
  ]
});
