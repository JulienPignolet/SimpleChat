import Vue from 'vue';
import VueI18n from 'vue-i18n';
import fr from '../assets/i18n/translations/fr'
import de from '../assets/i18n/translations/de'
import en from '../assets/i18n/translations/en'
import ru from '../assets/i18n/translations/ru'
Vue.use(VueI18n);

const messages = { fr, en, de, ru };

export const i18n = new VueI18n({
    locale: 'fr',
    fallbackLocale: 'en',
    messages,
});
