export default function auth ({ next }){
    if (localStorage.token === 'undefined' ) {
        return next({
            name: ''
         })
      }


    return next()
   }
