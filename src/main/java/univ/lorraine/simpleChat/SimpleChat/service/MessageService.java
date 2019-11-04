package univ.lorraine.simpleChat.SimpleChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univ.lorraine.simpleChat.SimpleChat.model.Message;
import univ.lorraine.simpleChat.SimpleChat.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;


    public void save(Message message) {
        messageRepository.save(message);
    }

}
