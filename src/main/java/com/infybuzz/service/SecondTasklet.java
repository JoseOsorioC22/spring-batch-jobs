package com.infybuzz.service;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class SecondTasklet implements Tasklet {
	private static  final Logger log = Logger.getLogger("com.infybuzz.service");
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

	//	log.info("MOSTRANDO LOG DESDE EÑ");
		return RepeatStatus.FINISHED;
	}

}
