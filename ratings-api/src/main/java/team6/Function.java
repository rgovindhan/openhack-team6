package team6;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

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
            @HttpTrigger(name = "ratingId", methods = {
                    HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("GetRating API Request");

        final String ratingId = request.getQueryParameters().get("ratingId");
        context.getLogger().info("Received GetRating API Request for ratingId " + ratingId);

        RatingService ratingService = new RatingServiceImpl();

        if (ratingId == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Require parameter ratingId").build();
        }

        Rating rating = ratingService.getRatingById(ratingId);
        if (rating != null) {
            return request.createResponseBuilder(HttpStatus.OK)
                    .body(rating)
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
            final ExecutionContext context) {

                /*
                CreateRating
Verb: POST
Input payload example:
{
  "userId": "cc20a6fb-a91f-4192-874d-132493685376",
  "productId": "4c25613a-a3c2-4ef3-8e02-9c335eb23204",
  "locationName": "Sample ice cream shop",
  "rating": 5,
  "userNotes": "I love the subtle notes of orange in this ice cream!"
}
Requirements
Validate both userId and productId by calling the existing API endpoints. You can find a user id to test with from the sample payload above
Add a property called id with a GUID value
Add a property called timestamp with the current UTC date time
Validate that the rating field is an integer from 0 to 5
Use a data service to store the ratings information to the backend
Return the entire review JSON payload with the newly created id and timestamp
Output payload example:
{
  "id": "79c2779e-dd2e-43e8-803d-ecbebed8972c",
  "userId": "cc20a6fb-a91f-4192-874d-132493685376",
  "productId": "4c25613a-a3c2-4ef3-8e02-9c335eb23204",
  "timestamp": "2018-05-21 21:27:47Z",
  "locationName": "Sample ice cream shop",
  "rating": 5,
  "userNotes": "I love the subtle notes of orange in this ice cream!"
}
*/
        context.getLogger().info("Create Rating HTTP trigger processed a request.");

        final String userId = parseFromQueryOrBody(request, "userId");
        final String productId = parseFromQueryOrBody(request, "productId");
        final String locationName = parseFromQueryOrBody(request, "locationName");
        final String rating = parseFromQueryOrBody(request, "rating");
        final String userNotes = parseFromQueryOrBody(request, "userNotes");

        StringBuilder builder = new StringBuilder();
        builder.append("{").append('\n');
        builder.append("id: ").append("79c2779e-dd2e-43e8-803d-ecbebed8972c").append('\n');
        builder.append("userId: ").append(userId).append('\n');
        builder.append("productId: ").append(productId).append('\n');
        builder.append("timestamp: ").append("2018-05-21 21:27:47Z").append('\n');
        builder.append("locationName: ").append(locationName).append('\n');
        builder.append("rating: ").append(rating).append('\n');
        builder.append("userNotes: ").append(userNotes).append('\n');
        builder.append("}");

        if (userId == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).header("Content-Type", "application/json").body(builder.toString()).build();
        }
    }

    private String parseFromQueryOrBody(HttpRequestMessage<Optional<String>> request, String key) {
        // Parse query parameter
        final String query = request.getQueryParameters().get(key);
        final String value = request.getBody().orElse(query);
        return value;
    }

}
