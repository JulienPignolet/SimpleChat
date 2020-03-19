<template>
  <v-app-bar app color="primary" :clipped-left="$vuetify.breakpoint.lgAndUp">
    <img src="../../public/images/logo.svg" alt="logo simple chat" class="logo" height="40" />
    <v-spacer />
    <v-tooltip bottom v-if="userIsSuperAdmin()">
      <template v-slot:activator="{ on }">
        <v-switch
          v-model="adminMode"
          dark
          color="white"
          inset
          hide-details
          on-icon="account"
          v-on="on"
        ></v-switch>
      </template>
      <span>{{ $t('app_bar.admin') }}</span>
    </v-tooltip>

    <locales-menu />
    <v-btn v-if="userIsConnected()" icon @click="deconnexion()">
      <v-icon large color="white">mdi-power</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script>
import { get, call } from "vuex-pathify";
import * as types from "@/store/types.js";
import { groupe } from "@/store/modules/groupe";
import { chat } from "@/store/modules/chat";
import RegisterStoreModule from "@/mixins/RegisterStoreModule";
import { sondage } from "../store/modules/sondage";
import LocalesMenu from "./LocalesMenu";
import { file } from "@/store/modules/file";
import Router from "../router/router";
import { interfaceControl } from "@/store/modules/interfaceControl";
import { user } from "@/store/modules/user";

export default {
  data() {
    return {
      currentPath: "a",
      adminMode: false,
    };
  },
  components: { LocalesMenu },
  mixins: [RegisterStoreModule],
  beforeCreate() {},
  created() {
    this.registerStoreModule("groupe", groupe);
    this.registerStoreModule("chat", chat);
    this.registerStoreModule("sondage", sondage);
    this.registerStoreModule("file", file);
    this.registerStoreModule("interfaceControl", interfaceControl);
    this.registerStoreModule("user", user);
    this.putainvivementlafindelannee().then(() => {
      if (this.currentPath.path.includes("admin")) {
        this.adminMode = true;
      } else {
        this.adminMode = false;
      }
    });
  },
  watch: {
    // Elue fonction la plus dégeulasse de 2020, bravo
    adminMode: function() {
      this.putainvivementlafindelannee().then(() => {
        if (this.adminMode === true) {
          if (this.currentPath.path.includes("group")) {
            Router.push({
              path: `/admin/group/${this.currentPath.params.groupId}`
            });
          } else if (this.currentPath.path.includes("friends")) {
            Router.push({
              path: `/admin/users`
            });
          } else {
            Router.push(`/admin`);
          }
        } else if (this.adminMode === false) {
          if (this.currentPath.path.includes("group")) {
            Router.push({
              path: `/chat/group/${this.currentPath.params.groupId}`
            });
          } else if (this.currentPath.path.includes("users")) {
            Router.push({
              path: `/chat/friends`
            });
          } else {
            Router.push(`/chat`);
          }
        }
      });
    }
  },
  computed: {
    user: get("user/user")
  },
  methods: {
    // Y a pas mieux que ça Dany stp, toi qui gère en promise ?
    putainvivementlafindelannee() {
      return new Promise(resolve => {
        this.currentPath = Router.history.current;
        resolve();
      });
    },
    deconnexion: call(`user/${types.deconnexion}`),
    getGroupes: call(`groupe/${types.getGroupes}`),
    getMessages: call(`chat/${types.getLiveMessages}`),
    getGroupeMembers: call(`groupe/${types.getGroupeMembers}`),
    userIsConnected: function() {
      return (
        this.$store.state.user.user.id != "undefined" &&
        this.$store.state.user.user.isStillConnected()
      );
    },
    userIsSuperAdmin: function() {
      if (this.userIsConnected()) {
        if (this.user.roles != null) {
          if (this.user.roles.some(e => e.name === "ROLE_SUPER_ADMIN")) {
            return true;
          }
        }
      }
    },
    activeAdminMode: function() {}
  },
  mounted: function() {
    window.setInterval(() => {
      this.getGroupes();
      this.getMessages();
      this.getGroupeMembers();
    }, 5000);
  }
};
</script>

