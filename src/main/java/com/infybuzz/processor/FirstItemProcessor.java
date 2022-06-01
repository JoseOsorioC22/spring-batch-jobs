package com.infybuzz.processor;

import com.infybuzz.model.StudentDTO;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class FirstItemProcessor implements ItemProcessor<StudentDTO, StudentDTO> {

    private static  final Logger log = Logger.getLogger("com.infybuzz.processor");
    @Override
    public StudentDTO process(StudentDTO student ) throws Exception {
      log.info("MOSTRANDO LOG DESDE EL PROCESSOR");
      if(student.getId() == 6 )
      {
          throw  new NullPointerException();
      }
      student.setApellidos(student.getApellidos() + " - APE");
        return student;
    }
}
