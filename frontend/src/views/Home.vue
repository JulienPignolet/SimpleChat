<template>
  <v-container fluid fill-height>
    <v-layout justify-center align-center>
      <v-flex text-xs-center>
        <login-form />
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import LoginForm from "../components/LoginForm";
export default {
  components: {
    LoginForm
  }
};
</script>