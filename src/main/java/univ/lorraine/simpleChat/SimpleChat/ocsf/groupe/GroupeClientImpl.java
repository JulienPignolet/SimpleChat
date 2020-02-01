package univ.lorraine.simpleChat.SimpleChat.ocsf.groupe;

import com.lloseng.ocsf.client.ObservableClient;
import univ.lorraine.simpleChat.SimpleChat.ocsf.AutorisationException;
import univ.lorraine.simpleChat.SimpleChat.ocsf.MessageOCSF;
import univ.lorraine.simpleChat.SimpleChat.ocsf.UserBuffer;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 * Représente un groupe de discussion
 */
class GroupeClientImpl extends ObservableClient {
	
    private Long id;
    private Map<Long, UserBuffer> users;

    GroupeClientImpl(Long id, String host, int port) {
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
	 * @param userId
	 */
	public void addUserToGroup(long userId) {
	    if(!users.containsKey(userId))
		    users.put(userId, new UserBuffer(userId));
	}

	protected void connectionClosed() {
        System.out.println("Client: Closed");
        System.out.println("Client.isConnected()="+isConnected());
    }

    protected void connectionException(Exception exception) {
        exception.printStackTrace();
    }

    protected void connectionEstablished() {
        System.out.println("Client " + id +" : Connected");
//        System.out.println("Client.isConnected()="+isConnected());
    }

    @Override
    protected void handleMessageFromServer(Object msg){
        System.out.println("Client " + id + ": Message received = " + msg);
        try (Jsonb jsonb = JsonbBuilder.create()) {
            MessageOCSF messageOCSF = jsonb.fromJson((String) msg, MessageOCSF.class);
            if (messageOCSF.getGroupId().equals(id)) {
                for (Map.Entry<Long, UserBuffer> user : users.entrySet()) {
                    user.getValue().addMessageToBuffer(messageOCSF);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getBufferById(long userId) throws AutorisationException {
	    if(users.containsKey(userId))
	        return users.get(userId).getMsgBuffer();
	    throw new AutorisationException(this.id, userId);
    }


    public void viderBuffer(long userId) throws AutorisationException {
        if(users.containsKey(userId))
            users.get(userId).clearBufferFromMessages();
        else
            throw new AutorisationException(this.id, userId);
    }
}
