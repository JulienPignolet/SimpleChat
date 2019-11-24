<template>
  <v-content>
    <v-container fluid fill-height>
      <v-layout align-center justify-center>
        <v-flex xs12 sm8 md4>
          <v-card class="elevation-12">
            <v-toolbar dense color="primary" dark flat>
              <v-toolbar-title>Connexion</v-toolbar-title>
            </v-toolbar>
            <v-card-text>
              <v-form>
                <v-text-field
                  label="Pseudonyme"
                  v-model="username"
                  prepend-icon="mdi-account"
                  type="text"
                ></v-text-field>
                <v-text-field
                  id="password"
                  label="Mot de passe"
                  v-model="password"
                  prepend-icon="mdi-lock"
                  type="password"
                ></v-text-field>
              </v-form>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="primary"
                @click="connexion({username, password}) "
              >Me connecter</v-btn>
            </v-card-actions>
          </v-card>
        </v-flex>
      </v-layout>
    </v-container>
  </v-content>
</template>

<script>
import { call } from "vuex-pathify";
import * as types from "@/store/types.js";
import { user } from "@/store/modules/user";
export default {
  data() {
    return {
      username: "",
      password: ""
    };
  },
  beforeCreate() {
    this.$store.registerModule("user", user);
  },
  methods: {
    connexion: call(`user/${types.connexion}`)
  }
};
</script>
