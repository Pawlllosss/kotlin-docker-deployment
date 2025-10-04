package pl.oczadly.kotlin_app.hackyeah2025.configuration.dynamo;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@EnableConfigurationProperties(DynamoDBProperties.class)
class DynamoDBConfig {

    private final DynamoDBProperties dynamoDBProperties;

    public DynamoDBConfig(DynamoDBProperties dynamoDBProperties) {
        this.dynamoDBProperties = dynamoDBProperties;
    }

    @Bean
    public DynamoDbClient amazonDynamoDB() {
        return DynamoDbClient.builder()
            .region(Region.of(dynamoDBProperties.getRegion()))
            .credentialsProvider(credentialsProvider())
            .build();
    }

    @Bean
    public AwsCredentialsProvider credentialsProvider() {
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
        );
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();
    }
}
