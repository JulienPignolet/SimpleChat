export class Sondage {
    constructor (id, question, status, dateDebut, dateFin, reponseSondages, groupe, initiateur, votesAnonymes) {
      this.id = id;
      this.question = question;
      this.status = status;
      this.dateDebut = dateDebut;
      this.dateFin = dateFin;
      this.reponseSondages = reponseSondages;
      this.groupe = groupe;
      this.initiateur = initiateur;
      this.votesAnonymes = votesAnonymes;
    }
  }