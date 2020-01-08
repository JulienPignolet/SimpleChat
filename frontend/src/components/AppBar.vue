<template>
  <v-app-bar app color="primary" :clipped-left="$vuetify.breakpoint.lgAndUp">
    <img src="../../public/images/logo.svg" alt="logo simple chat" class="logo" height="40" />
    <v-spacer></v-spacer>
    <v-btn v-if="userIsConnected()" icon @click="deconnexion()">
      <v-icon large color="white">mdi-power</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script>
import { call } from "vuex-pathify";
import * as types from "@/store/types.js";
import { groupe } from "@/store/modules/groupe";
import { chat } from "@/store/modules/chat"
import RegisterStoreModule from "@/mixins/RegisterStoreModule";
export default {
  mixins: [RegisterStoreModule],
  beforeCreate() {},
  created() {
    this.registerStoreModule("groupe", groupe);
    this.registerStoreModule("chat", chat);
    //this.getMessages();
  },
  methods: {
    deconnexion: call(`user/${types.deconnexion}`),
    getGroupes: call(`groupe/${types.getGroupes}`),
    getMessages: call(`chat/${types.getLiveMessages}`),
    userIsConnected: function() {
      return (
        this.$store.state.user != null &&
        this.$store.state.user.user.isStillConnected()
      );
    }
  },
  mounted: function() {
    window.setInterval(() => {
      this.getGroupes();
      this.getMessages();
    }, 5000);
  }
};
</script>

