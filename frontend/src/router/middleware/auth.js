export default function auth ({ next, store }){
    if(!store.getters['user/user']){
        return next({
           name: ''
        })
    }

    return next()
   }
