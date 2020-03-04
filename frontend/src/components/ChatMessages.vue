<template>
  <div class="d-flex">
    <div class="flex-grow-1">
      <v-list id="messages-list" style="height:82vh;" class="overflow-y-auto">
        <v-list-item v-for="(item, index) in messages" :key="item.message + index">
          <v-list-item-avatar color="grey">
            <v-icon dark>mdi-account-circle</v-icon>
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title>{{ item.pseudonyme }}</v-list-item-title>
            <poll v-if="isSondage(item.type)" :poll-id="parseInt(item.message.split(':')[1])" />
            <file
              v-else-if="isFile(item.type)"
              :file-id="parseInt(item.message.split(':')[0])"
              @ready="scrollBottom()"
            />
            <v-list-item-subtitle
              v-else
              style="overflow: visible; white-space: initial;"
              v-html="transformUrls(item.message)"
            />
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </div>
    <user-group />
  </div>
</template>

<script>
import { sync, call } from "vuex-pathify";
import File from "./File";
import Poll from "./Poll";
import UserGroup from "./UserGroup";
import * as types from "@/store/types.js";
export default {
  components: { File, Poll, UserGroup },
  data: () => ({}),
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.setGroupe({ id: parseInt(to.params.groupId, 10) });
    });
  },
  computed: {
    items: sync("chat/messageList"),
    messages() {
      return this.items.filter(item => item.type !== "drawpad");
    }
  },
  updated() {
    this.scrollBottom();
  },
  methods: {
    setGroupe: call(`groupe/${types.setGroupe}`),
    isSondage(type) {
      return type === "sondage";
    },
    isFile(type) {
      return type === "fichier";
    },
    transformUrls(message) {
      const regex = /((https?):\/\/|w{3}\.)(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-/]))?/gm;

      let m,
        resultMessage = "",
        index = 0;

      while ((m = regex.exec(message)) !== null) {
        if (m.index === regex.lastIndex) {
          regex.lastIndex++;
        }

        const match = m[0],
          lien = `${match.startsWith("http") ? "" : "//"}${match}`,
          lienHtml = `<a href="${lien}" target="_blank">${match}</a>`;

        resultMessage += message.slice(index, m.index);
        resultMessage += lienHtml;
        index = regex.lastIndex;
      }

      resultMessage += message.slice(index, message.length);

      return resultMessage;
    },
    scrollBottom() {
      const messageList = this.$el.querySelector("#messages-list");
      messageList.scrollTop = messageList.scrollHeight;
    }
  }
};
</script>

<style lang="css">
</style>
