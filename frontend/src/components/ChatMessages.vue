<template>
<v-list style="height:82vh;" class="overflow-y-auto" three-line>
          <v-list-item v-for="item in items" :key="item.message">
            <v-list-item-avatar color="grey">
              <v-icon dark>mdi-account-circle</v-icon>
            </v-list-item-avatar>
  
            <v-list-item-content>
              <v-list-item-title v-html="item.pseudonyme"></v-list-item-title>
              <v-list-item-subtitle v-html="item.message"></v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
      </v-list>
</template>

<script>
import { sync } from "vuex-pathify";
export default {
  data: () => ({

  }),
  computed: {
    items: sync("chat/messageList"),
  },
  methods : {
  }
}
</script>

<style lang="css">
</style>