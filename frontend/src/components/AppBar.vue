<template>
  <v-app-bar color="primary">
    <!-- TODO : Activer ce bouton après connexion -->
    <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
    <img src="../../public/images/logo.svg" alt="logo simple chat" class="logo" height="40" />
    <v-spacer></v-spacer>
    <!-- TODO : Activer ce bouton après connexion/Désactiver après déconnexion -->
    <v-btn icon @click="deconnexion().then(() => {$router.push('/')})">
      <v-icon large>mdi-power</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script>
import { call } from "vuex-pathify";
import * as types from "@/store/types.js";

export default {
  methods: {
    deconnexion: call("loginForm/" + types.deconnexion)
  }
};
</script>

