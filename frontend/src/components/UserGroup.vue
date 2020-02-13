<template>
  <v-list
    style="border-left: 1px solid rgba(0,0,0,0.1);"
    dense
  >
    <v-subheader>{{ $t('user_group.members') }}:</v-subheader>
    <v-list-item-group>
      <v-list-item v-for="user in members" :key="user.username">
        <user :user="user" />
      </v-list-item>
    </v-list-item-group>
  </v-list>
</template>

<script>
  import User from "./User";
  import {sync} from "vuex-pathify";
  export default {
    components: {User},
    data: () => ({
      users: [
        {
          name: 'Jordan'
        },
        {
          name: 'Thomas'
        }
      ]
    }),
    computed: {
      members: sync("groupe/groupeMembers")
    },
    methods: {}
  }
</script>

<style lang="css">
</style>
