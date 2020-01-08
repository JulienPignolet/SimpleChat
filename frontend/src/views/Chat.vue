<template>
  <v-content>
    <chat-list />
    <!-- TO-DO : Voir s'il y a pas mieux à faire pour les conditions des routes, pas trop fan -->
    <friend-list v-if="['friends'].includes($route.name)"/>
    <chat-messages v-if="['groupe'].includes($route.name)" />
    <chat-box v-if="['groupe'].includes($route.name)"/>
    <new-group-dialog />
  </v-content>
</template>
<script>
import ChatList from "../components/ChatList";
import ChatMessages from "../components/ChatMessages";
import FriendList from '../components/FriendList'
import ChatBox from "../components/ChatBox";
import NewGroupDialog from "../components/NewGroupDialog"
export default {
  components: {
    ChatList,
    ChatBox,
    FriendList,
    ChatMessages,
    NewGroupDialog
  },
  beforeCreate() {

  }
};
</script>