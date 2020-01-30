<template>
  <v-menu
    v-model="menu"
    :close-on-content-click="false"
    :nudge-width="200"
    offset-x
  >
    <template v-slot:activator="{ on }">
      <v-list-item-icon style="margin-right: 10px;" v-on="on">
        <v-icon>mdi-account-circle-outline</v-icon>
      </v-list-item-icon>
      <v-list-item-content v-on="on">
        <v-list-item-title>{{ user.username }}</v-list-item-title>
      </v-list-item-content>
    </template>

    <v-card style="min-width: 400px;">
      <v-list>
        <v-list-item>
          <v-list-item-icon style="margin-right: 10px;">
            <v-icon>mdi-account-circle-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ user.username }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>

      <v-divider></v-divider>

      <v-list class="pa-2">
        <v-list-item>
          <v-btn
            block
            color="primary"
            class="white--text"
            @click="clickToAddFriend()"
          >
            <v-icon left>mdi-account-plus</v-icon>
            Ajouter en ami
          </v-btn>
        </v-list-item>

        <v-list-item>
          <v-btn
            block
            color="red"
            class="white--text"
            @click="clickToBlockUser()"
          >
            <v-icon left>mdi-cancel</v-icon>
            Bloquer cet utilisateur
          </v-btn>
        </v-list-item>
      </v-list>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" text @click="menu = false">Fermer</v-btn>
      </v-card-actions>
    </v-card>
  </v-menu>
</template>

<script>
  import {call} from "vuex-pathify";
  import * as types from "@/store/types.js";

  export default {
    components: {},
    props: ['user'],
    data: () => ({
      menu: false,
    }),
    computed: {},
    methods: {
      addFriend: call(`user/${types.addFriend}`),
      blockUser: call(`user/${types.blockUser}`),
      clickToAddFriend : function() {
        this.addFriend(this.user.id)
      },
      clickToBlockUser : function() {
        this.blockUser(this.user.id)
      }
    }
  }
</script>

<style lang="css">
</style>
