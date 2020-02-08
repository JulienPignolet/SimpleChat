<template>
  <v-flex
    xs13
    sm9
    md6
  >
    <v-container v-if="fichier() !== undefined && isImage()">
      <img
        class="image-file"
        :alt="fichier() ? fichier().name : ''"
        :src="getLink()"
        @click="zoom()"
      >
    </v-container>
    <v-card
      v-else
      class="elevation-0"
      outlined
      :loading="fichier() === undefined"
    >
      <v-row
        no-gutters
        align="stretch"
      >
        <v-col
          align-self="center"
          style="text-align: center;"
        >
          <v-icon x-large>
            mdi-file-outline
          </v-icon>
        </v-col>
        <v-col cols="9">
          <v-card-text>
            <a
              class="title"
              @click="download()"
            >{{ fichier() ? fichier().name : '' }}</a>
            <v-spacer />
            <span class="subtitle-2">{{ fileConvertSize(fichier() ? fichier().size : 0) }}</span>
          </v-card-text>
        </v-col>
        <v-col align-self="center">
          <v-card-actions>
            <v-btn
              icon
              @click="download()"
            >
              <v-icon>mdi-download</v-icon>
            </v-btn>
          </v-card-actions>
        </v-col>
      </v-row>
    </v-card>
  </v-flex>
</template>

<script>
import { get, call, sync } from "vuex-pathify";
import * as types from "@/store/types.js";
import * as constants from "../constants/constants";
import { interfaceControl } from "@/store/modules/interfaceControl";
import RegisterStoreModule from "@/mixins/RegisterStoreModule";

export default {
  name: "File",
  mixins: [RegisterStoreModule],
  props: {
    fileId: {
      type: Number,
      default: null
    }
  },
  computed: {
    files: get("file/files"),
    file: sync("file/file")
  },
  created() {
    this.$store.dispatch(`file/${types.getFileData}`, this.fileId);
    this.registerStoreModule("interfaceControl", interfaceControl);
  },
  methods: {
    activateZoomImageDialog: call(
      `interfaceControl/${types.setZoomImageDialog}`
    ),
    fichier() {
      return this.files.find(file => file.id === this.fileId);
    },
    isImage() {
      return this.fichier().type.includes("image/");
    },
    download() {
      window.open(this.getLink(), "_blank");
    },
    getLink() {
      return `${constants.API_URL}api/fileUpload/${
        this.isImage() ? "getFile" : "downloadFile"
      }/${this.fileId}`;
    },
    zoom() {
      const img = new Image();

      img.onload = () => {
        this.file = {
          ...this.fichier(),
          width: img.width,
          height: img.height,
          link: img.src
        };
        this.activateZoomImageDialog(true);
      };

      img.src = this.getLink();
    },
    fileConvertSize(aSize) {
      const def = [
        [1, "bytes"],
        [1024, "KB"],
        [1024 * 1024, "MB"],
        [1024 * 1024 * 1024, "GB"],
        [1024 * 1024 * 1024 * 1024, "TB"]
      ];

      if (aSize === 0) {
        return "0 bytes";
      }

      aSize = Math.abs(parseInt(aSize, 10));
      for (let i = 0; i < def.length; i++) {
        if (aSize < def[i][0]) {
          console.log(aSize)
          let size = (aSize / def[i - 1][0]).toFixed(2);
          size = size.toString().replace(".00", "");
          return size + " " + def[i - 1][1];
        }
      }
    }
  }
};
</script>

<style lang="css">
a {
  text-decoration: none;
}

a:hover,
a:active {
  text-decoration: underline;
}

.image-file {
  max-height: 200px;
  cursor: pointer;
  border-radius: 5px;
}
</style>
