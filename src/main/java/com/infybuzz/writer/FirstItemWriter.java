package com.infybuzz.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class FirstItemWriter implements ItemWriter<Integer> {

    private static  final Logger log = Logger.getLogger("com.infybuzz.writer");

    @Override
    public void write(List<?extends Integer> list) throws Exception {
        log.info("MOSTRANDO INFO DESDE EL WRITER");
        list.stream().forEach((p) -> System.out.println("Numero: " + p ));
    }
}