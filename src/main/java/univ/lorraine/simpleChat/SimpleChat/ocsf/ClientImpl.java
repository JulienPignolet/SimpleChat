/**
 * Représente un groupe de discussion
 */

package univ.lorraine.simpleChat.SimpleChat.ocsf;

import com.lloseng.ocsf.client.AbstractClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ClientImpl extends AbstractClient {

    private Long id;
    private Map<Long, UserBuffer> users;
    
    ClientImpl(Long id, String host, int port) {
        super(host, port);
        setId(id);
        setUsers(new HashMap<>());
    }
    
    public Long getId() {
		return id;
	}
    
	public void setId(Long id) {
		this.id = id;
	}

	public Map<Long, UserBuffer> getUsers() {
		return users;
	}

	public void setUsers(Map<Long, UserBuffer> users) {
		this.users = users;
	}
	
	/**
	 * Ajoute un utilisateur au groupe
	 * @param user_id
	 */
	public void addUserToGroup(long user_id) {
		users.put(user_id, new UserBuffer(user_id));
	}

	protected void connectionClosed() {
        System.out.println("Client: Closed");
        System.out.println("Client.isConnected()="+isConnected());
    }

    protected void connectionException(Exception exception) {
        System.out.println("Client exception: " + exception);
    }

    protected void connectionEstablished() {
        System.out.println("Client: Connected");
        System.out.println("Client.isConnected()="+isConnected());
    }

    protected void handleMessageFromServer(Object msg) {
        System.out.println("Client: Message received = " + msg);
        for (Map.Entry<Long, UserBuffer> user : users.entrySet()) {
    		user.getValue().addMessageToBuffer(new Message((String) msg));
    	}
    }
    
    /**
     * Retourne le buffer d'un utilisateur
     * @param user_id
     * @return String
     * @throws AutorisationException
     */
    public String getBufferById(long user_id) throws AutorisationException {
    if(users.containsKey(user_id))
        return users.get(user_id).getMsgBuffer();
    throw new AutorisationException(this.id, user_id);
    }

    /**
     * vider le buffer d'un utilisateur
     * @param user_id id de l utilisateur
     * @return la suppression est effectué
     * @throws AutorisationException si user n´est pas identifié
     */
    public void viderBuffer(long user_id) throws AutorisationException {
        if(users.containsKey(user_id))
            users.get(user_id).clearBufferFromMessages();
        else
            throw new AutorisationException(this.id, user_id);
    }
}
