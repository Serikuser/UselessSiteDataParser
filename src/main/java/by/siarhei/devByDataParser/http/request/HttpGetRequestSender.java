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
    private static final int ONE_MINUTE_MS = 60000;
    private static final int REQUEST_SENT_LIMIT = 300;
    private static final int CURRENT_PAUSE_TIME_MS = ONE_MINUTE_MS;
    private static final String SENT_MESSAGE = "Request on URL %s was sent";
    private final HttpClient client;

    public HttpGetRequestSender() {
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
        for (String URL : URLList) {
            int count = 0;
            try {
                count++;
                if (count == REQUEST_SENT_LIMIT) {
                    pause();
                }
                String body = getResponseByURL(URL);
                bodyList.add(body);
                logger.info(String.format(SENT_MESSAGE, URL));
            } catch (IOException | InterruptedException e) {
                throw new RequestSendException(ERROR_MESSAGE + e.getMessage());
            }
        }
        return bodyList;
    }

    public void fillBodyInParsedCompanyList(List<ParsedCompany> parsedCompanyList) throws RequestSendException {
        for (ParsedCompany parsedCompany : parsedCompanyList) {
            int count = 0;
            try {
                count++;
                if (count == REQUEST_SENT_LIMIT) {
                    pause();
                }
                String URL = parsedCompany.getUrl();
                String body = getResponseByURL(URL);
                parsedCompany.setHtmlComponentBody(body);
                logger.info(String.format(SENT_MESSAGE, URL));
            } catch (IOException | InterruptedException e) {
                throw new RequestSendException(ERROR_MESSAGE + e.getMessage());
            }
        }
    }

    private void pause() throws InterruptedException {
        logger.info(String.format("Sender takes a break for %s min", CURRENT_PAUSE_TIME_MS / ONE_MINUTE_MS));
        Thread.sleep(CURRENT_PAUSE_TIME_MS);
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



