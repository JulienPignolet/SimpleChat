<template>
<v-list style="height:82vh;" class="overflow-y-auto" three-line>
        <template v-for="(item, index) in items">
          <v-subheader
            v-if="item.header"
            :key="item.header"
            v-text="item.header"
          ></v-subheader>
  
          <v-divider
            v-else-if="item.divider"
            :key="index"
            :inset="item.inset"
          ></v-divider>
  
          <v-list-item
            v-else
            :key="item.pseudonyme"
            click=""
          >
            <v-list-item-avatar color="grey">
              <v-icon dark>mdi-account-circle</v-icon>
            </v-list-item-avatar>
  
            <v-list-item-content>
              <v-list-item-title v-html="item.pseudonyme"></v-list-item-title>
              <v-list-item-subtitle v-html="item.message"></v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </template>
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