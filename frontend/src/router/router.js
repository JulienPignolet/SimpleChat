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


//import store from "@/store/index";
import auth from "@/router/middleware/auth";
import admin from "@/router/middleware/admin";

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
      path: "/admin",
      components: {
        default: AdminChatList,
        mainView: Home,
      },
      meta: {
        middleware: [auth, admin]
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
        middleware: [auth, admin]
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
        middleware: [auth, admin]
      }
    }
  ]
});

// Creates a `nextMiddleware()` function which not only
// runs the default `next()` callback but also triggers
// the subsequent Middleware function.
function nextFactory(context, middleware, index) {
  const subsequentMiddleware = middleware[index];
  // If no subsequent Middleware exists,
  // the default `next()` callback is returned.
  if (!subsequentMiddleware) return context.next;

  return (...parameters) => {
    // Run the default Vue Router `next()` callback first.
    context.next(...parameters);
    // Then run the subsequent Middleware with a new
    // `nextMiddleware()` callback.
    const nextMiddleware = nextFactory(context, middleware, index + 1);
    subsequentMiddleware({ ...context, next: nextMiddleware });
  };
}

router.beforeEach((to, from, next) => {
  if (to.meta.middleware) {
    const middleware = Array.isArray(to.meta.middleware)
      ? to.meta.middleware
      : [to.meta.middleware];

    const context = {
      from,
      next,
      router,
      to,
    };
    const nextMiddleware = nextFactory(context, middleware, 1);

    return middleware[0]({ ...context, next: nextMiddleware });
  }

  return next();
});

export default router