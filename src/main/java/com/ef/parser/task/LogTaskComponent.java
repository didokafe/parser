package com.ef.parser.task;

import com.ef.parser.repository.LogRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class LogTaskComponent {

    private static final Log logger = LogFactory.getLog(LogTaskComponent.class);

    @Autowired
    private LogRepository logRepository;

    private String filePath;


    @BeforeTask
    public void findLogs(TaskExecution taskExecution) {
        resolveArgs(taskExecution.getArguments());
        File file = new File(filePath);
        try {
            List<String> lines = FileUtils.readLines(file, "UTF-8");
            lines.forEach(line -> {
                logger.info("task running..." + line);

            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        logRepository.findAll();
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
}
