package pl.oczadly.kotlin_app.hackyeah2025.prompt;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

@DynamoDbBean
public class Prompt {

    private UUID id;
    private String prompt;
    private String response;

    public Prompt() {
        this.id = UUID.randomUUID();
        this.prompt = "";
        this.response = "";
    }

    public Prompt(UUID id, String prompt, String response) {
        this.id = id;
        this.prompt = prompt;
        this.response = response;
    }

    @DynamoDbPartitionKey
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
