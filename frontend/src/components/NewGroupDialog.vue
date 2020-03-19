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
          :items="userList.filter(user => user.active)"
          item-text="username"
          item-value="id"
          chips
          deletable-chips
          multiple
          single-line
          filled
          @click="getUsers()"
        />

        <v-text-field :label="$t('dialog.new_group.name')" v-model="groupeName" outlined />
        <v-card-actions>
          <v-spacer />
          <v-btn text @click="closeMenuAndResetInput()">{{ $t('general.cancel') }}</v-btn>
          <v-btn text @click="createGroupeAndCloseMenu()">{{ $t('dialog.new_group.create_group') }}</v-btn>
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
      menu: false
    };
  },
  computed: {
    selectedUserList: sync("user/selectedUserList"),
    groupeName: sync("groupe/groupeName"),
    userList: get("user/userList")
  },
  watch: {
    menu(val) {
      !val && this.closeMenuAndResetInput();
    }
  },
  methods: {
    getUsers: call(`user/${types.getUsers}`),
    createGroupe: call(`groupe/${types.createGroupe}`),
    createGroupeAndCloseMenu() {
      this.createGroupe();
      this.menu = false;
    },
    closeMenuAndResetInput() {
      this.menu = false;
      this.selectedUserList = [];
      this.groupeName = "";
    }
  }
};
</script>

