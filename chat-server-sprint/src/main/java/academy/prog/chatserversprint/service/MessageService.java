package academy.prog.chatserversprint.service;

import academy.prog.chatserversprint.model.FileDTO;
import academy.prog.chatserversprint.model.Message;
import academy.prog.chatserversprint.model.MessageDTO;
import academy.prog.chatserversprint.model.UserDTO;
import academy.prog.chatserversprint.repo.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void add(MessageDTO messageDTO) {
        var message = Message.fromDTO(messageDTO);
        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> get(long id) {
        var messages = messageRepository.findNew(id);
        var result = new ArrayList<MessageDTO>();

        messages.forEach(message -> result.add(message.toDTO()));
        return result;
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> getPrivate(String to) {
        var messages = messageRepository.findPrivate(to);
        var result = new ArrayList<MessageDTO>();

        messages.forEach(message -> result.add(message.toDTO()));
        return result;
    }

    @Transactional(readOnly = true)
    public List<FileDTO> findFile(Long[] ids) throws UnsupportedEncodingException {

        var result = new ArrayList<FileDTO>();

        for (long id : ids){
            var message = messageRepository.findFile(id);
            var temp = new FileDTO();
            temp.setFileData(message.getFileData());
            temp.setFileName(message.getFileName());
            temp.setId(message.getId());
            byte[] fileData = Base64.getEncoder().encode(message.getFileData().getBytes("UTF-8"));
            temp.setFileDataByte(fileData);
            result.add(temp);
        }
        return result;
    }


    // При цій логіці вважається, що людина онлайн, якщо вона відправила повідомлення не пізніше 5 хвилин назад
    @Transactional(readOnly = true)
    public List<UserDTO> getOnline(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -5);
        Date date = calendar.getTime();

        var messages = messageRepository.findUsersOnline(date);
        var result = new ArrayList<UserDTO>();
        for (Message message: messages) {
            UserDTO userOnline = new UserDTO();
            userOnline.setUserName(message.getFrom());
            result.add(userOnline);
        }
        return result;
    }
}

