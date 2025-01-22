package com.example.demo.chatclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DemoChatClientController {
    private final ChatClient chatClient;

    record ActorFilms(String actor, List<String> movies) {}

    public DemoChatClientController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }


    @GetMapping("/demo-chat-simple")
    public String demoChatSimple() {

        String userInput="Generate the filmography for a random actor.";

        return this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }


    @GetMapping("/demo-chat-entity")
    public ActorFilms  demoChatEntity(){

        ActorFilms actorFilms = chatClient.prompt()
                .user("Generate the filmography for a random actor.")
                .call()
                .entity(ActorFilms.class);
        return actorFilms;
    }

    @GetMapping("/demo-chat-list")
    public List<ActorFilms>  demoChatEntityList(){

        List<ActorFilms> actorFilms = chatClient.prompt()
                .user("Generate the filmography of 5 movies for Tom Hanks and Bill Murray.")
                .call()
                .entity(new ParameterizedTypeReference<List<ActorFilms>>() {});

        return actorFilms;

    }


    @GetMapping("/demo-chat-prompt")
    public String demoChatPromptTemplate(@RequestParam(value = "adjective", defaultValue = "funny") String adjective,
                                         @RequestParam(value = "topic", defaultValue = "programming") String topic) {

        PromptTemplate promptTemplate = new PromptTemplate("Tell me a {adjective} joke about {topic}");

        Prompt prompt = promptTemplate.create(Map.of("adjective", adjective, "topic", topic));

        return chatClient.prompt(prompt).call().content();
    }


    @GetMapping("/demo-chat-voice")
    Map<String, String> completion(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message,
                                   @RequestParam(value = "voice", defaultValue = "Robot")
                                   String voice) {
        return Map.of("completion",
                this.chatClient.prompt()
                        .system(sp -> sp.param("voice", voice))
                        .user(message)
                        .call()
                        .content());
    }


}
