<template>
  <v-dialog v-model="dialog" max-width="500">
    <v-card>
      <v-card-title>Séléctionne des utilisateurs</v-card-title>
      <v-container>
        <v-autocomplete
          v-model="selectedUserList"
          placeholder="Entre le nom d'un utilisateur"
          :items="userList"
          item-text="username"
          item-value="id"
          chips
          deletable-chips
          multiple
          single-line
          rounded
          filled
          @click="getUsers()"
        ></v-autocomplete>

        <v-text-field label="Nom du groupe" v-model="groupeName" outlined></v-text-field>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="dialog = false">Annuler</v-btn>
          <v-btn text @click="createGroupe()">Créer un groupe</v-btn>
        </v-card-actions>
      </v-container>
    </v-card>
  </v-dialog>
</template>

<script>
import { sync, call, get } from "vuex-pathify";
import * as types from "@/store/types.js";
export default {
  data() {
    return {
      groupName: ""
    };
  },
  created: function() {
    //  this.getFriends();
  },
  computed: {
    selectedUserList: sync("user/selectedUserList"),
    groupeName: sync("groupe/groupeName"),
    dialog: sync("interfaceControl/groupDialog"),
    userList: get("user/userList")
  },
  methods: {
    getUsers: call(`user/${types.getUsers}`),
    createGroupe: call(`groupe/${types.createGroupe}`)
  }
};
</script>

<style lang="css">
</style>

