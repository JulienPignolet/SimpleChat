<template>
  <div class="d-flex">
    <div class="flex-grow-1">
      <v-list
        id="messages-list"
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
              v-if="isSondage(item.type)"
              :poll-id="parseInt(item.message.split(':')[1])"
            />
            <file
              v-else-if="isFile(item.type)"
              :file-id="parseInt(item.message.split(':')[0])"
              @ready="scrollBottom()"
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
import File from "./File";
import Poll from "./Poll";
import UserGroup from "./UserGroup";
export default {
    components: {File, Poll, UserGroup},
    data: () => ({

  }),
  computed: {
    items: sync("chat/messageList")
  },
  updated() {
    this.scrollBottom()
  },
  methods : {
      isSondage(type) {
        return type === 'sondage';
      },
      isFile(type) {
        return type === 'fichier';
      },
      scrollBottom() {
        const messageList = this.$el.querySelector('#messages-list')
        messageList.scrollTop = messageList.scrollHeight
      }
  }
}
</script>

<style lang="css">
</style>
