<template>
  <v-flex @submit.prevent="sendMessage(message)">
    <v-form style="width:90%" class="ma-auto pt-3">
      <v-text-field
        ref="message"
        v-model="message"
        label="Solo"
        placeholder="Saisir un message..."
        solo
        append-icon="mdi-send"
        @click:append="sendMessage(message)"
      >
        <template v-slot:prepend>
          <v-btn icon>
            <v-icon>mdi-vector-polygon</v-icon>
          </v-btn>
          <v-btn icon>
            <v-icon>mdi-file-plus</v-icon>
          </v-btn>
          <v-btn icon>
            <v-icon>mdi-clipboard-list-outline</v-icon>
          </v-btn>
        </template>
      </v-text-field>
    </v-form>
  </v-flex>
</template>

<script>
import * as types from "@/store/types.js";
export default {
  data () {
    return {
      message : ""
    }
  },
  methods: {
    sendMessage(message){
      this.$store.dispatch(`chat/${types.sendMessage}`, message)
      this.message = ""
      } 
  }
}
</script>

<style lang="css">
</style>