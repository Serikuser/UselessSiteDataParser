package by.siarhei.devByDataParser.validator.impl;

import by.siarhei.devByDataParser.validator.api.HtmlElementDataValidator;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputElementDataValidator implements HtmlElementDataValidator {
    private static final String REGEX_NON_NUMERIC = "[^\\d. +-]+";
    private static final String NUMBERS_REGEX = "[^0-9]";

    @Override
    public boolean isValid(Element element) {
        String elementData = element.text().replaceAll(NUMBERS_REGEX, "");
        return !isElementDataNotNumeric(elementData);
    }

    public boolean isElementDataNotNumeric(String elementData) {
        Pattern pattern = Pattern.compile(REGEX_NON_NUMERIC);
        Matcher matcher = pattern.matcher(elementData);
        return matcher.find();
    }
}
