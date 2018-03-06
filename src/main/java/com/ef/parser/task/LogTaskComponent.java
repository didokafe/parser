package com.ef.parser.task;

import com.ef.parser.domain.Blocked;
import com.ef.parser.domain.Duration;
import com.ef.parser.domain.LogEntry;
import com.ef.parser.repository.BlockedRepository;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class LogTaskComponent {

    private static final Log logger = LogFactory.getLog(LogTaskComponent.class);
    private static final String DELIMITER = "\\|";

    @Autowired
    private LogFileRepository logFileRepository;

    @Autowired
    private BlockedRepository blockedRepository;

    @Value("${parser.file.date-format}")
    private String format;
    @Value("${parser.console.date-format}")
    private String consoleFormat;

    private String filePath;
    private Date startDate;
    private Date endDate;
    private Duration duration;
    private Long threshold;


    /**
     * The Spring Framework will call this method to run the task.
     * @param taskExecution
     */
    @BeforeTask
    public void processLogs(TaskExecution taskExecution) {
        resolveArgs(taskExecution.getArguments());
        File file = new File(filePath);
        List<LogEntry> logEntries = new ArrayList<>();

        try {
            List<String> lines = FileUtils.readLines(file, "UTF-8");
            lines.forEach(line -> {
                logEntries.add(convertToLogFile(line));
            });

        } catch (IOException e) {
            logger.error("Incorrect file path or no file.\n");
            throw new RuntimeException(e.getMessage());
        }
        logFileRepository.save(logEntries);
        setEndDate(startDate, duration);
        List<LogEntry> results = logFileRepository.findByDatesAndTreshold(startDate, endDate, threshold);
        logger.info("Number of results..." + results.size());
        processBlocked(results);
    }

    /**
     * Resolve the input arguments.
     * They don't need to be in an exact order.
     * @param args Input
     */
    private void resolveArgs(List<String> args) {
        args.forEach(arg -> {
            String[] argPart = arg.split("=");
            switch(argPart[0]) {
                case("--accesslog"):
                    filePath = argPart[1];
                    break;
                case("--startDate"):
                    startDate = getConsoleDate(argPart[1]);
                    break;
                case("--duration"):
                    duration = Duration.valueOf(argPart[1].toUpperCase());
                    break;
                case("--threshold"):
                    threshold = Long.valueOf(argPart[1]);
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * Write the returned results to a new table.
     * @param entries The saved entries from a file.
     */
    private void processBlocked(List<LogEntry> entries) {
        List<Blocked> blockeds = new ArrayList<>();
        entries.forEach(entry -> {
            Blocked blocked = new Blocked();
            blocked.setIp(entry.getIp());
            String period = duration.equals(Duration.HOURLY) ? "hour" : "day";
            blocked.setReason("Too many requests per " + period);
            blockeds.add(blocked);
            logger.info("Blocked ip: " + blocked.getIp() + " | " + blocked.getReason());
        });
        blockedRepository.save(blockeds);
    }

    /**
     * Converts a string to a ready for saving LogEntry
     * @param line The input line of a file.
     * @return LogEntry
     */
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
        return convertDate(startDate, format);
    }

    private Date getConsoleDate(String inputDate) {
        return convertDate(inputDate, consoleFormat);
    }

    private Date convertDate(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            logger.error("Incorrect date format.\n");
            throw new RuntimeException(e.getMessage());
        }
    }

    private void setEndDate(Date startDate, Duration duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        switch (duration) {
            case DAILY:
                calendar.add(Calendar.DATE, 1);
                break;
            case HOURLY:
                calendar.add(Calendar.HOUR, 1);
                break;
        }

        endDate = calendar.getTime();
    }
}
