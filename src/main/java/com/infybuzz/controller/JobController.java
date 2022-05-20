package com.infybuzz.controller;

import com.infybuzz.request.JobparamsRequest;
import com.infybuzz.service.JobService;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("api/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobOperator jobOperator;



    @GetMapping("/start/{jobName}")
    public String startJob(@PathVariable String jobName,
                           @RequestBody List<JobparamsRequest> jobParameterslist ) {
        jobService.startJob(jobName, jobParameterslist);
       return  "job: starterd...";
    }

    @GetMapping("/stop/{jobExecutionId}")
    public String stopJob(@PathVariable Long jobExecutionId)
    {
        try
        {
            jobOperator.stop(jobExecutionId);
        } catch(Exception e)
        {
            return "Error: Stopping Job";
        }

        return "Stopping Job...";
    }


}
