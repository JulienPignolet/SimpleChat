export class User {
    constructor (username, token, id) {
      localStorage.username = username;
      localStorage.token = token;
      localStorage.id = id;
      this.username = username;
      this.token = token;
      this.id = id;
      this.roles = JSON.parse(localStorage.getItem('roles'));
    }

    isStillConnected() {
      if(this.token != 'undefined'){
        return this.token != null && (Date.now() > JSON.parse(atob(this.token.split('.')[1]))['exp']);
      }
    }
}
