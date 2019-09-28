package cn.huangzijian888.websocket.controller;

import cn.huangzijian888.websocket.entity.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author huangzijian888
 */
@Controller
public class MessageController {
    @MessageMapping("/message")
    @SendTo("/topic/message")
    public Message message(Message message) {
        return message;
    }
}
