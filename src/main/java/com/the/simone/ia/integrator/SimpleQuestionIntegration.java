package com.the.simone.ia.integrator;

import com.the.simone.ia.model.response.SimpleQuestionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class SimpleQuestionIntegration {


    private final ChatClient chatClient;
    private String systemDefault = """
            You are a helpful AI assistant that helps people find information and help people resolve problem. Your name is Miss Monday
            In your first response, greet the user,present ur self with ur name and quick summary of answer and then do not repeat it. 
            Next, you should reply to the user's request. 
            Finish with thanking the user for asking question in the end.
            """;


    SimpleQuestionIntegration(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor())
                .build();
    }


    public SimpleQuestionResponse simpleRequestIntegration(String userMsg, String systemMsg){
        log.info("SimpleRequestIntegration service started");

        var systemMsgDef = (ObjectUtils.isEmpty(systemMsg)) ? systemDefault : systemMsg;

        var systemMessage = new SystemPromptTemplate(systemMsgDef).createMessage();

        var userRequest = new PromptTemplate(userMsg).createMessage();
        // setto prompt
        var prompt = new Prompt(List.of(systemMessage,userRequest));
        // chiamata a llm, non ricorda conversazione avvenute, va implementata rag con vectore store
        var resp = chatClient.prompt(prompt)
                .call().entity(SimpleQuestionResponse.class);
        // formatta correttamente nel json
        log.info("AmazingAiTest service ended successfully");
        return resp;
    }
    }
}
