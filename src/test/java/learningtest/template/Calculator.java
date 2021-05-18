package learningtest.template;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class Calculator {

    private final BufferedReaderContext bufferedReaderContext;

    public Integer calcSum(String filePath) throws IOException {
        LineCallback<Integer> callback = (line, value) -> {
            return value += Integer.valueOf(line);
        };

        return bufferedReaderContext.lineReadTemplate(filePath, callback, 0);
    }

    public Integer calcMultiply(String filePath) throws IOException {
        LineCallback<Integer> callback = (line, value) -> {
            return value *= Integer.valueOf(line);
        };

        return bufferedReaderContext.lineReadTemplate(filePath, callback, 1);
    }

    public String concatenateStrings(String filePath) throws IOException {
        LineCallback<String> callback = (line, value) -> {
            return value + line;
        };

        return bufferedReaderContext.lineReadTemplate(filePath, callback, "");
    }
}
