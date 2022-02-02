package team6;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBInput;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import team6.domain.Rating;
import team6.service.RatingService;
import team6.service.RatingServiceImpl;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import javax.sound.sampled.SourceDataLine;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it
     * using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */

    @FunctionName("GetRating")
    public HttpResponseMessage getRating(
            @HttpTrigger(name = "ratingId", methods = {
                    HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
                    @CosmosDBInput(name = "ratingInput",
                        databaseName = "ratings-db",
                        collectionName = "ratings-container",
                        id = "{Query.ratingId}",
                        partitionKey = "{Query.ratingId}",
                        connectionStringSetting = "CosmosDbConnectionString") Optional<Rating> ratingFromDb,
            final ExecutionContext context) {
        context.getLogger().info("GetRating API Request");

        final String ratingId = request.getQueryParameters().get("ratingId");
        context.getLogger().info("Received GetRating API Request for ratingId " + ratingId);

        if (ratingId == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Require parameter ratingId").build();
        }

        if (ratingFromDb.isPresent()) {
            return request.createResponseBuilder(HttpStatus.OK)
                    .body(ratingFromDb.get())
                    .header("Content-Type", "application/json")
                    .build();
        } else {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("No rating with id " + ratingId)
                    .header("Content-Type", "application/json")
                    .build();
        }
    }

    @FunctionName("CreateRating")
    public HttpResponseMessage create(
            @HttpTrigger(
                name = "createRating",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
                @CosmosDBOutput(name = "ratingOutput",
                        databaseName = "ratings-db",
                        collectionName = "ratings-container",
                        connectionStringSetting = "CosmosDbConnectionString")
                        OutputBinding<Rating> outputRatingBinding,
            final ExecutionContext context) throws JsonMappingException, JsonProcessingException {
                
        context.getLogger().info("Create Rating HTTP trigger processed a request.");
        
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<String> ratingOptional = request.getBody();
        if(!ratingOptional.isPresent()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        }
        
        Rating rating = objectMapper.readValue(ratingOptional.get(), Rating.class);
        rating.setId(UUID.randomUUID().toString());
        rating.setTimestamp(Calendar.getInstance().getTime().toString());
        outputRatingBinding.setValue(rating);

        return request.createResponseBuilder(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body(rating)
            .build();
    }
}