package by.siarhei.devByDataParser.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class InputDataReader {

    private static final String DEFAULT_INPUT_FILE = "input/companyURLsParsed.txt";
    private static final Logger logger = LogManager.getLogger();

    public List<String> readData() {
        logger.info("The input was obtained from the default source");
        return readData(DEFAULT_INPUT_FILE);
    }

    public List<String> readData(String fileFolder) {

        Path path = Paths.get(fileFolder);
        List<String> list;

        try (BufferedReader br = Files.newBufferedReader(path)) {
            list = br.lines().collect(Collectors.toList());

        } catch (IOException e) {
            logger.error(String.format("File cannot be opened or does not exists, throws exception: %s", e));
            list = readData();
        }
        return list;
    }
}
