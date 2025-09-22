package pl.oczadly.kotlin_app.hackyeah2025.prompt

import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository
class PromptRepository(private val dbClient: DynamoDbEnhancedClient) {

    private val table = dbClient.table("prompts", TableSchema.fromBean(Prompt::class.java))

    fun save(prompt: Prompt): Prompt {
        table.putItem(prompt)
        return prompt
    }

    fun findAll(): List<Prompt> {
        return table.scan().items().toList()
    }
}