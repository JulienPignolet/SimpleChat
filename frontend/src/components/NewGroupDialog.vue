<template>
  <v-menu v-model="menu" :close-on-content-click="false" :nudge-width="500" offset-x>
    <template v-slot:activator="{ on }">
      <v-btn v-on="on" text icon>
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
    <v-card>
      <v-card-title>{{ $t('dialog.new_group.select_users') }}</v-card-title>
      <v-container>
        <v-autocomplete
          v-model="selectedUserList"
          :placeholder="$t('general.type_username')"
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
        />

        <v-text-field :label="$t('dialog.new_group.name')" v-model="groupeName" outlined />
        <v-card-actions>
          <v-spacer />
          <v-btn text @click="menu = false">{{ $t('general.cancel') }}</v-btn>
          <v-btn text @click="createGroupe()">{{ $t('dialog.new_group.create_group') }}</v-btn>
        </v-card-actions>
      </v-container>
    </v-card>
  </v-menu>
</template>

<script>
import { sync, call, get } from "vuex-pathify";
import * as types from "@/store/types.js";
export default {
  data() {
    return {
      groupName: "",
      menu: false
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

