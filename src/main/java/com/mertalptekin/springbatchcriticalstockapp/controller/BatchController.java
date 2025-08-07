package com.mertalptekin.springbatchcriticalstockapp.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher; // Job çalıştırcak olan nesnemiz, EnableBatchProcessing ile otomatik olarak oluşturuldu Spring IOC üzerinde

    @Autowired
    private JobExplorer jobExplorer; // Job'ların durumunu kontrol etmek için kullanılır, jobExplorer ile job'ların durumlarını kontrol edebiliriz.


    @Autowired
    @Qualifier("sampleTaskletJob") // Job'ımızı ismiyle alıyoruz, @Qualifier ile job'ı alabiliriz.
    private Job taskletJob; // Job'ımızı burada tanımlıyoruz, @Autowired ile job'ı alabiliriz.

    @Autowired
    @Qualifier("criticalStockJob") // Job'ımızı ismiyle alıyoruz, @Qualifier ile job'ı alabiliriz.
    private Job criticalStockJob;

    @Autowired
    @Qualifier("criticalStockJobFlow") // Job'ımızı ismiyle alıyoruz, @Qualifier ile job'ı alabiliriz.
    private Job criticalStockJobFlow; // Job'ımızı burada tanımlıyoruz, @Autowired ile job'ı alabiliriz.

    @PostMapping("runSampleTaskletJob")
    public ResponseEntity<String> runTaskletBatchJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
            // JobLauncher ile job'ı çalıştırıyoruz, jobLauncher.run() metodu ile job'ı çalıştırabiliriz.
            // Bu örnekte JobPatermters değeri değişmeyen sabit bir değer olarak tanımlanmıştır. Job Failed olursa tekrardan çalıştırılabilir. Fakat JobCompleted olursa already hatası verir.
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("jobName","taskletJob")
                    .toJobParameters();
            jobLauncher.run(taskletJob, jobParameters); // JobParameters null olarak geçildi, eğer job parametreleri varsa burada geçilebilir.

        return ResponseEntity.ok("Tasklet Job started successfully");
    }

    @PostMapping("runCriticalStockJobFlow")
    public ResponseEntity<String> runCriticalStockJobFlow() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("jobName","runCriticalStockJobFlow")
                .addLong("executionTime", System.currentTimeMillis()) // yeni parametre aldığında her seferince çalışacaktır.
                .toJobParameters();
        jobLauncher.run(criticalStockJobFlow, jobParameters);

        return ResponseEntity.ok("Critical Stock Job Flow started successfully");
    }


    @PostMapping("runCriticalStockJob")
    public ResponseEntity<String> runCriticalStockJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("jobName","criticalStockJob")
                .addLong("executionTime", System.currentTimeMillis()) // yeni parametre aldığında her seferince çalışacaktır.
                .toJobParameters();
        jobLauncher.run(criticalStockJob, jobParameters);

        return ResponseEntity.ok("Critical Stock Job started successfully");
    }

    @Autowired
    @Qualifier("simpleChunkOrientedJob")
    private Job simpleChunkOrientedJob;


    @PostMapping("runSimpleChunkOrientedJob")
    public ResponseEntity<String> runSimpleChunkOrientedJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("jobName","SimpleChunkOrientedJob")
                .addLong("executionTime", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(simpleChunkOrientedJob, jobParameters);

        return ResponseEntity.ok("SimpleChunkOriented Job started successfully");
    }



    // Not: ChunkOriented ProductChunkDto örneği için kullanıyoruz

    @Autowired
    @Qualifier("chunkOrientedProductJob")
    private Job chunkOrientedProductJob;


    @PostMapping("runChunkOrientedProductJob")
    public ResponseEntity<String> runChunkOrientedProductJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("jobName","ChunkOrientedProductJob")
                .addLong("executionTime", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(chunkOrientedProductJob, jobParameters);

        return ResponseEntity.ok("ChunkOrientedProductJob Job started successfully");
    }


    @Autowired
    @Qualifier("customerFlowJob")
    private Job customerFlowJob;


    @PostMapping("runCustomerFlowJob")
    public ResponseEntity<String> runCustomerFlowJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("jobName","CustomerFlowJob")
                .addLong("executionTime", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(customerFlowJob, jobParameters);

        return ResponseEntity.ok("CustomerFlowJob Job started successfully");
    }


}
