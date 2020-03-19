<template>
  <v-menu
    v-model="menu"
    :close-on-content-click="false"
    :nudge-width="200"
    offset-x
  >
    <template v-slot:activator="{ on }">
      <v-list-item-icon style="margin-right: 10px;" v-on="on">
        <v-icon>mdi-account-circle-outline</v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>
          <div class="d-flex justify-space-between align-center">
            <span v-on="on">{{ user.username }}</span>
            <div class="d-flex align-center" style="margin-left: 40px;" v-if="currentUserIsAdmin && !isAdmin">
              <v-icon
                class="amber--text"
                @click="clickToAddAdmin">
                mdi-account-star
              </v-icon>
              <v-icon class="delete-member"
                      @click="clickToDelete"
                      style="margin-left: 10px;">
                mdi-trash-can-outline
              </v-icon>
            </div>
          </div>
        </v-list-item-title>
      </v-list-item-content>
    </template>

    <v-card style="min-width: 400px;">
      <v-list>
        <v-list-item>
          <v-list-item-icon style="margin-right: 10px;">
            <v-icon>mdi-account-circle-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ user.username }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>

      <v-divider/>

      <v-list class="pa-2">
        <v-list-item v-if="!isFriend">
          <v-btn
            block
            color="primary"
            class="white--text"
            @click="clickToAddFriend()"
          >
            <v-icon left>mdi-account-plus</v-icon>
            {{ $t('user.add_friend') }}
          </v-btn>
        </v-list-item>

        <v-list-item v-if="isFriend">
          <v-btn
            block
            color="red"
            class="white--text"
            @click="clickToRemoveFriend()"
          >
            <v-icon left>mdi-account-remove</v-icon>
            {{ $t('user.remove_friend') }}
          </v-btn>
        </v-list-item>

        <v-list-item v-if="!isBlock">
          <v-btn
            block
            color="red"
            class="white--text"
            @click="clickToBlockUser()"
          >
            <v-icon left>mdi-cancel</v-icon>
            {{ $t('user.block') }}
          </v-btn>
        </v-list-item>

        <v-list-item v-if="isBlock">
          <v-btn
            block
            color="primary"
            class="white--text"
            @click="clickToUnblockUser()"
          >
            <v-icon left>mdi-cancel</v-icon>
            {{ $t('user.unblock') }}
          </v-btn>
        </v-list-item>
        <v-list-item>
          <v-btn
            block
            color="primary"
            class="white--text"
            @click.stop="dialogAccessAccount = true"
          >
            <v-icon left>mdi-account-key</v-icon>
            Accès au compte
          </v-btn>
        </v-list-item>
      </v-list>

      <v-card-actions>
        <v-spacer/>
        <v-btn color="primary" text @click="menu = false">{{ $t('general.close') }}</v-btn>
      </v-card-actions>

      <v-dialog
        v-model="dialogAccessAccount"
        max-width="450"
      >
        <v-card>
          <v-card-title class="headline">Donner l'accès à son compte ?</v-card-title>

          <v-card-text>
            <v-form>
              <v-text-field
                id="password"
                :label="'nouveau mot de passe'"
                v-model="password"
                prepend-icon="mdi-lock"
                type="password"
              />
            </v-form>
          </v-card-text>

          <v-card-actions>
            <v-spacer></v-spacer>

            <v-btn
              color="red"
              text
              @click="dialogAccessAccount = false"
            >
              Annuler
            </v-btn>

            <v-btn
              color="primary"
              text
              @click="clickToGiveAccountAccess"
            >
              Continuer
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-card>
  </v-menu>
</template>

<script>
  import {call} from "vuex-pathify";
  import * as types from "@/store/types.js";

  export default {
    components: {},
    props: ['user', 'currentUserIsAdmin'],
    data: () => ({
      menu: false,
      dialogAccessAccount: false,
      password: ''
    }),
    computed: {
      isFriend () {
        let friendList = this.$store.state.groupe.groupeFriends;
        return friendList.filter((friend) => friend.id === this.user.id).length > 0;
      },

      isBlock () {
        let blockList = this.$store.state.groupe.groupeBlockUsers;
        return blockList.filter((block) => block.id === this.user.id).length > 0;
      },

      isAdmin() {
        let adminMembers = this.$store.state.groupe.groupeAdminUsers;
        return adminMembers.filter((admin) => admin.memberId === this.user.id.toString() && admin.adminGroup).length > 0;
      }
    },
    methods: {
      addFriend: call(`user/${types.addFriend}`),
      removeFriend: call(`user/${types.deleteFriend}`),
      blockUser: call(`user/${types.blockUser}`),
      unblockUser: call(`user/${types.unblockUser}`),
      deleteUser: call(`groupe/${types.groupeDeleteUser}`),
      addAdmin: call(`groupe/${types.groupeAddAmin}`),
      giveAccountAccess: call(`groupe/${types.groupeAddAmin}`),
      clickToAddFriend : function() {
        this.addFriend(this.user.id)
      },
      clickToRemoveFriend : function() {
        this.removeFriend(this.user.id)
      },
      clickToBlockUser : function() {
        this.blockUser(this.user.id)
      },
      clickToUnblockUser : function() {
        this.unblockUser(this.user.id)
      },
      clickToAddAdmin: function() {
        this.addAdmin(this.user.id)
      },
      clickToDelete: function() {
        this.deleteUser(this.user.id)
      },
      clickToGiveAccountAccess: function() {
        this.giveAccountAccess(this.password);
        this.dialogAccessAccount = false;
      }
    }
  }
</script>

<style lang="scss">
  .delete-member {
    transition: all 0.3s ease-in-out;
  }
  .delete-member:hover {
    color: red;
  }
</style>
