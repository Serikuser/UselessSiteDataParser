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

    public static void main(String[] args) {
        DevByParsingInitializer parser = new DevByParsingInitializer();
        parser.parse();
        parser.printTotalNumberOfEmployeeInBelarus();
    }
}
