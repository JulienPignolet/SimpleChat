import Vue from "vue";
import Router from "vue-router";

import LoginForm from "@/components/LoginForm"
import Register from "@/components/RegisterForm.vue";

import Home from "@/components/Home"

import ChatList from "@/components/ChatList";
import ChatBox from "@/components/ChatBox";
import ChatMessages from "@/components/ChatMessages.vue"
import FriendList from '@/components/FriendList'

import AdminChatList from "@/components/Admin/ChatList";
import AdminChatMessages from "@/components/Admin/ChatMessages"
import AdminUserList from "@/components/Admin/UserList"


import store from "@/store/index";
import auth from "@/router/middleware/auth";


Vue.use(Router);

const router = new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      redirect: '/login',
    },
    {
      path: "/login",
      components: {
        home: LoginForm
      }
    },
    {
      path: "/register",
      components: {
        home: Register
      }
    },
    {
      path: "/chat",
      components: {
        default: ChatList,
        mainView: Home,
      },
      meta: {
        middleware: [auth]
      }
    },
    {
      path: "/chat/group/:groupId",
      components: {
        default: ChatList,
        mainView: ChatMessages,
        chatBox: ChatBox
      },
      meta: {
        middleware: [auth]
      }
    },
    {
      path: "/admin/group/:groupId",
      components: {
        default: AdminChatList,
        mainView: AdminChatMessages,
      },
      meta: {
        middleware: [auth]
      }
    },
    {
      path: "/chat/friends",
      components: {
        default: ChatList,
        mainView: FriendList
      },
      meta: {
        middleware: [auth]
      },
    },
    {
      path: "/admin/users",
      components: {
        default: AdminChatList,
        mainView: AdminUserList
      },
      meta: {
        middleware: [auth]
      }
    }
  ]
});

router.beforeEach((to, from, next) => {
  if (!to.meta.middleware) {
    return next()
  }
  const middleware = to.meta.middleware

  const context = {
    to,
    from,
    next,
    store
  }
  return middleware[0]({
    ...context
  })
})

export default router