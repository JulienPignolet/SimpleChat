export class User {
    constructor (username, token, id) {
      localStorage.username = username;
      localStorage.token = token;
      localStorage.id = id;
      this.username = username;
      this.token = token;
      this.id = id;
    }

    isStillConnected() {
      return this.token != null && (Date.now() > JSON.parse(atob(this.token.split('.')[1]))['exp']);
    }
}
