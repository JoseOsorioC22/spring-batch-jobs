package com.infybuzz.reader;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
public class FirstItemReader implements ItemReader<Integer> {

	private static  final Logger log = Logger.getLogger("com.infybuzz.reader");

	List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
	int i = 0;

	@Override
	public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("MOSTRANDO LOG DESDE EL METODO READ() - i es igual a: " + i );
		Integer item;
		if(i < list.size()) {
			item = list.get(i);
			i++;
			return item;
		}
		i = 0;
		log.info("AHORA VA A RETORNAR NULO");
		return null;
	}

}
