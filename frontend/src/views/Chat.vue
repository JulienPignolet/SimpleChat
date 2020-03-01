<template>
  <v-content>
    <chat-list />
    <!-- TO-DO : Voir s'il y a pas mieux à faire pour les conditions des routes, pas trop fan -->
    <friend-list v-if="['friends'].includes($route.name)"/>
    <chat-messages v-if="['groupe'].includes($route.name)" />
    <chat-box v-if="['groupe'].includes($route.name)"/>
    <new-group-dialog />
    <file-upload-dialog />
    <zoom-image-dialog />
  </v-content>
</template>
<script>
import ChatList from "../components/ChatList";
import ChatMessages from "../components/ChatMessages";
import FriendList from '../components/FriendList'
import ChatBox from "../components/ChatBox";
import NewGroupDialog from "../components/NewGroupDialog"
import FileUploadDialog from "../components/FileUploadDialog"
import ZoomImageDialog from "../components/ZoomImageDialog" 
export default {
  components: {
    ChatList,
    ChatBox,
    FriendList,
    ChatMessages,
    NewGroupDialog,
    FileUploadDialog,
    ZoomImageDialog
  },
  beforeCreate() {

  }
};
</script>
