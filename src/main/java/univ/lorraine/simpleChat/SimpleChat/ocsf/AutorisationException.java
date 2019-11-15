/**
 * EXCEPTION : Envoyée lorsqu'un utilisateur tente de récupérer un message d'un groupe dans lequel il n'est pas.
 */

package univ.lorraine.simpleChat.SimpleChat.ocsf;

public class AutorisationException extends Exception {

	public AutorisationException(long group_id, long user_id) {
		super("L'utilisateur " + user_id + " n'est pas autorisé à consulter le groupe " + group_id + ".");
	}

}
