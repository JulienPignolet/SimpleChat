export class Alerte {
    constructor (couleur, texte) {
      this.timeout = 2000
      this.activation = true
      this.couleur = couleur
      this.texte = texte
    }
  }