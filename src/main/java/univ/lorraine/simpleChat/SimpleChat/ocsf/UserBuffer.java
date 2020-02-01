package univ.lorraine.simpleChat.SimpleChat.ocsf;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un utilisateur dans une discussion de groupe.
 */
public class UserBuffer {
	
	private long id; 					// ID de l'utilisateur
	private List<MessageOCSF> msgBuffer;	// Liste des messages en attente d'envoie
	private boolean empty;


	public UserBuffer(long id) {
		setId(id);
		setMsgBuffer(new ArrayList<MessageOCSF>());
		setEmpty(true);
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public void setMsgBuffer(List<MessageOCSF> buffer) {
		this.msgBuffer = buffer;
	}

	/**
	 * Retourne un JSON contenant les messages et vide le buffer
	 * @return JSON
	 */
	public String getMsgBuffer() {
		if(msgBuffer.isEmpty())
			return "{\"buffer\":[]}";

		StringBuilder json = new StringBuilder("{ \"buffer\":[");
		try (Jsonb jsonb = JsonbBuilder.create()) {
			for (int i = 0; i < msgBuffer.size() - 1; i++)
				json.append(jsonb.toJson(msgBuffer.get(i))).append(",");
			json.append(jsonb.toJson(msgBuffer.get(msgBuffer.size() - 1)));
			msgBuffer.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.append("]}").toString();
	}
	
	/**
	 * Ajout d'un message au buffer
	 * @param msg
	 */
	public synchronized void addMessageToBuffer(MessageOCSF msg) {
		msgBuffer.add(msg);
		setEmpty(false);
		notify();
	}

	/**
	 * vider le buffer
	 */
	public void clearBufferFromMessages() { // /!\ NE SERT ACTUELLEMENT PAS
		msgBuffer.clear();
		setEmpty(true);
	}
}
