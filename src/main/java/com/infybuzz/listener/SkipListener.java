package com.infybuzz.listener;

import com.infybuzz.model.StudentDTO;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.logging.Logger;

@Component
public class SkipListener {


/*    Marca un método para ser llamado cuando se omite un elemento
 debido a una excepción lanzada en el ItemReader
 */
    @OnSkipInRead
    public void skipAndRead(Throwable throwable) {
      if(throwable instanceof FlatFileParseException)
      {
          CreateFile("C:\\Users\\josse\\Downloads\\Create-First-Item-Reader\\Chunk Job\\First Chun Step\\reader\\SkipInRead.txt",
                  ((FlatFileParseException) throwable).getInput() );
      }
    }

    @OnSkipInProcess
    public void SkipAndProcessor(StudentDTO studentDTO, Throwable throwable )
    {
        CreateFile("C:\\Users\\josse\\Downloads\\Create-First-Item-Reader\\Chunk Job\\First Chun Step\\processor\\SkipInProcess.txt",
               studentDTO.toString() );
    }

    @OnSkipInWrite
    public void SkipAndWritter(StudentDTO studentDTO, Throwable throwable)
    {
        CreateFile("C:\\Users\\josse\\Downloads\\Create-First-Item-Reader\\Chunk Job\\First Chun Step\\writer\\SkipInWriter.txt",
                studentDTO.toString() );
    }



    public void CreateFile(String filePatch, String data )
    {
        try(FileWriter fileWriter = new FileWriter(new File(filePatch), true))
        {
            fileWriter.write(data + "\n");
        } catch(Exception e)
        {
            Logger log = Logger.getLogger("com.infybuzz.listener.SkipListener");
            log.info("ERROR:: " + e.getMessage());
        }
    }

}
