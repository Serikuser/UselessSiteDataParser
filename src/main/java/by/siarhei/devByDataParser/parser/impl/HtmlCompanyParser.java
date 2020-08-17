package by.siarhei.devByDataParser.parser.impl;

import by.siarhei.devByDataParser.entity.ParsedCompany;
import by.siarhei.devByDataParser.parser.api.HtmlBodyDataParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlCompanyParser implements HtmlBodyDataParser<List<ParsedCompany>> {

    private static final String DEV_BY_COMPANY_URL = "https://companies.dev.by/";
    private static final String EMPLOYEE_TABLE_ID = "tablesort";
    private static final String A_HREF_QUERY = "a[href~=^((?!http).)*$]";
    private static final String HREF_CLASS = "href";
    private static final String SPLASH = "/";
    private static final int MIN_LENGTH = 1;
    private static final int INDEX_SECOND = 1;


    @Override
    public List<ParsedCompany> parseDataFromResponseBody(String responseBody) {
        List<ParsedCompany> parsedCompanyList = new ArrayList<>();
        Document doc = Jsoup.parse(responseBody);
        Element mainLink = doc.getElementById(EMPLOYEE_TABLE_ID);
        Elements subLinks = mainLink.select(A_HREF_QUERY);
        List<String> parsedCompanyURL = new ArrayList<>();
        for (Element element : subLinks) {
            String companyName = element.text();
            String[] temp = element.attr(HREF_CLASS).split(SPLASH);
            if (temp.length >= MIN_LENGTH) {
                String companyURLName = temp[INDEX_SECOND].replaceAll(SPLASH, "");
                if (!parsedCompanyURL.contains(companyURLName)) {
                    parsedCompanyURL.add(companyURLName);
                    parsedCompanyList.add(new ParsedCompany(companyName, formURL(companyURLName)));
                }
            }
        }
        return parsedCompanyList;
    }
    private String formURL(String companyURLName){
        return DEV_BY_COMPANY_URL + companyURLName;
    }
}
