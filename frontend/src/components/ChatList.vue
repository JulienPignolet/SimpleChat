<template>
        <v-navigation-drawer
          permanent
        >
          <v-list
            dense
            rounded
          >
          <!-- TODO : Récupérer une vraie liste de salon-->
            <v-list-item
              v-for="item in items"
              :key="item.title"
              link
            >
              <v-list-item-icon>
                <v-icon>{{ item.icon }}</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>{{ item.title }}</v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-navigation-drawer>
</template>

<script>
export default {
  data() {
    return {
      items: [
        { title: "Salon #1" },
        { title: "Salon #2" },
        { title: "Nouveau salon", icon: "mdi-account-multiple-plus" }
      ]
    };
  },
  methods: {}
};
</script>

<style lang="css">
</style>