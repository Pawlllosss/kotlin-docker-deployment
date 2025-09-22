package pl.oczadly.kotlin_app.hackyeah2025.configuration.dynamo

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.*
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

@Configuration
@EnableConfigurationProperties(DynamoDBProperties::class)
private class DynamoDBConfig(private val dynamoDBProperties: DynamoDBProperties) {


    @Bean
    fun amazonDynamoDB(): DynamoDbClient = DynamoDbClient.builder()
        .region(Region.of(dynamoDBProperties.region))
        .credentialsProvider(credentialsProvider())
        .build()

    @Bean
    fun credentialsProvider(): AwsCredentialsProvider {
        return AwsCredentialsProviderChain.of(
            // 1. Environment variables (local development)
            EnvironmentVariableCredentialsProvider.create(),

            // 2. System properties
            SystemPropertyCredentialsProvider.create(),

            // 3. Profile credentials (~/.aws/credentials)
            ProfileCredentialsProvider.create(),

            // 4. Instance profile (AWS EC2/ECS/Lambda)
            InstanceProfileCredentialsProvider.create(),

            // 5. Container credentials (ECS tasks)
            ContainerCredentialsProvider.builder().build(),

            // 6. Web identity token (EKS)
            WebIdentityTokenFileCredentialsProvider.create()
        )
    }

    @Bean
    fun dynamoDbEnhancedClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient {
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()
    }

}