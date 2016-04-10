package com.lufax.esearch.config.xml;

import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class ESearchConfigReader {
	
	public static <T> T parseConfig(Reader reader,Class<T> dataType) throws JAXBException, InstantiationException, IllegalAccessException {
		JAXBContext context = JAXBContext.newInstance(dataType);
		Marshaller marshaller = context.createMarshaller();
		Unmarshaller unmarshaller = context.createUnmarshaller();
		T data = (T) unmarshaller.unmarshal(reader);
		return data;
	}
}
