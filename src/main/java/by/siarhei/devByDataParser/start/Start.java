package by.siarhei.devByDataParser.start;

import by.siarhei.devByDataParser.http.exception.RequestSendException;
import by.siarhei.devByDataParser.http.request.HttpGetRequestSender;
import by.siarhei.devByDataParser.parser.ResponseParser;
import by.siarhei.devByDataParser.reader.InputDataReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Start {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        InputDataReader reader = new InputDataReader();
        List<String> urlList = reader.readData();
        HttpGetRequestSender requestSender = new HttpGetRequestSender();
        ResponseParser parser = new ResponseParser();
        List<String> bodes = new ArrayList<>();
        try {
            bodes = requestSender.getHTMLBodyFromResponse(urlList);
        } catch (InterruptedException | RequestSendException e) {
            logger.error("Something went wrong Error message: " + e.getMessage());
        }
        if (!bodes.isEmpty()) {
            int total = 0;
            for (String body : bodes) {
                total = total + parser.parseEmployeeNumberFromHTMLResponseBody(body);
            }
            logger.info(String.format("Total employee in Belarus: ~%s",total));
        }
    }
}
