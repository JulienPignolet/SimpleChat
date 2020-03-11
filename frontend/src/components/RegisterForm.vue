<template>
  <v-container fluid fill-height>
    <v-layout
      justify-center
      align-center
      @keydown.enter="register({username, password, passwordConfirm})"
      @keydown.escape="$router.push('/login')"
    >
      <v-flex text-xs-center>
        <v-content>
          <v-container fluid fill-height>
            <v-layout align-center justify-center>
              <v-flex xs12 sm8 md4>
                <v-card class="elevation-12">
                  <v-toolbar color="primary" dark flat>
                    <v-toolbar-title>{{ $t('register.title') }}</v-toolbar-title>
                  </v-toolbar>
                  <v-card-text>
                    <v-form>
                      <v-text-field
                        :label="$t('login.username')"
                        name="username"
                        id="username"
                        v-model="username"
                        prepend-icon="mdi-account"
                        type="text"
                      />
                      <v-text-field
                        id="password"
                        name="password"
                        :label="$t('login.password')"
                        v-model="password"
                        prepend-icon="mdi-lock"
                        type="password"
                      />
                      <v-text-field
                        id="repeatPassword"
                        name="repeatPassword"
                        :label="$t('register.confirm_password')"
                        v-model="passwordConfirm"
                        prepend-icon="mdi-lock"
                        type="password"
                      />
                    </v-form>
                  </v-card-text>
                  <v-card-actions>
                    <v-btn color="green" dark @click="$router.push('/login')">{{ $t('general.cancel') }}</v-btn>
                    <v-spacer></v-spacer>
                    <v-btn
                      color="primary"
                      @click="register({username, password, passwordConfirm})"
                    >{{ $t('register.sign_up') }}</v-btn>
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
import { call } from "vuex-pathify";
import RegisterStoreModule from "@/mixins/RegisterStoreModule";
import * as types from "@/store/types.js";
import { user } from "@/store/modules/user";
export default {
  mixins: [RegisterStoreModule],
  data() {
    return {
      username: "",
      password: "",
      passwordConfirm: ""
    };
  },
  created() {
    this.registerStoreModule("user", user);
  },
  methods: {
    register: call(`user/${types.register}`)
  }
};
</script>
