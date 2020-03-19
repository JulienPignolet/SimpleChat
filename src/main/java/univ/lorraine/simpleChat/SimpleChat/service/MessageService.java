package univ.lorraine.simpleChat.SimpleChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.Message;
import univ.lorraine.simpleChat.SimpleChat.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    private GroupeService groupeService;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    public void save(Message message) {
        messageRepository.save(message);
    }

    public Message find(Long messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    public void manage(Message message, boolean active) {
        message.setActive(active);
        messageRepository.save(message);
    }

    public String get(Long groupeId)
    {
        List<Message> messages = new ArrayList<>(messageRepository.get(groupeService.find(groupeId)));
        if(messages.isEmpty())
            return "{\"buffer\":[]}";

        StringBuilder json = new StringBuilder("{ \"buffer\":[");
        for(int i = 0; i < messages.size()-1; i++)
            json.append(messages.get(i).toJSON()).append(",");
        json.append(messages.get(messages.size()-1).toJSON());
        return json.append("]}").toString();
    }

    public String getActive(Long groupeId)
    {
        List<Message> messages = new ArrayList<>(messageRepository.findByGroupeIdAndActiveIsTrue(groupeId));
        if(messages.isEmpty())
            return "{\"buffer\":[]}";

        StringBuilder json = new StringBuilder("{ \"buffer\":[");
        for(int i = 0; i < messages.size()-1; i++)
            json.append(messages.get(i).toJSON()).append(",");
        json.append(messages.get(messages.size()-1).toJSON());
        return json.append("]}").toString();
    }

    public List<Message> getDrawpadMessages(Groupe groupe)
    {
        List<Message> messages = new ArrayList<>(messageRepository.getAllDrawpadMessages(groupe));
        return messages;
    }

    public void deleteInDatabase(Message message)
    {
        this.messageRepository.delete(message);
    }

}
