package com.infybuzz.config;

import com.infybuzz.model.StudentDTO;
import com.infybuzz.processor.FirstItemProcessor;
import com.infybuzz.reader.FirstItemReader;
import com.infybuzz.writer.FirstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.infybuzz.listener.FirstJobListener;
import com.infybuzz.listener.FirstStepListener;
import com.infybuzz.service.SecondTasklet;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.time.LocalDate;

@Configuration
public class SampleJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private SecondTasklet secondTasklet;
	
	@Autowired
	private FirstJobListener firstJobListener;
	
	@Autowired
	private FirstStepListener firstStepListener;

	@Autowired
	private FirstItemReader firstItemReader;

	@Autowired
	private FirstItemProcessor firstItemProcessor;

	@Autowired
	private FirstItemWriter firstItemWriter;


	@Bean
	public Job firstJob() {
		return jobBuilderFactory.get("First Job")
				.incrementer(new RunIdIncrementer())
				.start(firstStep())
				.next(secondStep())
				.listener(firstJobListener)
				.build();
	}

	@Bean
	public Step firstStep() {
		return stepBuilderFactory.get("First Step")
				.tasklet(firstTask())
				.listener(firstStepListener)
				.build();
	}


	public Tasklet firstTask() {
		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

				return RepeatStatus.FINISHED;
			}
		};
	}
	
	public Step secondStep() {
		return stepBuilderFactory.get("Second Step")
				.tasklet(secondTasklet)
				.build();
	}
	
	/*
	 * private Tasklet secondTask() { return new Tasklet() {
	 * 
	 * @Override public RepeatStatus execute(StepContribution contribution,
	 * ChunkContext chunkContext) throws Exception {
	 * System.out.println("This is second tasklet step"); return
	 * RepeatStatus.FINISHED; } }; }
	 */


	@Bean
	public Job secondJob() {
		return jobBuilderFactory.get("Second Job")
				.incrementer(new RunIdIncrementer())
				.start(StepWithChunk())
				.build();
	}

	@Bean
	public Step StepWithChunk()
	{
		return stepBuilderFactory.get("step with Chunk")
				.<StudentDTO, StudentDTO>chunk(2)
				.reader(readCSV())
				//.processor(firstItemProcessor)
				.writer(firstItemWriter)
				.build();

	}


// Este metodo permite convertir la fecha de nacimiento A DateTime
	public ConversionService testConversionService() {
		DefaultConversionService testConversionService = new DefaultConversionService();
		DefaultConversionService.addDefaultConverters(testConversionService);
		testConversionService.addConverter(new Converter<String, LocalDate>() {
			@Override
			public LocalDate convert(String text) {
				return LocalDate.parse(text);
			}

		});

		return testConversionService;
	}



	public FlatFileItemReader<StudentDTO> readCSV()
	{
	/*
	* For this,  first:
	* -> Create the reference FlatFIleItemReader and add the type
	* -> Source location of csv file
	* -> the line Mapper:
	*     -> Line tokenizer
	* 	  -> Bean Mapper */
		FlatFileItemReader<StudentDTO> flatFileItemReader = new FlatFileItemReader<>();



		// Leemos el archivo .csv

		flatFileItemReader.setResource(new FileSystemResource(
			new File("C:\\Users\\josse\\Downloads\\Create-First-Item-Reader\\src\\main\\resources\\students.csv")));

		// Se Mapea
		flatFileItemReader.setLineMapper(new DefaultLineMapper<StudentDTO>(){
			{ // Pasamos el line tokenizer
				setLineTokenizer(new DelimitedLineTokenizer()
				{
					{ // establecemos los nombres que se encuentran en el indide del  archivo csv
						setNames("Id", "nombre", "apellidos", "email", "fechaNacimiento");
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentDTO>()
				{
					{
						setTargetType(StudentDTO.class);
					}
					{
						setConversionService(testConversionService());
					}
				});

			}
		});


		// Le indicamos las lineas que ira leyendo (en este caso es de uno en uno )
		flatFileItemReader.setLinesToSkip(1);

		return flatFileItemReader;
	}

	@Bean
	public FieldSetMapper<StudentDTO> testClassRowMapper(ConversionService conversionService)
	{
		BeanWrapperFieldSetMapper<StudentDTO> mapper = new BeanWrapperFieldSetMapper<>();
		mapper.setConversionService(conversionService);
		mapper.setTargetType(StudentDTO.class);
		return mapper;
	}


}
