<template>
  <v-navigation-drawer :clipped="$vuetify.breakpoint.lgAndUp" app>
    <v-list-item link @click="getUserFriends()" @mousedown.stop @touchstart.native.stop>
      <v-list-item-icon>
        <v-icon>{{ "mdi-account-heart" }}</v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>{{ $t('chat_list.friends') }}</v-list-item-title>
      </v-list-item-content>
    </v-list-item>

    <v-divider></v-divider>
    <v-list dense>
      <v-subheader>
        {{ $t('chat_list.discussions') }}
        <v-spacer />
        <new-group-dialog />
      </v-subheader>
      <v-list-item-group>
        <v-list-item v-for="groupe in groupes" :key="groupe.id" @click="setGroupe(groupe)">
          <v-list-item-content>
            <v-list-item-title>{{ groupe.name }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
import { sync, call } from "vuex-pathify";
import * as types from "@/store/types.js";
// import { groupe } from "@/store/modules/groupe";
import { interfaceControl } from "@/store/modules/interfaceControl";
import RegisterStoreModule from "@/mixins/RegisterStoreModule";
import NewGroupDialog from "@/components/NewGroupDialog";
export default {
  components: { NewGroupDialog },
  mixins: [RegisterStoreModule],
  data() {
    return {};
  },
  created() {
    // this.registerStoreModule("groupe", groupe);
    this.registerStoreModule("interfaceControl", interfaceControl);
    this.getGroupes();
    console.log("bonjour");
    console.log(this.$router);
    // store.dispatch("groupe/setGroupe", { id : to.params.groupId}, {root: true})
    this.getUsers();
  },
  watch: {
    groupe() {
      if (this.groupe.id != undefined) {
        this.$router.push(`/chat/group/${this.groupe.id}`);
        this.chooseGroup(this.groupe.id);
      }
    }
  },
  computed: {
    currentGroup: sync("groupe/groupe"),
    groupes: sync("groupe/groupeList"),
    groupe: sync("groupe/groupe")
  },
  methods: {
    activateNewGroupDialog: call(`interfaceControl/${types.setGroupDialog}`),
    getGroupes: call(`groupe/${types.getGroupes}`),
    getUsers: call(`user/${types.getUsers}`),
    createGroupe: call(`groupe/${types.createGroupe}`),
    chooseGroup: call(`groupe/${types.chooseGroup}`),
    setGroupe: call(`groupe/${types.setGroupe}`),
    getUserFriends: call(`user/${types.getUserFriends}`)
  },
  beforeRouterEnter(from, to, next) {
    console.log("zdsdqs");
    next(vm => {
      console.Log("qfqs");
      vm.adaptSidebar(to.path);
    });
  }
};
</script>

<style lang="css">
</style>
