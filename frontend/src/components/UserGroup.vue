<template>
  <v-list
    style="border-left: 1px solid rgba(0,0,0,0.1);"
    dense
  >
    <div v-if="isAdmin">
      <v-subheader>{{ $t('user_group.actions') }}</v-subheader>

      <div class="buttons">
        <v-btn
          block
          color="primary"
          class="white--text"
        >
          <v-icon left>mdi-account-plus</v-icon>
          {{ $t('user_group.add_member') }}
        </v-btn>
        <v-btn
          block
          color="red"
          class="white--text"
          @click="groupeDelete()"
        >
          <v-icon left>mdi-cancel</v-icon>
          {{ $t('user_group.delete') }}
        </v-btn>
      </div>
    </div>

    <v-subheader>{{ $t('user_group.members') }}:</v-subheader>
    <v-list-item-group>
      <v-list-item v-for="user in members" :key="user.username">
        <user
          :user="user"
          :currentUserIsAdmin="isAdmin"
        />
      </v-list-item>
    </v-list-item-group>
  </v-list>
</template>

<script>
  import User from "./User";
  import {call, sync} from "vuex-pathify";
  import * as types from "@/store/types.js";
  export default {
    components: {User},
    computed: {
      members: sync("groupe/groupeMembers"),
      isAdmin: sync("groupe/isAdmin")
    },
    methods: {
      groupeDelete: call(`groupe/${types.groupeDelete}`)
    }
  }
</script>

<style lang="css">
  .buttons {
    margin: 0px 20px;
  }

  .buttons > * {
    margin: 10px 0px;
  }
</style>
