<template>
  <v-navigation-drawer :clipped="$vuetify.breakpoint.lgAndUp" app>
    <v-list-item link>
      <v-list-item-icon>
        <v-icon>{{ "mdi-account-heart" }}</v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>Amis</v-list-item-title>
      </v-list-item-content>
    </v-list-item>

    <v-divider></v-divider>
    <v-list dense>
      <v-subheader>
        Discussions
        <v-spacer />
        <v-btn text icon @click="activateNewGroupDialog(true) ">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </v-subheader>
      <v-list-item-group>
      <v-list-item v-for="groupe in groupes" :key="groupe.id" @click="chooseGroup(groupe)">
        <v-list-item-content>
          <v-list-item-title>{{ groupe.name }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      </v-list-item-group>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
import { sync, call } from "vuex-pathify";
import * as types from "@/store/types.js";
import { groupe } from "@/store/modules/groupe";
import { interfaceControl } from "@/store/modules/interfaceControl";
export default {
  data() {
    return {};
  },
  beforeCreate() {
    this.$store.registerModule("groupe", groupe);
    this.$store.registerModule("interfaceControl", interfaceControl);
  },
  created: function() {
    this.getGroupes()
  },
  computed: {
    currentGroup: sync("groupe/groupe"),
    groupes: sync("groupe/groupeList")
  },
  methods: {
    activateNewGroupDialog: call(`interfaceControl/${types.setGroupDialog}`),
    getGroupes: call(`groupe/${types.getGroupes}`),
    createGroupe: call(`groupe/${types.createGroupe}`),
    chooseGroup:  call(`groupe/${types.chooseGroup}`)
  }
};
</script>

<style lang="css">
</style>