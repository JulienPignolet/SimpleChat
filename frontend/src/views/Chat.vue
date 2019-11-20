<template>
  <v-container fluid>
    <v-row no-gutters>
      <v-col>
        <chat-list />
      </v-col>
      <v-col cols=10> 
      <chat-messages />
      </v-col>
    </v-row>
    <v-bottom-navigation fixed app background-color="primary">
      <chat-box />
    </v-bottom-navigation>
  </v-container>
</template>
<script>
import ChatBox from "../components/ChatBox";
import ChatList from "../components/ChatList";
import ChatMessages from "../components/ChatMessages";
import { chat } from "../store/modules/chat";
export default {
  components: {
    ChatBox,
    ChatList,
    ChatMessages
  },
  beforeCreate() {
    this.$store.registerModule("chat", chat);
  }
};
</script>