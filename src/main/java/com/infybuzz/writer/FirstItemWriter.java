package com.infybuzz.writer;

import com.infybuzz.model.StudentDTO;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class FirstItemWriter implements ItemWriter<StudentDTO> {

    private static  final Logger log = Logger.getLogger("com.infybuzz.writer");

    @Override
    public void write(List<?extends StudentDTO> list) throws Exception {
        log.info("MOSTRANDO INFO DESDE EL WRITER");

        list.stream().forEach((p) ->
        {
            if(p.getId() == 9)
            {
                throw  new ArithmeticException("ERROR ARITMETICO!!!");
            }
            System.out.println("Numero: " + p );
        });
    }
}
