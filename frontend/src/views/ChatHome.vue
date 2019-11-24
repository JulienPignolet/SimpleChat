<template>
  <v-content>
    <chat-list />
    <chat-messages />
    <chat-box />
    <new-group-dialog />
  </v-content>
</template>
<script>
import ChatList from "../components/ChatList";
import ChatMessages from "../components/ChatMessages";
import ChatBox from "../components/ChatBox";
import NewGroupDialog from "../components/NewGroupDialog"
import { chat } from "../store/modules/chat";
export default {
  components: {
    ChatList,
    ChatBox,
    ChatMessages,
    NewGroupDialog
  },
  beforeCreate() {
    this.$store.registerModule("chat", chat);
  }
};
</script>