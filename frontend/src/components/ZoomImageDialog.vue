<template>
  <v-dialog
    v-model="dialog"
    max-width="70vw"
    :width="file.width"
    content-class="zoom-dialog"
    overlay-opacity="0.85"
  >
    <v-card>
      <v-img
        :src="file.link"
        max-height="70vh"
      />
    </v-card>
    <a
      class="img-link"
      @click="openLink()"
    >Ouvrir l'original</a>
  </v-dialog>
</template>

<script>
import { sync, get } from "vuex-pathify";

export default {
  computed: {
    dialog: sync("interfaceControl/zoomImageDialog"),
    file: get("file/file")
  },
  methods: {
    openLink() {
      window.open(this.file.link, "_blank");
    }
  }
};
</script>
<style lang="css">
.zoom-dialog {
  -webkit-box-shadow: none;
  box-shadow: none;
}

.zoom-dialog > a.img-link {
  color: #c0c0c0;
}

.zoom-dialog > a.img-link:hover,
.zoom-dialog > a.img-link:active {
  color: #fcfcfc;
}
</style>

