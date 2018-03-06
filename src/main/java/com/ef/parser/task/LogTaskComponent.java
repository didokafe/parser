package com.ef.parser.task;

import com.ef.parser.domain.Duration;
import com.ef.parser.domain.LogEntry;
import com.ef.parser.repository.LogFileRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class LogTaskComponent {

    private static final Log logger = LogFactory.getLog(LogTaskComponent.class);
    private static final String DELIMITER = "\\|";

    @Autowired
    private LogFileRepository logFileRepository;

    @Value("${parser.file.date-format}")
    private String format;

    private String filePath;
    private Date startDate;
    private String duration;
    private Integer threshold;


    @BeforeTask
    public void processLogs(TaskExecution taskExecution) {
        resolveArgs(taskExecution.getArguments());
        File file = new File(filePath);
        List<LogEntry> logEntries = new ArrayList<>();

        try {
            List<String> lines = FileUtils.readLines(file, "UTF-8");
            lines.forEach(line -> {
                //logger.info("task running..." + line);
                logEntries.add(convertToLogFile(line));
            });

        } catch (IOException e) {
            logger.error("Incorrect file path or no file.\n");
            throw new RuntimeException(e.getMessage());
        }
        logFileRepository.save(logEntries);
        List<LogEntry> results = logFileRepository.findAll();
        logger.info("Number of results..." + results.size());
    }

    //switch to resolve arguments
    private void resolveArgs(List<String> args) {
        args.forEach(arg -> {
            String[] argPart = arg.split("=");
            switch(argPart[0]) {
                case("--accesslog"):
                    filePath = argPart[1];
                    break;
                case("--startDate"):
                    startDate = getStartDate(argPart[1]);
                    break;
                case("--duration"):
                    duration = Duration.valueOf(argPart[1].toUpperCase()).name();
                    break;
                case("--threshold"):
                    threshold = Integer.valueOf(argPart[1]);
                    break;
                default:
                    break;
            }
        });
    }

    private LogEntry convertToLogFile(String line) {
        String[] lineArr = line.split(DELIMITER);
        LogEntry logEntry = new LogEntry();
        logEntry.setDate(getStartDate(lineArr[0]));
        logEntry.setIp(lineArr[1]);
        logEntry.setRequest(lineArr[2]);
        logEntry.setStatus(lineArr[3]);
        logEntry.setUserAgent(lineArr[4]);

        return logEntry;
    }

    private Date getStartDate(String startDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(startDate);
        } catch (ParseException e) {
            logger.error("Incorrect date format.\n");
            throw new RuntimeException(e.getMessage());
        }
    }
}
