package pl.oczadly.kotlin_app.hackyeah2025.prompt

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import java.util.UUID

@DynamoDbBean
data class Prompt(
    @get:DynamoDbPartitionKey
    var id: UUID = UUID.randomUUID(),
    var prompt: String = "",
    var response: String = "",
)
