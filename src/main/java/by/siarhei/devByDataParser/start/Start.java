package by.siarhei.devByDataParser.start;

import by.siarhei.devByDataParser.entity.ParsedCompany;
import by.siarhei.devByDataParser.http.exception.RequestSendException;
import by.siarhei.devByDataParser.http.request.HttpGetRequestSender;
import by.siarhei.devByDataParser.parser.impl.HtmlCompanyParser;
import by.siarhei.devByDataParser.parser.impl.HtmlResponseBodyEmployeeParser;
import by.siarhei.devByDataParser.parser.api.HtmlBodyDataParser;
import by.siarhei.devByDataParser.reader.InputDataReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Start {
    private static final Logger logger = LogManager.getLogger();
    private static final String DEV_BY_COMPANIES_URL = "https://companies.dev.by/";
    private static final HttpGetRequestSender requestSender = new HttpGetRequestSender();
    private static final HtmlBodyDataParser<Integer> companyDataParser = new HtmlResponseBodyEmployeeParser();

    public static void main(String[] args) {
        List<ParsedCompany> parsedCompanyList;

        HtmlBodyDataParser<List<ParsedCompany>> companyListParser = new HtmlCompanyParser();
        try {
            String companyListBody = requestSender.getHTMLBodyFromResponse(DEV_BY_COMPANIES_URL);
            parsedCompanyList = companyListParser.parseDataFromResponseBody(companyListBody);
            if (!parsedCompanyList.isEmpty()) {
                fillNumberOfEmployeeParsedCompanyList(parsedCompanyList);
                printTotalNumberOfEmployeeInBelarus(parsedCompanyList);

            }
        } catch (RequestSendException e) {
            logger.error("Something went wrong Error message: " + e.getMessage());
        }
    }

    private static void printTotalNumberOfEmployeeInBelarus(List<ParsedCompany> parsedCompanyList) {
        int total = 0;
        for (ParsedCompany parsedCompany : parsedCompanyList) {
            int numberOfEmployee = parsedCompany.getEmployeeInBelarusNumber();
            String companyName = parsedCompany.getName();
            total = total + numberOfEmployee;
            logger.info(String.format("Company name: %s, Number of employee in Belarus: %s",companyName,numberOfEmployee));
        }
        logger.info(String.format("Total employee in Belarus: ~%s", total));

    }

    private static void fillNumberOfEmployeeParsedCompanyList(List<ParsedCompany> parsedCompanyList) throws RequestSendException {
        requestSender.fillBodyInParsedCompanyList(parsedCompanyList);
        for (ParsedCompany parsedCompany : parsedCompanyList) {
            String body = parsedCompany.getHtmlComponentBody();
            int numberOfEmployee = companyDataParser.parseDataFromResponseBody(body);
            parsedCompany.setEmployeeInBelarusNumber(numberOfEmployee);
        }
    }
}
