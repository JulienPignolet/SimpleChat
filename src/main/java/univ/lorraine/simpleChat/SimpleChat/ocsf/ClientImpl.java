/**
 * Représente un groupe de discussion
 */

package univ.lorraine.simpleChat.SimpleChat.ocsf;

import com.lloseng.ocsf.client.AbstractClient;

import java.util.ArrayList;
import java.util.List;

class ClientImpl extends AbstractClient {

    private Long id;
    private List<UserBuffer> users;
    
    ClientImpl(Long id, String host, int port) {
        super(host, port);
        setId(id);
        setUsers(new ArrayList<UserBuffer>());
    }
    
    public Long getId() {
		return id;
	}
    
	public void setId(Long id) {
		this.id = id;
	}

	public List<UserBuffer> getUsers() {
		return users;
	}

	public void setUsers(List<UserBuffer> users) {
		this.users = users;
	}
	
	/**
	 * Ajoute un utilisateur au groupe
	 * @param user_id
	 */
	public void addUserToGroup(long user_id) {
		users.add(new UserBuffer(user_id));
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
        for (UserBuffer user : users) {
    		user.addMessageToBuffer(new Message((String) msg));
    	}
    }
    
    /**
     * Retourne le buffer d'un utilisateur
     * @param user_id
     * @return String
     * @throws AutorisationException
     */
    public String getBufferById(long user_id) throws AutorisationException {
    	for(UserBuffer user: users)
    		if(user.getId() == user_id)
    			return user.getMsgBuffer();
    	throw new AutorisationException(this.id, user_id);
    }

    /**
     * vider le buffer d'un utilisateur
     * @param user_id id de l utilisateur
     * @return la suppression est effectué
     * @throws AutorisationException si user n´est pas identifié
     */
    public void viderBuffer(long user_id) throws AutorisationException {
        for(UserBuffer user: users)

            if(user.getId() == user_id) {
                user.clearBufferFromMessages();
                return;
            }
        throw new AutorisationException(this.id, user_id);
    }
}
