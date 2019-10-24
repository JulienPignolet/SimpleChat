<template>
  <v-app>
    <v-content fluid>
      <app-bar />
      <router-view></router-view>
    </v-content>
    <alerte />
  </v-app>
</template>

<script>
import AppBar from "@/components/AppBar.vue";
import Alerte from '@/components/Alerte.vue'

export default {
  name: "App",
  components: {
    AppBar,
    Alerte
  }
};
</script>
