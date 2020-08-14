package by.siarhei.devByDataParser.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ResponseParser {
    private static final String NUMBERS_REGEX = "[^0-9]";
    private static final String EMPLOYEE_COUNT_CLASS = "employee-count";
    private static final int INDEX_FIRST = 0;
    private static final int INDEX_THIRD = 2;

    public Integer parseEmployeeNumberFromHTMLResponseBody(String responseBody) {
        Document doc = Jsoup.parse(responseBody);
        Elements links = doc.getElementsByClass(EMPLOYEE_COUNT_CLASS);
        if (!links.isEmpty()) {
            int size = links.size();
            switch (size) {
                case 1:
                case 2:
                    return getNumberOfEmployee(links, INDEX_FIRST);
                default:
                    return getNumberOfEmployee(links, INDEX_THIRD);
            }
        }
        return 0;
    }

    private int getNumberOfEmployee(Elements elements, int index) {
        return Integer.parseInt(elements.get(index).text().replaceAll(NUMBERS_REGEX, ""));
    }
}
