package by.siarhei.devByDataParser.start;

import by.siarhei.devByDataParser.entity.ParsedCompany;
import by.siarhei.devByDataParser.http.exception.RequestSendException;
import by.siarhei.devByDataParser.http.request.HttpGetRequestSender;
import by.siarhei.devByDataParser.parser.api.HtmlBodyDataParser;
import by.siarhei.devByDataParser.parser.impl.HtmlCompanyParser;
import by.siarhei.devByDataParser.parser.impl.HtmlResponseBodyEmployeeParser;
import by.siarhei.devByDataParser.validator.PauseTimeUnits;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static by.siarhei.devByDataParser.validator.PauseTimeUnits.MIN;
import static by.siarhei.devByDataParser.validator.PauseTimeUnits.valueOf;

public class DevByParsingInitializer {
    private static final Logger logger = LogManager.getLogger();

    private static final String DEV_BY_COMPANIES_URL = "https://companies.dev.by/";
    private static final int ONE_MINUTE_MS = 60000;
    private static final int STANDARD_SENT_LIMIT = 200;
    private static final int INDEX_FIRST = 0;
    private static final int INDEX_SECOND = 1;
    private static final int INDEX_THIRD = 2;

    private HttpGetRequestSender requestSender;
    private HtmlBodyDataParser<Integer> companyDataParser;
    private HtmlBodyDataParser<List<ParsedCompany>> companyListParser;
    private List<ParsedCompany> parsedCompanyList;
    private final int sentLimit;
    private final int pauseTime;

    public DevByParsingInitializer() {
        this.sentLimit = STANDARD_SENT_LIMIT;
        this.pauseTime = ONE_MINUTE_MS * 2;
        init();
    }

    public DevByParsingInitializer(String[] initArgs) {
        this.sentLimit = getSentLimitFromArgs(initArgs);
        this.pauseTime = getPauseTimeFromArgs(initArgs);
        init();
    }

    public DevByParsingInitializer(int requestSentLimit, int pauseTimeMs) {
        this.sentLimit = requestSentLimit;
        this.pauseTime = pauseTimeMs;
        init();
    }

    private void init(){
        this.requestSender = new HttpGetRequestSender(sentLimit, pauseTime);
        this.companyDataParser = new HtmlResponseBodyEmployeeParser();
        this.companyListParser = new HtmlCompanyParser();
    }

    public boolean parse() {
        boolean isParsed = false;
        try {
            String companyListBody = requestSender.getHTMLBodyFromResponse(DEV_BY_COMPANIES_URL);
            parsedCompanyList = companyListParser.parseDataFromResponseBody(companyListBody);
            if (!parsedCompanyList.isEmpty()) {
                fillNumberOfEmployeeParsedCompanyList();
                isParsed = true;
            }
        } catch (RequestSendException e) {
            logger.error(String.format("Something went wrong Error message: %s", e.getMessage()));
        }
        return isParsed;
    }

    public void printTotalNumberOfEmployeeInBelarus() {
        int total = 0;
        for (ParsedCompany parsedCompany : parsedCompanyList) {
            int numberOfEmployee = parsedCompany.getEmployeeInBelarusNumber();
            String companyName = parsedCompany.getName();
            total = total + numberOfEmployee;
            logger.info(String.format("Company name: %s, Number of employee in Belarus: %s", companyName, numberOfEmployee));
        }
        logger.info(String.format("Total employee in Belarus: ~%s in %s companies", total, parsedCompanyList.size()));
    }

    private void fillNumberOfEmployeeParsedCompanyList() throws RequestSendException {
        requestSender.fillBodyInParsedCompanyList(parsedCompanyList);
        for (ParsedCompany parsedCompany : parsedCompanyList) {
            String body = parsedCompany.getHtmlComponentBody();
            int numberOfEmployee = companyDataParser.parseDataFromResponseBody(body);
            parsedCompany.setEmployeeInBelarusNumber(numberOfEmployee);
        }
    }


    private int getSentLimitFromArgs(String[] initArgs) {
        return Integer.parseInt(initArgs[INDEX_FIRST]);
    }

    private int getPauseTimeFromArgs(String[] initArgs) {
        PauseTimeUnits unit = valueOf(initArgs[INDEX_THIRD].toUpperCase());
        if (unit == MIN) {
            return Integer.parseInt(initArgs[INDEX_SECOND]) * ONE_MINUTE_MS;
        }
        return Integer.parseInt(initArgs[INDEX_SECOND]);
    }
}
