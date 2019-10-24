<template>
    <div id="login">
        <h1>Connexion</h1>

        <form action="" v-on:submit.prevent>
            <div class="loginLogo">
                <img src="../../public/images/loginLogo.svg" alt="login logo">
            </div>

            <v-text-field
                    outlined
                    dense
                    label="Pseudonyme"
                    prepend-inner-icon="mdi-account"
                    color="#368BD6"
                    height="2"
                    v-model="user.pseudo"
            ></v-text-field>

            <v-text-field
                    outlined
                    dense
                    type="password"
                    label="Mot de passe"
                    prepend-inner-icon="mdi-lock"
                    color="#368BD6"
                    height="2"
                    v-model="user.password"
            ></v-text-field>

            <button type="submit" class="button" v-on:click="showUser">Connexion</button>

        </form>
    </div>
</template>

<script>
    export default {
        data () {
            return {
                user : {
                    "pseudo" : "",
                    "password" : ""
                }
            }
        },

        methods: {
            showUser () {
                console.log(this.user.pseudo);
            }
        }

    }
</script>

<style lang="scss" src="./css/login.scss"></style>