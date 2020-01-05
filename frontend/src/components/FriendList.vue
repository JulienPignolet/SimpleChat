<template>
  <v-data-table :headers="headers" :items="friendList" class="elevation-1" hide-default-footer>
    <template v-slot:item.action="{ item }">
      <v-icon small @click="deleteFriend(item.id)">mdi-delete</v-icon>
    </template>
  </v-data-table>
</template>

<script>
import { sync, call} from "vuex-pathify";
import * as types from "@/store/types.js";
export default {
  data: () => ({
    headers: [
      {
        text: "Nom",
        align: "left",
        value: "username"
      },

      { text: "Chat en commun", value: "chat", sortable: false },
      { text: "Actions", value: "action", sortable: false }
    ]
  }),
  computed: {
    friendList: sync("user/friendList")
  },
  methods: {
     deleteFriend: call(`user/${types.deleteFriend}`)
  }
};
</script>

<style lang="css">
</style>