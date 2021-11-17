package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler for requests to Lambda function.
 */
@Slf4j
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        System.out.println("This is a new version of the function.");

        log.trace("This is a TRACE message.");
        log.debug("This is a DEBUG message.");
        log.info("This is a INFO message.");
        log.warn("This is a WARN message.");
        log.error("This is a ERROR message.");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {
            log.info("before calling checkip");
            Subsegment subsegment= AWSXRay.beginSubsegment("#Calling external service.");
            final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
            AWSXRay.endSubsegment();
            String output = String.format("{ \"message\": \"hello world\", \"location\": \"%s\" }", pageContents);
            log.info("after calling checkip");
            return response
                    .withStatusCode(200)
                    .withBody(output);
        } catch (IOException e) {
            log.error("error while calling checkip", e);
            return response
                    .withBody("{}")
                    .withStatusCode(500);
        }
    }

    private String getPageContents(String address) throws IOException{
        URL url = new URL(address);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
