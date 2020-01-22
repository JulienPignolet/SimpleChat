<template>
  <v-layout @keydown.enter="vote()">
    <v-flex
      xs13
      sm9
      md6
    >
      <v-card
        class="elevation-0"
        outlined
      >
        <v-toolbar
          dense
          flat
        >
          <v-icon class="pr-2">
            mdi-help-circle
          </v-icon>
          <div class="subheading">
            {{ sondage().question }}
          </div>
        </v-toolbar>
        <v-divider />
        <v-card-text class="py-0">
          <v-radio-group
            v-model="reponseId"
            :mandatory="false"
          >
            <v-radio
              v-for="reponse in sondage().reponseSondages"
              :key="reponse.id"
              :label="reponse.reponse"
              :value="reponse.id"
            />
          </v-radio-group>
        </v-card-text>
        <v-card-actions class="py-0">
          <v-spacer />
          <v-btn
            icon
            :disabled="reponseId === undefined"
            @click="vote()"
          >
            <v-icon>mdi-send</v-icon>
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
import {get} from "vuex-pathify";

export default {
  name: "Poll",
  props: {
    pollId: {
      type: Number,
      default: null
    }
  },
  data () {
    return {
      reponseId: undefined
    }
  },
  computed: {
    sondages: get("sondage/sondages"),
  },
  created() {
    this.$store.dispatch(`sondage/getSondage`, this.pollId);
  },
  methods: {
    sondage() {
      return this.sondages.find(sondage => sondage.id === this.pollId);
    },
    vote() {
        console.log(this.reponseId);
    }
  }
};
</script>
