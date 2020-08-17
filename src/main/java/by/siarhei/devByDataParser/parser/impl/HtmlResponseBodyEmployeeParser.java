package by.siarhei.devByDataParser.parser.impl;

import by.siarhei.devByDataParser.parser.api.HtmlBodyDataParser;
import by.siarhei.devByDataParser.validator.api.HtmlElementDataValidator;
import by.siarhei.devByDataParser.validator.impl.InputElementDataValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlResponseBodyEmployeeParser implements HtmlBodyDataParser <Integer>{
    private static final String NUMBERS_REGEX = "[^0-9]";
    private static final String EMPLOYEE_COUNT_CLASS = "employee-count";
    private static final int INDEX_FIRST = 0;
    private static final int INDEX_THIRD = 2;
    private final HtmlElementDataValidator validator;

    public HtmlResponseBodyEmployeeParser() {
        validator = new InputElementDataValidator();
    }

    @Override
    public Integer parseDataFromResponseBody(String responseBody) {
        Document doc = Jsoup.parse(responseBody);
        Elements links = doc.getElementsByClass(EMPLOYEE_COUNT_CLASS);
        if (!links.isEmpty()) {
            try {
                int size = links.size();
                return getNumberOfEmployee(size, links);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private int getNumberOfEmployee(int size, Elements links) {
        switch (size) {
            case 1:
            case 2:
                return parseNumberOfEmployeeFromHTMLElements(links, INDEX_FIRST);
            default:
                return parseNumberOfEmployeeFromHTMLElements(links, INDEX_THIRD);
        }
    }

    private int parseNumberOfEmployeeFromHTMLElements(Elements elements, int index) {
        Element element = elements.get(index);
        if (validator.isValid(element)) {
            return Integer.parseInt(element.text().replaceAll(NUMBERS_REGEX, ""));
        } else {
            return 0;
        }
    }
}
