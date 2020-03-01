import Vue from "vue";
import Router from "vue-router";
import Home from "@/views/Home.vue";
import Chat from "@/views/Chat.vue";
import Admin from "@/views/Admin.vue"
import Register from "@/components/RegisterForm.vue";
import FriendList from '@/components/FriendList'
import UserList from '@/components/Admin/UserList'
import store from "../store/index";
import auth from "./middleware/auth";

Vue.use(Router);

const router = new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      redirect: '/login'
    },
    {
      path: "/admin",
      name: "admin",
      component: Admin,
      children: [
        {
          path: 'users',
          name: "users",
          component: UserList,
          meta: {
            middleware: [auth]
          }
        },
        {
          path: "group/:groupId",
          name: "groupe",
          component: Chat,
          meta: {
            middleware: [auth]
          }
        },
      ],
    },
    {
      path: "/chat",
      name: "chat",
      component: Chat,
      children: [
        {
          path: 'friends',
          name: "friends",
          component: FriendList,
          meta: {
            middleware: [auth]
          }
        },
        {
          path: "group/:groupId",
          name: "groupe",
          component: Chat,
          meta: {
            middleware: [auth]
          }
        },
      ],
      meta: {
        middleware: [auth]
      }
    },
    // {
    //   path: "/chat/group/:groupId",
    //   component: Chat,
    //   meta: {
    //     middleware: [auth]
    //   }
    // },
    {
      path: "/register",
      name: "register",
      component: Register
    },
    {
      path: "/login",
      name: "home",
      component: Home
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