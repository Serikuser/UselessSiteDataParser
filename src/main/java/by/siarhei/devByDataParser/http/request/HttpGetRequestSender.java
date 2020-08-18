package by.siarhei.devByDataParser.http.request;

import by.siarhei.devByDataParser.entity.ParsedCompany;
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
    private static final String SENT_MESSAGE = "Request №[%s] on URL %s was sent";
    private static final int ONE_MINUTE_MS = 60000;
    private static final int STANDARD_SENT_LIMIT = 150;

    private final int currentPauseTimeMs;
    private final int requestSentLimit;
    private final HttpClient client;

    public HttpGetRequestSender() {
        this(ONE_MINUTE_MS * 2, STANDARD_SENT_LIMIT);
    }

    public HttpGetRequestSender(int pauseTimeInMS, int requestSentLimit) {
        this.currentPauseTimeMs = pauseTimeInMS;
        this.requestSentLimit = requestSentLimit;
        client = HttpClient.newHttpClient();
    }

    public String getHTMLBodyFromResponse(String requestURL) throws RequestSendException {
        try {
            return getResponseByURL(requestURL);
        } catch (IOException | InterruptedException e) {
            throw new RequestSendException(ERROR_MESSAGE + e.getMessage());
        }
    }

    public List<String> getHTMLBodyFromResponse(List<String> URLList) throws RequestSendException {
        List<String> bodyList = new ArrayList<>();
        int count = 1;
        for (String URL : URLList) {
            try {
                if (count == requestSentLimit) {
                    count = 1;
                    pause();
                }
                String body = getResponseByURL(URL);
                bodyList.add(body);
                logger.info(String.format(SENT_MESSAGE, count, URL));
                count++;
            } catch (IOException | InterruptedException e) {
                throw new RequestSendException(ERROR_MESSAGE + e.getMessage());
            }
        }
        return bodyList;
    }

    public void fillBodyInParsedCompanyList(List<ParsedCompany> parsedCompanyList) throws RequestSendException {
        int count = 1;
        for (ParsedCompany parsedCompany : parsedCompanyList) {
            try {
                if (count == requestSentLimit) {
                    count = 1;
                    pause();
                }
                String URL = parsedCompany.getUrl();
                String body = getResponseByURL(URL);
                parsedCompany.setHtmlComponentBody(body);
                logger.info(String.format(SENT_MESSAGE, count, URL));
                count++;
            } catch (IOException | InterruptedException e) {
                throw new RequestSendException(ERROR_MESSAGE + e.getMessage());
            }
        }
    }

    private void pause() throws InterruptedException {
        logger.info(String.format("Sender takes a break for %s min", currentPauseTimeMs / ONE_MINUTE_MS));
        Thread.sleep(currentPauseTimeMs);
    }

    private String getResponseByURL(String URL) throws IOException, InterruptedException {
        HttpRequest request = formRequest(URL);
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private HttpRequest formRequest(String URL) {
        return HttpRequest.newBuilder().uri(URI.create(URL)).build();
    }
}



