package univ.lorraine.simpleChat.SimpleChat.ocsf.groupe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lloseng.ocsf.client.ObservableClient;
import univ.lorraine.simpleChat.SimpleChat.ocsf.AutorisationException;
import univ.lorraine.simpleChat.SimpleChat.ocsf.Message;
import univ.lorraine.simpleChat.SimpleChat.ocsf.UserBuffer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Représente un groupe de discussion
 */
class GroupeClientImpl extends ObservableClient {
	
    private Long id;
    RestTemplate restTemplate;
    private Map<Long, UserBuffer> users;
    final String URL_BLOCKLIST = "http://localhost:8080/blockList/";

    GroupeClientImpl(Long id, String host, int port) {
        super(host, port);
        setId(id);
        setUsers(new HashMap<>());
        this.restTemplate = new RestTemplate();
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
	    if(!users.containsKey(user_id))
		    users.put(user_id, new UserBuffer(user_id));
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

    protected void handleMessageFromServer(Object msg) {
        System.out.println("Client " + id + ": Message received = " + msg);
        Message message = new Message((String) msg);
        if(message.getGroup_id().equals(id)) {
            for (Map.Entry<Long, UserBuffer> user : users.entrySet()) {
            	try {
	            	// Vérifie si le membre du groupe a bloqué l'expéditeur
	            	String expeditor_id = user.getValue().getId() + "";
	            	ResponseEntity<String> response = this.restTemplate.getForEntity(this.URL_BLOCKLIST + expeditor_id, String.class);
	            	if(response.getStatusCode() == HttpStatus.OK) { // Si une blocklist existe
	            		JsonObject blocklist = new JsonParser().parse(response.getBody()).getAsJsonObject();
	            		if (!blocklist.has(expeditor_id)) // Si l'expéditeur n'est pas bloqué
	            			user.getValue().addMessageToBuffer(message);
	                }
	            	/*else
	            		user.getValue().addMessageToBuffer(message);*/
            	} catch (Exception e) {
            		user.getValue().addMessageToBuffer(message);
				}
            }
        }
    }
    
    public String getBufferById(long user_id) throws AutorisationException {
	    if(users.containsKey(user_id))
	        return users.get(user_id).getMsgBuffer();
	    throw new AutorisationException(this.id, user_id);
    }


    public void viderBuffer(long user_id) throws AutorisationException {
        if(users.containsKey(user_id))
            users.get(user_id).clearBufferFromMessages();
        else
            throw new AutorisationException(this.id, user_id);
    }
}
