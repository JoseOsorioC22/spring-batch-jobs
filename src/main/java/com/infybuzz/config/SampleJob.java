package com.infybuzz.config;

import com.infybuzz.model.StudentDTO;
import com.infybuzz.model.StudentJdbc;
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
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
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
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
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

    @Autowired
    private DataSource dataSource;


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
    public Step StepWithChunk() {
        return stepBuilderFactory.get("step with Chunk")
                .<StudentJdbc, StudentJdbc>chunk(2)
               // .reader(readCSV())
                .reader(JdbcCursorItemReader())
                //.processor(firstItemProcessor)
                .writer(firstItemWriter)
                .build();

    }


    // Este metodo permite convertir la fecha de nacimiento A LocalDate
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


    public FlatFileItemReader<StudentDTO> readCSV() {
        /*
         * For this,  first:
         * -> Create the reference FlatFIleItemReader and add the type
         * -> Source location of csv file
         * -> the line Mapper:
         *     -> Line tokenizer
         * 	  -> Bean Mapper */

        // Creamos los objetos:

        // Leemos el archivo .csv
        File file =  new File("C:\\Users\\josse\\Downloads\\Create-First-Item-Reader\\src\\main\\resources\\students.csv");
        FileSystemResource recurso = new FileSystemResource(file);
        FlatFileItemReader<StudentDTO> flatFileItemReader = new FlatFileItemReader<>();
        DefaultLineMapper<StudentDTO> defaultLineMapper = new DefaultLineMapper<StudentDTO>();
        BeanWrapperFieldSetMapper<StudentDTO> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();


        // Establecemos el recurso
        flatFileItemReader.setResource(recurso);

        // Delimitamos:
        delimitedLineTokenizer.setNames("Id", "nombre", "apellidos", "email", "fechaNacimiento"); // Los nombres a leer
        delimitedLineTokenizer.setDelimiter("|"); // carcater que delimita

        // El Wrapper
        beanWrapperFieldSetMapper.setTargetType(StudentDTO.class); // Le pasamos la clase del objeto que mapea
        beanWrapperFieldSetMapper.setConversionService(testConversionService()); // le agregamos la linea para la conversion del campo LocalDate



        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer); // le pasamos el delimitador
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper); // le pasamos la clase encargada que mapea
        flatFileItemReader.setLineMapper(defaultLineMapper); // le pasamos el objeto defaultLineMapper

        // Le indicamos las lineas que irá leyendo (en este caso es de uno en uno )
        flatFileItemReader.setLinesToSkip(1);


        return flatFileItemReader; // retornamos
    }

    @Bean
    public FieldSetMapper<StudentDTO> testClassRowMapper(ConversionService conversionService) {
        BeanWrapperFieldSetMapper<StudentDTO> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setConversionService(conversionService);
        mapper.setTargetType(StudentDTO.class);
        return mapper;
    }

    public JdbcCursorItemReader<StudentJdbc> JdbcCursorItemReader()
    {
        JdbcCursorItemReader<StudentJdbc> jdbccursorItemReader = new JdbcCursorItemReader<StudentJdbc>();

        jdbccursorItemReader.setDataSource(dataSource);
        jdbccursorItemReader.setSql("select id, nombre, apellido, email from student");

        jdbccursorItemReader.setRowMapper( new BeanPropertyRowMapper<StudentJdbc>()
        {
            {
                setMappedClass(StudentJdbc.class);
            }
        });

        return  jdbccursorItemReader;
    }




}
