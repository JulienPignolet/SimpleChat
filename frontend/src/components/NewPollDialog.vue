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
        <span class="headline">Créer un sondage</span>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row no-gutters>
            <v-col
              cols="12"
              sm="12"
            >
              <v-text-field
                label="Question ?"
                prepend-inner-icon="mdi-help-circle"
                outlined
                required
              />
            </v-col>
          </v-row>
          <v-row no-gutters>
            <v-col
              cols="12"
              sm="6"
              class="pr-1"
            >
              <v-text-field
                label="Date de début"
                prepend-inner-icon="mdi-clock"
                outlined
              />
            </v-col>
            <v-col
              cols="12"
              sm="6"
              class="pl-1"
            >
              <v-text-field
                label="Date de fin"
                prepend-inner-icon="mdi-clock"
                outlined
              />
            </v-col>
          </v-row>
          <v-row no-gutters>
            <v-col>
              <p>Réponses :</p>
            </v-col>
          </v-row>
          <v-row no-gutters>
            <v-col>
              <v-text-field
                label="Réponse n°1"
                outlined
              />
            </v-col>
          </v-row>
          <v-row no-gutters>
            <v-col>
              <v-btn
                width="100%"
                outlined
                color="blue darken-2"
              >
                Ajouter une réponse
              </v-btn>
            </v-col>
          </v-row>
          <v-row no-gutters>
            <v-col>
              <v-checkbox
                label="Anonymiser les votes"
                color="blue darken-2"
                value="false"
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
          @click="dialog = false"
        >
          Envoyer
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
    export default {
        name: "NewPollDialog",
        data: () => ({
            dialog: false,
        }),
    }
</script>

<style scoped>

</style>