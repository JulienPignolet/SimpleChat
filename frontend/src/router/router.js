import Vue from "vue";
import Router from "vue-router";
// import Home from "@/views/Home.vue";
// import Chat from "@/views/Chat.vue";
import ChatList from "@/components/ChatList";
import ChatBox from "@/components/ChatBox";
 import ChatMessages from "@/components/ChatMessages.vue"
 import Home from "@/components/Home"
// import Admin from "@/views/Admin.vue"
import Register from "@/components/RegisterForm.vue";
import FriendList from '@/components/FriendList'
//import ChatList from '@/components/ChatList'
// import UserList from '@/components/Admin/UserList'
import store from "../store/index";
//import groupe from "../store/modules/groupe"
// import auth from "./middleware/auth";
import LoginForm from "@/components/LoginForm"


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
    },
    {
      path: "/chat/group/:groupId",
      components: {
        default: ChatList,
        mainView: ChatMessages,
        chatBox: ChatBox
      },
    },
    {
      path: "/chat/friends",
      components: {
        default: ChatList,
        mainView: FriendList

      },
      

      // children: [
      //   {
      //     path: "group/:groupId",
      //     components: {
      //       mainView: ChatMessages
      //     }
      //   },
      //   {
      //     path: "friends",
      //     name: "friends",
      //     components: {
      //       mainView: FriendList
      //     },
      //     meta: {
      //       middleware: [auth]
      //     }
      //   }
      // ]
    },
    // {
    //   path: "/admin",
    //   name: "admin",
    //   component: Admin,
    //   children: [
    //     {
    //       path: 'users',
    //       name: "users",
    //       component: UserList,
    //       meta: {
    //         middleware: [auth]
    //       }
    //     },
    //     {
    //       path: "group/:groupId",
    //       name: "groupe",
    //       component: Chat,
    //       meta: {
    //         middleware: [auth]
    //       }
    //     },
    //   ],
    // },
    // {
    //   path: "/chat",
    //   name: "chat",
    //   component: Chat,
    //   children: [
    //     {
    //       path: 'friends',
    //       name: "friends",
    //       component: FriendList,
    //       meta: {
    //         middleware: [auth]
    //       }
    //     },
    //     {
    //       path: "group/:groupId",
    //       name: "groupe",
    //       component: ChatMessages,
    //       // beforeEnter: (to, from, next) => {

    //       //   if(from.params.groupId === undefined && from.name != 'chat'){     
    //       //     console.log(store) 
    //       //     console.log(to.params.groupId)
    //       //     store.dispatch("groupe/setGroupe", { id : to.params.groupId}, {root: true})  
    //       //     // store.dispatch('groupe/chooseGroupe', to.params.groupId, {root: true})       
    //       //     // next(to.path)
    //       //   }else{  
    //       //     next()
    //       //   }           
    //       // },
    //       meta: {
    //         middleware: [auth]
    //       }
    //     },
    //   ],
    //   meta: {
    //     middleware: [auth]
    //   }
    // },
    // {
    //   path: "/chat/group/:groupId",
    //   component: Chat,
    //   meta: {
    //     middleware: [auth]
    //   }
    // },
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