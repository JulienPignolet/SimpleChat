<template>
  <!-- TODO : Taille en dur -> mettre une taille relative, le css ça me soule, faites le svp-->
  <v-parallax height="1000" src="https://cdn.vuetifyjs.com/images/parallax/material.jpg">
    <v-row align="center" justify="center">
      <v-col class="text-center">
        <h1 class="display-1 font-weight-thin mb-4">Bienvenue sur SimpleChat {{user}}</h1>
        <h4 class="subheading">Commence à chatter avec tes amis !</h4>
      </v-col>
    </v-row>
  </v-parallax>
</template>

<script>
import { get } from "vuex-pathify";
export default {
  computed: {
    user: get("user/user@username")
  }
};
</script>
