<template>
  <v-dialog v-model="dialog" max-width="500">
    <v-card>
      <v-card-title>Séléctionne des utilisateurs</v-card-title>
      <v-container>
        <v-autocomplete
        
          placeholder="Entre le nom d'un utilisateur"
          :items="['Trevor Handsen', 'Alex Nelson']"
          chips
          multiple
          single-line
          rounded
          filled
        ></v-autocomplete>

        <v-text-field label="Nom du groupe" v-model="groupName" outlined></v-text-field>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="dialog = false">Annuler</v-btn>

          <v-btn text @click="createGroupe({groupName})">Créer un groupe</v-btn>
        </v-card-actions>
      </v-container>
    </v-card>
  </v-dialog>
</template>

<script>
import { sync, call } from "vuex-pathify";
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
    //friends: sync("groupe/friendList"),
    dialog: sync("interfaceControl/groupDialog")
  },
  methods: {
    getFriends: call(`user/${types.getFriends}`),
    createGroupe: call(`groupe/${types.createGroupe}`)
  }
};
</script>

<style lang="css">
</style>

