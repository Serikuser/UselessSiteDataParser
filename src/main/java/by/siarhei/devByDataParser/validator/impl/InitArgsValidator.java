package by.siarhei.devByDataParser.validator.impl;

import by.siarhei.devByDataParser.validator.PauseTimeUnits;
import by.siarhei.devByDataParser.validator.api.InputArgsValidator;

import java.util.stream.Stream;

import static org.jsoup.internal.StringUtil.isNumeric;

public class InitArgsValidator implements InputArgsValidator {
    private final static int REQUIRED_ARGS_LENGTH = 3;
    public static final int INDEX_FIRST = 0;
    public static final int INDEX_SECOND = 1;
    public static final int INDEX_THIRD = 2;

    @Override
    public boolean isArgsValid(String[] initArgs) {
        boolean isValid = false;
        if (initArgs.length == REQUIRED_ARGS_LENGTH) {
            isValid = validatePauseTime(initArgs);
            if (isValid) {
                isValid = validateSentLimit(initArgs[INDEX_FIRST]);
            }
        }
        return isValid;
    }

    private boolean validateSentLimit(String initArg) {
        if (isNumeric(initArg)) {
            int sentLimit = Integer.parseInt(initArg);
            return sentLimit > 0;
        }
        return false;
    }

    private boolean validatePauseTime(String[] initArgs) {
        boolean isValid = Stream.of(PauseTimeUnits.values()).
                anyMatch(unit -> unit.name().equalsIgnoreCase(initArgs[INDEX_THIRD]));
        String time = initArgs[INDEX_SECOND];
        if (isValid && isNumeric(time)) {
            int intTimeValue = Integer.parseInt(time);
            isValid = intTimeValue > 0;
        } else {
            isValid = false;
        }
        return isValid;
    }
}
