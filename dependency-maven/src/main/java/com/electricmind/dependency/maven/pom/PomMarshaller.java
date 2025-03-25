package com.electricmind.dependency.maven.pom;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

class PomMarshaller {

	public PomModel parsePom(InputStream input) throws IOException {
		XmlMapper mapper = new XmlMapper();
		return mapper.readValue(input, PomModel.class);
	}
	
}
