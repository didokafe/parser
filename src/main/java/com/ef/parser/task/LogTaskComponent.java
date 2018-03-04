package com.ef.parser.task;

import com.ef.parser.repository.LogRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

@Component
public class LogTaskComponent {

    private static final Log logger = LogFactory.getLog(LogTaskComponent.class);

    @Autowired
    private LogRepository logRepository;


    @BeforeTask
    public void findLogs(TaskExecution taskExecution) {
        //logger.info("task running..." + taskExecution.getArguments().get(0));
        logRepository.findAll();
    }
}
