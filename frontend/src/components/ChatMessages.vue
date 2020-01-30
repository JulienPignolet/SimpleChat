<template>
  <div class="d-flex">
    <div class="flex-grow-1">
      <v-list
        style="height:82vh;"
        class="overflow-y-auto"
        three-line
      >
        <v-list-item
          v-for="item in items"
          :key="item.message"
        >
          <v-list-item-avatar color="grey">
            <v-icon dark>
              mdi-account-circle
            </v-icon>
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title v-html="item.pseudonyme" />
            <poll
              v-if="isSondage(item.message)"
              :poll-id="parseInt(item.message.split(':')[1])"
            />
            <v-list-item-subtitle
              v-else
              v-html="item.message"
            />
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </div>
      <user-group/>
    <div>

    </div>
  </div>
</template>

<script>
import { sync } from "vuex-pathify";
import Poll from "./Poll";
import UserGroup from "./UserGroup";
export default {
    components: {Poll, UserGroup},
    data: () => ({

  }),
  computed: {
    items: sync("chat/messageList")
  },
  methods : {
      isSondage(message) {
          const msgRegex = RegExp('^sondage_id:([0-9]+)$');
          return msgRegex.test(message);
      }
  }
}
</script>

<style lang="css">
</style>
