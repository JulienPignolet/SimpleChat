<template>
  <v-navigation-drawer :clipped="$vuetify.breakpoint.lgAndUp" app>
    <v-list-item link @click="$router.push('/admin/users')" @mousedown.stop @touchstart.native.stop>
      <v-list-item-icon>
        <v-icon>{{ "mdi-account" }}</v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>{{ $t('chat_list.user_list') }}</v-list-item-title>
      </v-list-item-content>
    </v-list-item>

    <v-divider></v-divider>
    <v-list dense>
      <v-subheader>
        {{ $t('chat_list.discussions') }}
        <v-spacer />
      </v-subheader>
      <v-list-item-group>
        <v-list-item v-for="groupe in groupes" :key="groupe.id" @click="setGroupe(groupe)">
          <v-list-item-content>
            <v-list-item-title>{{ groupe.name }}</v-list-item-title>
          </v-list-item-content>
          <v-list-item-action>
            <div v-if="groupe.deletedat == null" >
              <v-btn icon @click="deleteGroupe(groupe.id)">
                <v-icon color="red">mdi-delete</v-icon>
              </v-btn>
            </div>
            <div v-else>
              <v-btn icon @click="restoreGroupe(groupe.id)">
                <v-icon color="grey">mdi-backup-restore</v-icon>
              </v-btn>
            </div>
          </v-list-item-action>
        </v-list-item>
      </v-list-item-group>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
import { sync, call } from "vuex-pathify";
import * as types from "@/store/types.js";
// import { groupe } from "@/store/modules/groupe";
// import { interfaceControl } from "@/store/modules/interfaceControl";
import RegisterStoreModule from "@/mixins/RegisterStoreModule";
export default {
  mixins: [RegisterStoreModule],
  data() {
    return {};
  },
  watch: {
    groupe() {
      if (this.groupe.id != undefined) {
        this.$router.push(`/admin/group/${this.groupe.id}`);
        this.chooseGroup();
      }
    }
  },
  created() {
    // this.registerStoreModule("groupe", groupe);
    // this.registerStoreModule("interfaceControl", interfaceControl);
    this.getGroupes();
    this.getUsers();
  },
  computed: {
    currentGroup: sync("groupe/groupe"),
    groupes: sync("groupe/allGroupeList"),
    groupe: sync("groupe/groupe")
  },
  methods: {
    getGroupes: call(`groupe/${types.getAllGroupes}`),
    getUsers: call(`user/${types.getUsers}`),
    createGroupe: call(`groupe/${types.createGroupe}`),
    chooseGroup: call(`groupe/${types.chooseGroupAdmin}`),
    deleteGroupe: call(`groupe/${types.deleteGroupe}`),
    restoreGroupe: call(`groupe/${types.restoreGroupe}`),
    setGroupe: call(`groupe/${types.setGroupe}`),
    getUserFriends: call(`user/${types.getUserFriends}`)
  }
};
</script>

<style lang="css">
</style>
