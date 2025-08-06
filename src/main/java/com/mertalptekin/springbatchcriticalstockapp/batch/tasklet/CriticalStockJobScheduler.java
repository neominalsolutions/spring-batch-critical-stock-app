package com.mertalptekin.springbatchcriticalstockapp.batch.tasklet;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class CriticalStockJobScheduler { // Zamanlanmış görevleri çalıştırmak için kullanılan sınıf

    @Autowired
    private JobLauncher launcher;
    @Autowired
    @Qualifier("criticalStockJob")
    private Job job;

    // @Scheduled(fixedRate = 5000 * 60,fixedDelay = 3000 * 60) // Runs every 5 minutes
    @Scheduled(cron = "0 * * * * *")
    public void runCriticalStockJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        var paramBuilder = new JobParametersBuilder();
        paramBuilder.addDate("executionTime", new java.util.Date());
        var parameters = paramBuilder.toJobParameters();
        JobExecution je = launcher.run(job,parameters);

        if(je.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("Critical Stock Job completed");
        }
        else if(je.getStatus() == BatchStatus.FAILED) {
            System.out.println("Critical Stock Job failed");
        }
        else {
            System.out.println("Critical Stock Job is in progress");
        }
    }

}
