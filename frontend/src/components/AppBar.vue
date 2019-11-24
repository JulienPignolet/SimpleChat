<template>
  <v-app-bar app color="primary">
    <!-- TODO : Activer ce bouton après connexion -->
    <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
    <img src="../../public/images/logo.svg" alt="logo simple chat" class="logo" height="40" />
    <v-spacer></v-spacer>


    <v-btn v-if="!userIsConnected()" icon @click="$router.push('/login')">
      <v-icon large color="white">mdi-login</v-icon>
    </v-btn>

    <v-btn v-if="userIsConnected()" icon @click="deconnexion()">
      <v-icon large color="white">mdi-power</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script>
import { call } from "vuex-pathify";
import * as types from "@/store/types.js";
import { user } from "@/store/modules/user";

export default {
  beforeCreate() {
    this.$store.registerModule("user", user);
  },

  methods: {
    deconnexion: call(`user/${types.deconnexion}`),

    userIsConnected: function() {
      return this.$store.state.user != null && this.$store.state.user.user.isStillConnected();
    }
  }
};
</script>

