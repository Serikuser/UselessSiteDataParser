package by.siarhei.devByDataParser.http.request;

import by.siarhei.devByDataParser.http.exception.RequestSendException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class HttpGetRequestSender {
    private static final Logger logger = LogManager.getLogger();

    private static final String ERROR_MESSAGE = "GET request cant be sent with message: ";
    private static final int ONE_MINUTE_MS = 60000;
    private static final int CURRENT_PAUSE_TIME_MS = ONE_MINUTE_MS;
    private static final String SENT_MESSAGE = "Request on URL %s was sent";

    public String getHTMLBodyFromResponse(String requestURL) throws RequestSendException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .build();
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RequestSendException(ERROR_MESSAGE + e.getMessage());
        }
    }

    public List<String> getHTMLBodyFromResponse(List<String> requestURLs) throws RequestSendException, InterruptedException {
        List<String> bodyList = new ArrayList<>();
        int count = 0;
        for (String URL : requestURLs) {
            count++;
            if(count == 300){
               pause();
            }
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .build();
            try {
                HttpResponse<String> response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());
                bodyList.add(response.body());
                logger.info(String.format(SENT_MESSAGE, URL));
            } catch (IOException | InterruptedException e) {
                throw new RequestSendException(ERROR_MESSAGE + e.getMessage());
            }

        }
        return bodyList;
    }

    private void pause() throws InterruptedException{
        logger.info(String.format("Sender takes a break for %s min", CURRENT_PAUSE_TIME_MS / ONE_MINUTE_MS));
        Thread.sleep(CURRENT_PAUSE_TIME_MS);
    }
}



