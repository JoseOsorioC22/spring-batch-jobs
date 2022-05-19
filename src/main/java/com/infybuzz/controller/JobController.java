package com.infybuzz.controller;

import com.infybuzz.request.JobparamsRequest;
import com.infybuzz.service.JobService;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/job")
public class JobController {

    @Autowired
    private JobService jobService;


    @GetMapping("/start/{jobName}")
    public String startJob(@PathVariable String jobName,
                           @RequestBody List<JobparamsRequest> jobParameterslist ) {
        jobService.startJob(jobName, jobParameterslist);
       return  "job: starterd...";
    }

}
