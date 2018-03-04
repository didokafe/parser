package com.ef.parser.task;

import com.ef.parser.domain.LogFile;
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


    @BeforeTask
    public void processLogs(TaskExecution taskExecution) {
        resolveArgs(taskExecution.getArguments());
        File file = new File(filePath);
        List<LogFile> logFiles = new ArrayList<>();

        try {
            List<String> lines = FileUtils.readLines(file, "UTF-8");
            lines.forEach(line -> {
                //logger.info("task running..." + line);
                logFiles.add(convertToLogFile(line));
            });

        } catch (IOException e) {
            logger.error("Incorrect file path or no file.\n");
            throw new RuntimeException(e.getMessage());
        }
        logFileRepository.save(logFiles);
        List<LogFile> results = logFileRepository.findAll();
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
                default:
                    break;
            }
        });
    }

    private LogFile convertToLogFile(String line) {
        String[] lineArr = line.split(DELIMITER);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        LogFile logFile = new LogFile();
        try {
            logFile.setDate(dateFormat.parse(lineArr[0]));
        } catch (ParseException e) {
            logger.error("Incorrect date format.\n");
            throw new RuntimeException(e.getMessage());
        }
        logFile.setIp(lineArr[1]);
        logFile.setRequest(lineArr[2]);
        logFile.setStatus(lineArr[3]);
        logFile.setUserAgent(lineArr[4]);

        return logFile;
    }
}
