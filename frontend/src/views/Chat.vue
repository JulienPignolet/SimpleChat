<template>
  <v-container fluid fill-height>
    <chat-list/>
    <v-layout row justify-space-between>
    <chat-messages/>
    <chat-box/>
    </v-layout>
  </v-container>
</template>
<script>

import ChatBox from "../components/ChatBox";
import ChatList from "../components/ChatList"
import ChatMessages from "../components/ChatMessages"
import { chat } from "../store/modules/chat";
export default {
  components: {
    ChatBox,
    ChatList,
    ChatMessages
  },
  beforeCreate() {
    this.$store.registerModule("chat", chat);
  },
};
</script>