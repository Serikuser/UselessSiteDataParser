package by.siarhei.devByDataParser.start;

public class Start {

    public static void main(String[] args) {
        DevByParsingInitializer parser = new DevByParsingInitializer();
        boolean isParsed = parser.parse();
        if(isParsed){
            parser.printTotalNumberOfEmployeeInBelarus();
        }
    }
}
