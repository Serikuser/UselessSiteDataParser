package by.siarhei.devByDataParser.parser.api;

public interface HtmlBodyDataParser <T>{
     T parseDataFromResponseBody(String responseBody);
}
