package ca.intelliware.commons.dependency;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class DescriberTest {

	@Test
	public void testShouldSupportString() throws Exception {
		String string = "This is a string";
		assertEquals("string", string, new Describer().getDescription(string));
	}

	@Test
	public void testShouldSupportFile() throws Exception {
		File file = new File("/temp");
		assertEquals("file", "temp", new Describer().getDescription(file));
	}


}
