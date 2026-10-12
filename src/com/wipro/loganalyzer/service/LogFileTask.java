package com.wipro.loganalyzer.service;

import com.wipro.loganalyzer.bean.LogEntry;
import com.wipro.loganalyzer.util.LogLineParser;
import com.wipro.loganalyzer.util.LogParseException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class LogFileTask implements Callable<List<LogEntry>> {

    private String filePath;

    public LogFileTask(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<LogEntry> call() throws Exception {

        List<LogEntry> logs = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(filePath));

        for (String line : lines) {

            try {
                LogEntry log = LogLineParser.parse(line);
                logs.add(log);
            } catch (LogParseException e) {
                System.out.println(e.getMessage());
            }
        }

        return logs;
    }
}