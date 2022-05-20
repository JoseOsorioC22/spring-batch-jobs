package com.infybuzz.service;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class SecondJobSheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("firstJob")
    private Job firstJob;

    @Autowired
    @Qualifier("secondJob")
    private Job secondJob;

    private static final Logger log = Logger.getLogger("com.infybuzz.service");

    //@Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void SecondJobStarter()
    {
        log.info("Mostrando desde Second Job Starter");
        Map<String, JobParameter> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(params);
        try
        {
          jobLauncher.run(secondJob, jobParameters );

        } catch(Exception e)
        {
            log.warning("Error en el metodo startJob en clase SecondJobSheduler" + e.getMessage() + "/n"
                    + e.getCause());
        }
    }

}
