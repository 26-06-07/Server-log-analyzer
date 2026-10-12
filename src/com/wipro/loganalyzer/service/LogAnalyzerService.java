package com.wipro.loganalyzer.service;

import com.wipro.loganalyzer.bean.LogEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class LogAnalyzerService {

    public static void main(String[] args) throws Exception {

        List<String> filePaths = Arrays.asList(
                "log1.txt",
                "log2.txt",
                "log3.txt"
        );

        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<List<LogEntry>>> futures = new ArrayList<>();

        for (String file : filePaths) {
            futures.add(executor.submit(new LogFileTask(file)));
        }

        List<LogEntry> allLogs = new ArrayList<>();

        for (Future<List<LogEntry>> future : futures) {
            allLogs.addAll(future.get());
        }

        executor.shutdown();

        Map<String, Long> severityCount = allLogs.stream()
                .collect(Collectors.groupingBy(
                        LogEntry::getLevel,
                        Collectors.counting()));

        System.out.println("===== Severity Count =====");

        severityCount.forEach((level, count) ->
                System.out.println(level + " : " + count));

        System.out.println("\n===== Timeline =====");

        allLogs.stream()
                .filter(log -> log.getLevel().equalsIgnoreCase("ERROR"))
                .sorted(Comparator.comparing(LogEntry::getTimestamp))
                .forEach(System.out::println);
    }
}
