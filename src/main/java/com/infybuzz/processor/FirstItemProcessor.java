package com.infybuzz.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class FirstItemProcessor implements ItemProcessor<Integer, Long> {

    private static  final Logger log = Logger.getLogger("com.infybuzz.processor");
    @Override
    public Long process(Integer numero) throws Exception {
      log.info("MOSTRANDO LOG DESDE EL PROCESSOR");
        return Long.valueOf(numero+20);
    }
}
