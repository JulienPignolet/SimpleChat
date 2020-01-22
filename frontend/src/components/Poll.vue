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
        :loading="sondage() === undefined"
      >
        <v-toolbar
          dense
          flat
        >
          <v-icon class="pr-2">
            mdi-help-circle
          </v-icon>
          <div class="subheading">
            {{ sondage() ? sondage().question : '' }}
          </div>
        </v-toolbar>
        <v-divider />
        <v-card-text
          v-if="sondage()"
          class="py-0"
        >
          <v-radio-group
            v-if="!showResults()"
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
          <div
            v-if="showResults()"
          >
            <v-row
              v-for="reponse in sondage().reponseSondages"
              :key="reponse.id"
              no-gutters
            >
              <v-col
                cols="12"
                sm="9"
              >
                <v-progress-linear
                  active
                  background-opacity="0.3"
                  height="20pt"
                  rounded
                  striped
                  :value="nbVotes() > 0 ? (reponse.nbVote / nbVotes()) * 100 : 0"
                  :color="reponse.nbVote === maxVotes() ? 'green' : 'grey'"
                  class="my-2"
                  style="text-align: left"
                >
                  {{ reponse.reponse }}
                </v-progress-linear>
              </v-col>
              <v-col
                cols="12"
                sm="2"
                class="ml-2 align-self-center"
              >
                {{ reponse.nbVote }} vote{{ reponse.nbVote !== 1 ? 's' : '' }}
              </v-col>
            </v-row>
          </div>
        </v-card-text>
        <v-card-actions
          v-if="!showResults()"
          class="py-0"
        >
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
import * as types from "../store/types";

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
      reponseId: undefined,
      sondage_: undefined
    }
  },
  computed: {
    sondages: get("sondage/sondages")
  },
  created() {
    this.$store.dispatch(`sondage/getSondage`, this.pollId);
  },
  methods: {
    sondage() {
      if (this.sondage_ === undefined) {
          this.sondage_ = this.sondages.find(sondage => sondage.id === this.pollId);
      }
      return this.sondage_;
    },
    vote() {
        console.log(this.reponseId);
        this.$store.dispatch(`sondage/${types.sendVote}`, { "sondageId": this.sondage().id, "reponseId": this.reponseId});
    },
    hasUserVoted() {
      let userVote = undefined;

      if (this.sondage() !== undefined) {
          this.sondage().reponseSondages.forEach(
              reponse => {
                  reponse.listVotes.forEach(
                      vote => {
                        if (vote.user.id === this.$store.state.user.user.id) {
                            userVote = vote;
                        }
                      }
                  )
              }
          );
      }

      return userVote !== undefined;
    },
    showResults() {
      // TODO Prendre en compte la date de fin et ajouter une heure de fin
      return this.hasUserVoted();
    },
    nbVotes() {
        let nb = 0;
        if (this.sondage() !== undefined) {
            this.sondage().reponseSondages.forEach(
                reponse => {
                    nb += reponse.nbVote;
                }
            );
        }

        return nb;
    },
    maxVotes() {
        let max = 0;
        if (this.sondage() !== undefined) {
            this.sondage().reponseSondages.forEach(
                reponse => {
                    if (reponse.nbVote > max) {
                        max = reponse.nbVote;
                    }
                }
            );
        }

        return max;
    }
  }
};
</script>
