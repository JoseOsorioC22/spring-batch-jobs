package com.infybuzz.service;

import com.infybuzz.request.JobparamsRequest;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class JobService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("firstJob")
    private Job firstJob;

    @Autowired
    @Qualifier("secondJob")
    private Job secondJob;


    private static final Logger log = Logger.getLogger("com.infybuzz.service");

    @Async
    public void startJob(String jobName, @RequestBody List<JobparamsRequest> jobParameterslist)
    {
        Map<String, JobParameter> params = new HashMap<>();

        jobParameterslist.stream().forEach((objetoParams) ->
        {
            params.put(objetoParams.getParamKey(), new JobParameter(objetoParams.getParamValue()));
        });

        JobParameters jobParameters = new JobParameters(params);
        try
       {
           if(jobName.equals("first"))
           {
               jobLauncher.run(firstJob, jobParameters);
           } else if(jobName.equals("second"))
           {
               jobLauncher.run(secondJob, jobParameters);
           }
       } catch(Exception e)
       {
           log.warning("Error en el metodo startJob en clase JobService " + e.getMessage() + "/n"
           + e.getCause());
       }

    }

}
