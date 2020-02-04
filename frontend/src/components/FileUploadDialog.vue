<template>
  <v-dialog
    v-model="dialog"
    max-width="600px"
  >
    <v-card>
      <v-card-title>
        <span class="headline">{{ $t('dialog.file_upload.title') }}</span>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-file-input
            v-model="files"
            color="primary"
            counter
            :label="$t('dialog.file_upload.file_input')"
            multiple
            :placeholder="$t('dialog.file_upload.select_files')"
            prepend-icon="mdi-paperclip"
            outlined
            :show-size="1000"
          >
            <template v-slot:selection="{ index, text }">
              <v-chip
                color="primary"
                dark
                label
                small
                @click="deleteFile(index, $event)"
              >
                {{ text }}
              </v-chip>
            </template>
          </v-file-input>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn
          color="secondary"
          text
          @click="cancel()"
        >
          {{ $t('general.cancel') }}
        </v-btn>
        <v-btn
          color="primary"
          text
          :disabled="files.length === 0"
          @click="submitFiles()"
        >
          {{ $t('general.send') }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { sync } from "vuex-pathify";
import * as types from "@/store/types.js";

export default {
  computed: {
    dialog: sync("interfaceControl/fileUploadDialog"),
    files: sync("file/newFiles")
  },
  methods: {
    submitFiles() {
      let formData = new FormData();
      
      formData.append("userId", this.$store.state.user.user.id)
      formData.append("groupeId", this.$store.state.groupe.groupe.id)

      if (this.files.length === 1) {
        formData.append("file", this.files[0]);
        this.sendFile(formData);
      } else {
        for (var i = 0; i < this.files.length; i++) {
          formData.append("files", this.files[i]);
        }

        this.sendFiles(formData);
      }

      this.files = [];
    },
    sendFile(file) {
      this.$store.dispatch(`file/${types.sendFile}`, file);
    },
    sendFiles(files) {
      this.$store.dispatch(`file/${types.sendMultipleFiles}`, files);
    },
    cancel() {
      this.dialog = false;
      this.files = [];
    },
    deleteFile(index, event) {
      this.files.splice(index, 1);
      event.stopPropagation()
    },
  }
};
</script>
<style lang="css">
</style>

