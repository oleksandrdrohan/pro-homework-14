package academy.prog.chatserversprint.controler;

import academy.prog.chatserversprint.model.FileDTO;
import academy.prog.chatserversprint.model.MessageDTO;
import academy.prog.chatserversprint.model.UserDTO;
import academy.prog.chatserversprint.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("add")
    public ResponseEntity<Void> add(@RequestBody MessageDTO messageDTO) {
        messageService.add(messageDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("get")
    public List<MessageDTO> get(
            @RequestParam(required = false, defaultValue = "0") Long from)
    {
        return messageService.get(from);
    }

    @GetMapping("get/private")
    public List<MessageDTO> getPrivate(
            @RequestParam(required = false) String to)
    {
        return messageService.getPrivate(to);
    }

    @GetMapping("get/online")
    public List<UserDTO> getOnlineUsers() {
        return messageService.getOnline();
    }

    @GetMapping("file")
    public List<FileDTO> file(@RequestParam Long... messageId) throws UnsupportedEncodingException {
        Long[] ids = messageId;
        return messageService.findFile(ids);
    }

    @GetMapping("test")
    public String test() {
        return "It works!";
    }
}
