<template>
  <v-list style="border-left: 1px solid rgba(0,0,0,0.1);" dense>
    <div v-if="isAdmin">
      <v-subheader>{{ $t('user_group.actions') }}</v-subheader>

      <div class="buttons">
        <v-menu v-model="menu" :close-on-content-click="false" :nudge-width="500" offset-x>
          <template v-slot:activator="{ on }">
            <v-btn v-on="on" block color="primary" class="white--text">
              <v-icon left>mdi-account-plus</v-icon>
              {{ $t('user_group.add_member') }}
            </v-btn>
          </template>
          <v-card>
            <v-card-title>{{ $t('dialog.new_group.select_users') }}</v-card-title>
            <v-container>
              <v-autocomplete
                v-model="selectedUserList"
                :placeholder="$t('general.type_username')"
                :items="userList.filter((user) => !members.some((member) => user.id === member.id) && user.active)"
                item-text="username"
                item-value="id"
                chips
                deletable-chips
                multiple
                single-line
                filled
                @click="getUsers()"
              />
              <v-card-actions>
                <v-spacer />
                <v-btn text @click="closeMenuAndResetInput()">{{ $t('general.cancel') }}</v-btn>
                <v-btn
                  text
                  @click="createGroupeAndCloseMenu()"
                >{{ $t('dialog.new_group.add_user') }}</v-btn>
              </v-card-actions>
            </v-container>
          </v-card>
        </v-menu>
        <v-btn block color="red" class="white--text" @click="groupeDelete()">
          <v-icon left>mdi-cancel</v-icon>
          {{ $t('user_group.delete') }}
        </v-btn>
      </div>
    </div>

    <v-subheader>{{ $t('user_group.members') }}:</v-subheader>
    <v-list-item-group>
      <v-list-item v-for="user in members.filter(user => user.active)" :key="user.username">
        <user :user="user" :currentUserIsAdmin="isAdmin" />
      </v-list-item>
    </v-list-item-group>
  </v-list>
</template>

<script>
import User from "./User";
import { call, sync, get } from "vuex-pathify";
import * as types from "@/store/types.js";
export default {
    data() {
    return {
      menu: false
    };
  },
  components: { User },
  computed: {
    selectedUserList: sync("user/selectedUserList"),
    userList: get("user/userList"),
    members: sync("groupe/groupeMembers"),
    isAdmin: sync("groupe/isAdmin")
  },
  methods: {
    getUsers: call(`user/${types.getUsers}`),
    groupeDelete: call(`groupe/${types.groupeDelete}`),
    addMembers: call(`groupe/${types.addMembers}`),
    createGroupeAndCloseMenu() {
      this.addMembers(this.selectedUserList);
      this.menu = false;
      this.selectedUserList = [];
    },
    closeMenuAndResetInput() {
      this.menu = false;
      this.selectedUserList = [];
      this.groupeName = "";
    }
  }
};
</script>

<style lang="css">
.buttons {
  margin: 0px 20px;
}

.buttons > * {
  margin: 10px 0px;
}
</style>
