
package com.wipro.loganalyzer.util;

import com.wipro.loganalyzer.bean.LogEntry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.DateTimeException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogLineParser {

    private static final Pattern pattern =
            Pattern.compile("\\[(.*?)\\]\\[(.*?)\\]\\s(.*)");

    public static LogEntry parse(String line) throws LogParseException {

        Matcher matcher = pattern.matcher(line);

        if (!matcher.matches()) {
            throw new LogParseException("Invalid log format: " + line);
        }

        try {
            String time = matcher.group(1).replace('T', ' ');

            LocalDateTime timestamp = LocalDateTime.parse(
                    time,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );

            String level = matcher.group(2);
            String message = matcher.group(3);

            return new LogEntry(timestamp, level, message);

        } catch (DateTimeException e) {
            throw new LogParseException("Invalid timestamp: " + line);
        }
    }
}
