<template>
  <div>
    <v-menu v-model="menu" :close-on-content-click="false" :nudge-width="200" offset-x>
      <v-card>
        <v-card-actions>
          <v-spacer/>

          <v-btn text @click="menu = false">{{ $t('general.cancel') }}</v-btn>
          <v-btn color="primary" text @click="clickToAddFriend()">{{ $t('general.add') }}</v-btn>
        </v-card-actions>
      </v-card>
    </v-menu>

    <v-data-table :headers="headers" :items="userList" class="elevation-1" hide-default-footer>
      <template v-slot:item.action="{ item }">
        <v-icon small @click="deleteFriend(item.id)">mdi-delete</v-icon>
      </template>
      <template v-slot:item.chat="{ item }">
        <v-chip v-for="groupe in groupeCommun[item.id]" :key="groupe.id" @click="$router.push(`/chat/group/${groupe.id}`)">
          {{groupe.name}}
        </v-chip>
      </template>
    </v-data-table>
  </div>
</template>

<script>
import { sync, get, call } from "vuex-pathify";
import * as types from "@/store/types.js";
export default {
  created(){
    this.getGroupesCommun()
  },
  data: () => ({
    friendId: "",
    menu: false,
    headers: [
      {
        text: "Nom",
        align: "left",
        value: "username"
      },
      { text: "Actions", value: "action", sortable: false }
    ]
  }),
  computed: {
    friendList: sync("user/friendList"),
    userList: get("user/userList"),
    groupeCommun : get('groupe/groupeCommunList')
  },
  methods: {
    deleteFriend: call(`user/${types.deleteFriend}`),
    addFriend: call(`user/${types.addFriend}`),
    getGroupesCommun: call(`groupe/${types.getGroupesCommun}`),
    clickToAddFriend: function() {
      this.addFriend(this.friendId);
      this.menu = false;
      this.friendId = "";
    }
  }
};
</script>

<style lang="css">
</style>
