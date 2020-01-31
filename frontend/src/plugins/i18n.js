import Vue from 'vue';
import VueI18n from 'vue-i18n';
Vue.use(VueI18n);

const messages = {
    'fr': {
        'login-title': 'Connexion à SimpleChat',
        'sign-in': 'Se connecter',
        'sign-up': 'Créer un compte',
        'username': 'Pseudonyme',
        'password': 'Mot de passe',
        'wrong-credentials': 'Informations saisies invalides'
    },
    'en': {
        'login-title': 'Sign in to SimpleChat',
        'sign-in': 'Sign in',
        'sign-up': 'Sign up',
        'username': 'Username',
        'password': 'Password',
        'wrong-credentials': 'Wrong credentials'
    },
    'de': {
        'login-title': 'Sich bei SimpleChat einmelden',
        'sign-in': 'Einloggen',
        'sign-up': 'Anmelden',
        'username': 'Nutzername',
        'password': 'Passwort',
        'wrong-credentials': 'Falsche Anmeldeinformationen'
    },
    'ru': {
        'login-title': 'Войдите в SimpleChat',
        'sign-in': 'войти в систему',
        'sign-up': 'зарегистрироваться',
        'username': 'имя пользователя',
        'password': 'пароль',
        'wrong-credentials': 'Неправильные учетные данные'
    }
};

export const i18n = new VueI18n({
    locale: 'en',
    fallbackLocale: 'fr',
    messages,
});
