<template>
  <v-content>
    <chat-list />
    <user-list v-if="['users'].includes($route.name)"/>
    <chat-messages v-if="['groupe'].includes($route.name)" />
  </v-content>
</template>
<script>
import ChatList from "../components/Admin/ChatList";
import ChatMessages from "../components/Admin/ChatMessages";
import UserList from '../components/Admin/UserList'
export default {
  components: {
    ChatList,
    UserList,
    ChatMessages,
  },
  beforeCreate() {

  }
};
</script>
