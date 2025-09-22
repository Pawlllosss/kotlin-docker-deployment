package pl.oczadly.kotlin_app.hackyeah2025.configuration.dynamo

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws.dynamodb")
data class DynamoDBProperties(
    val region: String = "us-east-1",
    val endpoint: String? = null
)
