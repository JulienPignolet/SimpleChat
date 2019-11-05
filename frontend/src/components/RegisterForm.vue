<template>
  <v-container fluid fill-height>
    <v-layout justify-center align-center>
      <v-flex text-xs-center>
        <v-content>
            <v-container fluid fill-height>
            <v-layout align-center justify-center>
                <v-flex xs12 sm8 md4>
                <v-card class="elevation-12">
                    <v-toolbar color="primary" dark flat>
                    <v-toolbar-title>Inscription</v-toolbar-title>
                    </v-toolbar>
                    <v-card-text>
                    <v-form>
                        <!-- TODO : Changer les v-model par un autre truc mieux mais jme rappelle plus faut que je regarde plus tard-->
                        <v-text-field
                        label="Pseudonyme"
                        name="username"
                        v-model="username"
                        prepend-icon="mdi-account"
                        type="text"
                        ></v-text-field>

                        <!-- TODO : Changer les v-model par un autre truc mieux mais jme rappelle plus faut que je regarde plus tard-->
                        <v-text-field
                        id="password"
                        name="password"
                        label="Mot de passe"
                        v-model="password"
                        prepend-icon="mdi-lock"
                        type="password"
                        ></v-text-field>

                        <!-- TODO : Changer les v-model par un autre truc mieux mais jme rappelle plus faut que je regarde plus tard-->
                        <v-text-field
                        id="repeatPassword"
                        name="repeatPassword"
                        label="Répéter le mot de passe"
                        prepend-icon="mdi-lock"
                        type="password"
                        ></v-text-field>
                    </v-form>
                    </v-card-text>
                    <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn
                        color="primary"
                        @click="register"
                    >S'inscrire</v-btn>
                    </v-card-actions>
                </v-card>
                </v-flex>
            </v-layout>
            </v-container>
        </v-content>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import axios from 'axios';
import * as constants from "../constants/constants";

export default {
  data() {
    return {
      username: "",
      password: "",
    };
  },

  methods: {
    register  : function() {
      let request = { "username": this.username,"password": this.password,"passwordConfirm": this.password };
      console.log(request);

      axios
      .post(constants.API_URL + 'registrationTest', request)
      .then(response => {
        console.log(response);
      }) 
    }
  }
};
</script>
