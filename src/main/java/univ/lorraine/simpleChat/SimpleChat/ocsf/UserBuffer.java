/**
 * Représente un utilisateur dans une discussion de groupe.
 */

package univ.lorraine.simpleChat.SimpleChat.ocsf;

import java.util.ArrayList;
import java.util.List;

public class UserBuffer {
	
	private long id; 					// ID de l'utilisateur
	private List<Message> msgBuffer;	// Liste des messages en attente d'envoie
	
	public UserBuffer(long id) {
		setId(id);
		setMsgBuffer(new ArrayList<Message>());
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

	public void setMsgBuffer(List<Message> buffer) {
		this.msgBuffer = buffer;
	}

	/**
	 * Retourne un JSON contenant les messages et VIDER LE BUFFER
	 * @return JSON
	 */
	public String getMsgBuffer() {
		StringBuilder json = new StringBuilder("{");
		for(Message msg: msgBuffer)
			json.append(msg.toString()).append(',');
		msgBuffer.clear();
		return json.append('}').toString();
	}
	
	/**
	 * Ajout d'un message au buffer
	 * @param msg
	 */
	public void addMessageToBuffer(Message msg) {
		msgBuffer.add(msg);
	}

	/**
	 * vider le buffer
	 *
	 */
	public void clearBufferFromMessages(){
		msgBuffer.clear();
	}
}
