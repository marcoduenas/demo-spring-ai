package com.example.demo_spring_ai;


import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }



    @GetMapping("/jokes")
    public String generate(@RequestParam(value = "message",defaultValue = "tell me a dad joke") String message){
        /*ChatClient.Builder builder;
        ChatClient chatClient = builder.build();*/
        return chatClient.call(message);
    }
}
