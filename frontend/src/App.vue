<template>
  <v-app>
    <v-content>
      <app-bar />
      <router-view name="home" />
      <router-view />
      <router-view name="mainView" />
      <router-view name="chatBox" />
      <file-upload-dialog />
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

export default {
  name: "App",
  components: {
    AppBar,
    Alerte,
    FileUploadDialog,
    ZoomImageDialog
  }
};
</script>
