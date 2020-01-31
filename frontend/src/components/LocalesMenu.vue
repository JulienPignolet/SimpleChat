<template>
  <v-menu
    bottom
    left
    offset-y
    max-height="calc(100% - 16px)"
    transition="slide-y-transition"
    light
  >
    <template v-slot:activator="{ attrs, on }">
      <v-btn
        :aria-label="$t('Vuetify.AppToolbar.translations')"
        class="text-capitalize"
        text
        v-bind="attrs"
        v-on="on"
        color="white"
      >
        <v-icon :left="$vuetify.breakpoint.mdAndUp">
          mdi-translate
        </v-icon>

        <span
          class="subtitle-1 text-capitalize font-weight-light hidden-sm-and-down"
          v-text="currentLanguage().name"
        />

        <v-icon
          class="hidden-sm-and-down"
          right
        >
          mdi-menu-down
        </v-icon>
      </v-btn>
    </template>

    <v-list
      dense
      nav
    >
      <template v-for="(language, i) in languages">
        <v-list-item
          :key="language.locale"
          @click="changeLocale(language.locale)"
        >
          <v-list-item-title v-text="language.name" />
        </v-list-item>
      </template>
    </v-list>
  </v-menu>
</template>

<script>
  import { i18n } from '../plugins/i18n';

  export default {
    name: 'LocalesMenu',
    data: () => ({
      languages: [
        {locale: 'en', name: 'English'},
        {locale: 'fr', name: 'Français'},
        {locale: 'de', name: 'Deutsch'},
        {locale: 'ru', name: 'Русский'},
      ]
    })
    ,
    mounted() {
      if(localStorage.locale) {
        i18n.locale = localStorage.locale;
      }
    },
    methods: {
      changeLocale(locale) {
        i18n.locale = locale;
        localStorage.locale = locale;
      },
      currentLanguage() {
        return this.languages.find(
          language => language.locale === i18n.locale
        )
      }
    },
  }
</script>

<style lang="css">

</style>
