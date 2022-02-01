package team6;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import team6.domain.Rating;
import team6.service.RatingService;
import team6.service.RatingServiceImpl;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }

    @FunctionName("GetRating")
    public HttpResponseMessage getRating(
            @HttpTrigger(
                name = "ratingId",
                methods = {HttpMethod.GET},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        RatingService ratingService = new RatingServiceImpl();
        
        // Parse query parameter
        final String ratingId = request.getQueryParameters().get("ratingId");

        if (ratingId == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Require parameter ratingId").build();
        }
        Rating rating = ratingService.getRatingById(ratingId);
        if(rating!=null){
            return request.createResponseBuilder(HttpStatus.OK)
            .body(rating)
            .header("Content-Type", "application/json")
            .build();
        }else{
            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
            .body("No rating with id " + ratingId)
            .header("Content-Type", "application/json")
            .build();
        }
    }
}
