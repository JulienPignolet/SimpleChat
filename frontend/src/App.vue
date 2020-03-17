<template>
  <v-app>
    <v-content>
      <app-bar />
      <router-view name="home" />
      <router-view />
      <router-view name="mainView" />
      <router-view name="chatBox" />
      <drawpad />
      <file-upload-dialog />
      <new-poll-dialog />
      <zoom-image-dialog />
      <alerte />
    </v-content>
  </v-app>
</template>

<script>
import AppBar from "@/components/AppBar.vue";
import Alerte from "@/components/Alerte.vue";
import FileUploadDialog from "@/components/FileUploadDialog";
import ZoomImageDialog from "@/components/ZoomImageDialog";
import NewPollDialog from "./components/NewPollDialog";
import Drawpad from "./components/Drawpad";

export default {
  name: "App",
  components: {
    Drawpad,
    NewPollDialog,
    AppBar,
    Alerte,
    FileUploadDialog,
    ZoomImageDialog
  }
};
</script>
