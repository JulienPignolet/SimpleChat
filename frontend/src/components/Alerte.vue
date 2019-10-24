<template>
  <v-snackbar
    v-model="activation"
    :color="alerte.couleur"
    :timeout="alerte.timeout"
  >{{alerte.texte}}</v-snackbar>
</template>

<script>
import { sync } from "vuex-pathify";
import { alerte } from "../store/modules/alerte";
export default {
  name: "Alerte",
  beforeCreate() {
    this.$store.registerModule("alerte", alerte);
  },
  computed: {
    alerte: sync("alerte/alerte"),
    activation: sync("alerte/alerte@activation")
  },
  methods: {}
};
</script>