export default function admin ({ next }){
    if (JSON.parse(localStorage.getItem('roles')).some(e => e.name === "ROLE_SUPER_ADMIN")) {
        return next()
      }


    return next({ path: '/chat' })
   }
