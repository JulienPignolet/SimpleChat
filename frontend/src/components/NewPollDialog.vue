<template>
  <v-dialog
    v-model="dialog"
    max-width="600px"
  >
    <template v-slot:activator="{ on }">
      <v-btn
        icon
        v-on="on"
      >
        <v-icon>mdi-clipboard-list-outline</v-icon>
      </v-btn>
    </template>
    <v-card>
      <v-card-title>
        <span class="headline">{{ $t('dialog.new_poll.title') }}</span>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row no-gutters>
            <v-col
              cols="12"
              sm="12"
            >
              <v-text-field
                v-model="question"
                :label="$t('dialog.new_poll.question')"
                prepend-inner-icon="mdi-help-circle"
                required
              />
            </v-col>
          </v-row>
          <v-row no-gutters>
            <v-col
              cols="12"
              sm="12"
            >
              <v-menu
                ref="menuDateFin"
                v-model="menuDateFin"
                :close-on-content-click="false"
                :return-value.sync="dateFin"
                transition="scale-transition"
                offset-y
                max-width="290px"
                min-width="290px"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    v-model="dateFinFormatted"
                    :label="$t('dialog.new_poll.end_date')"
                    prepend-inner-icon="mdi-calendar"
                    readonly
                    @blur="dateFin = parseDate(dateFinFormatted)"
                    v-on="on"
                  />
                </template>
                <v-date-picker
                  v-model="dateFin"
                  :min="today"
                  :first-day-of-week="1"
                  :locale="getLocale()"
                  scrollable
                >
                  <v-spacer />
                  <v-btn
                    text
                    color="primary"
                    @click="menuDateFin = false"
                  >
                    {{ $t('general.cancel') }}
                  </v-btn>
                  <v-btn
                    text
                    color="primary"
                    @click="$refs.menuDateFin.save(dateFin)"
                  >
                    {{ $t('general.ok') }}
                  </v-btn>
                </v-date-picker>
              </v-menu>
            </v-col>
          </v-row>
          <v-row no-gutters>
            <v-col>
              <p
                style="color: black"
                class="mt-2"
              >
                {{ $t('dialog.new_poll.propositions') }}:
              </p>
            </v-col>
          </v-row>
          <v-row
            v-for="reponse in reponseSondages"
            :key="reponse.id"
            no-gutters
          >
            <v-col>
              <v-text-field
                v-model="reponse.reponse"
                :label="$t('dialog.new_poll.proposition') + ' n°' + reponse.id"
              />
            </v-col>
          </v-row>
          <v-row no-gutters>
            <v-col>
              <v-btn
                width="100%"
                outlined
                color="blue darken-2"
                @click="addReponse()"
              >
                {{ $t('dialog.new_poll.add_proposition') }}
              </v-btn>
            </v-col>
          </v-row>
          <v-row no-gutters>
            <v-col>
              <v-checkbox
                v-model="votesAnonymes"
                :label="$t('dialog.new_poll.anonymize_votes')"
                color="blue darken-2"
                hide-details
              />
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions class="justify-center">
        <v-btn
          color="primary"
          large
          class="px-5"
          :disabled="!canSubmit()"
          @click="submit()"
        >
          {{ $t('general.send') }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
    import {ReponseSondage} from "../models/ReponseSondage";
    import * as types from "../store/types";
    import { i18n } from '../plugins/i18n';
    import languages from '../assets/i18n/languages';

    export default {
        name: "NewPollDialog",
        data: vm => ({
            dialog: false,
            menuDateFin: false,
            today: new Date().toISOString().substr(0, 10),
            question: "",
            dateFin: new Date().toISOString().substr(0, 10),
            dateFinFormatted: vm.formatDate(new Date().toISOString().substr(0, 10)),
            votesAnonymes: false,
            reponseSondages: new Array(new ReponseSondage(1, "", 0))
        }),

        computed: {
            computedDateFormatted () {
                return this.formatDate(this.dateFin)
            },
        },

        watch: {
            dateFin(val) {
                this.dateFinFormatted = this.formatDate(val)
            },
        },

        methods: {
            submit() {
                this.dialog = false;
                let sondage = {
                    question: this.question,
                    dateFin: this.dateFin,
                    reponseSondages: this.reponseSondages.map(r => r.reponse),
                    votesAnonymes: this.votesAnonymes
                };
                this.$store.dispatch(`sondage/${types.sendSondage}`, sondage);

                // Reset the form
                this.question = "";
                this.reponseSondages = new Array(new ReponseSondage(1, "", 0));
                this.dateFin = new Date().toISOString().substr(0, 10);
                this.dateFinFormatted = this.formatDate(new Date().toISOString().substr(0, 10));
                this.votesAnonymes = false;
            },
            canSubmit() {
                let isQuestionEmpty = this.question === "";
                let hasAtLeastOneResponses = this.reponseSondages.filter(response => response.reponse !== "").length > 0;
                return !isQuestionEmpty && hasAtLeastOneResponses;
            },
            addReponse() {
                this.reponseSondages.push(new ReponseSondage(this.reponseSondages.length + 1, "", 0));
            },
            formatDate (date) {
                if (!date) return null;

                const [year, month, day] = date.split('-');
                return `${day}/${month}/${year}`;
            },
            parseDate (date) {
                if (!date) return null;

                const [day, month, year] = date.split('/');
                return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
            },
            getLocale() {
              const language = languages.languages.find(
                  language => language.locale === i18n.locale
              );
              return language.locale + '-' + language.country;
            }
        }
    }
</script>

<style scoped>

</style>
