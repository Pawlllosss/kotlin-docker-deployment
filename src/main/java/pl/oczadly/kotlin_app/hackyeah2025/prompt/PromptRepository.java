package pl.oczadly.kotlin_app.hackyeah2025.prompt;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PromptRepository {

    private final DynamoDbTable<Prompt> table;

    public PromptRepository(DynamoDbEnhancedClient dbClient) {
        this.table = dbClient.table("prompts", TableSchema.fromBean(Prompt.class));
    }

    public Prompt save(Prompt prompt) {
        table.putItem(prompt);
        return prompt;
    }

    public List<Prompt> findAll() {
        return table.scan().items().stream().collect(Collectors.toList());
    }
}
