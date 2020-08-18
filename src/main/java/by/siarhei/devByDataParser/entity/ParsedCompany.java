package by.siarhei.devByDataParser.entity;

import java.util.Objects;

public class ParsedCompany {
    private String name;
    private String url;
    private String htmlComponentBody;
    private int employeeInBelarusNumber;

    public ParsedCompany(String name, String url) {
        this.url = url;
        this.name = name;
    }

    public ParsedCompany() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getEmployeeInBelarusNumber() {
        return employeeInBelarusNumber;
    }

    public void setEmployeeInBelarusNumber(int employeeInBelarusNumber) {
        this.employeeInBelarusNumber = employeeInBelarusNumber;
    }

    public String getHtmlComponentBody() {
        return htmlComponentBody;
    }

    public void setHtmlComponentBody(String htmlComponentBody) {
        this.htmlComponentBody = htmlComponentBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsedCompany that = (ParsedCompany) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }
}
