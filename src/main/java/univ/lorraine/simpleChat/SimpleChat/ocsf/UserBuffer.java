package univ.lorraine.simpleChat.SimpleChat.ocsf;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un utilisateur dans une discussion de groupe.
 */
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
	 * Retourne un JSON contenant les messages et vide le buffer
	 * @return JSON
	 */
	public String getMsgBuffer() {
		StringBuilder json = new StringBuilder("{ \"buffer\":[");
		for(int i = 0; i < msgBuffer.size()-1; i++)
			json.append(msgBuffer.get(i).toString()).append(",");
		json.append(msgBuffer.get(msgBuffer.size()-1).toString());
		msgBuffer.clear();
		return json.append("]}").toString();
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
	 */
	public void clearBufferFromMessages() { // /!\ NE SERT ACTUELLEMENT PAS
		msgBuffer.clear();
	}
}
