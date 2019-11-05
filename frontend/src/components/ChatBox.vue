<template>
  <v-flex md8 offset-md2 class="pa-1" @submit.prevent="sendMessage(message)">
    <v-form style="width:100%">
    <v-text-field
      label="Solo"
      placeholder="Saisir un message..."
      v-model="message"
      solo
      ref="message"
      >
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
      this.$store.dispatch("chat/" + types.sendMessage, message)
      this.message = ""
      } 
  }
}
</script>

<style lang="css">
</style>