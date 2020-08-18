package by.siarhei.devByDataParser.start;

public class Start {

    public static void main(String[] args) {
        DevByParsingInitializer parser = new DevByParsingInitializer();
        parser.parse();
        parser.printTotalNumberOfEmployeeInBelarus();
    }
}
