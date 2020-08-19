package by.siarhei.devByDataParser.start;

import by.siarhei.devByDataParser.validator.api.InputArgsValidator;
import by.siarhei.devByDataParser.validator.impl.InitArgsValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Start {
    private static final Logger logger = LogManager.getLogger();
    public static final String ERROR_MESSAGE = "Initialization arguments were not entered or incorrect " +
            "(not negative integer sentLimit, not negative integer pauseTime,string time unit allowed values : ms, min). " +
            " Initialization started with default values;";


    public static void main(String[] args) {
        DevByParsingInitializer parser;
        InputArgsValidator validator = new InitArgsValidator();
        if (validator.isArgsValid(args)) {
            parser = new DevByParsingInitializer(args);
            boolean isParsed = parser.parse();
            if (isParsed) {
                parser.printTotalNumberOfEmployeeInBelarus();
            }
        } else {
            logger.info(ERROR_MESSAGE);
            parser = new DevByParsingInitializer();
            boolean isParsed = parser.parse();
            if (isParsed) {
                parser.printTotalNumberOfEmployeeInBelarus();
            }
        }
    }
}
