package ca.intelliware.commons.dependency.graph.shape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.SystemUtils;

import com.electricmind.dependency.DependencyManager;

import ca.intelliware.commons.dependency.graph.Grapher;

public class PackageGraphWithAmbiguousCrossings {
	
	public static void main(String[] args) throws Exception {
		new PackageGraphWithAmbiguousCrossings().process();
	}

	private void process() throws FileNotFoundException, IOException {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("a1", "b1");
		manager.add("a2", "b2");
		manager.add("a2", "b3");
		manager.add("a2", "b4");
		manager.add("a2", "b1");
		manager.add("a3", "b5");
		
		manager.add("b1", "c1");
		manager.add("b1", "c2");
		manager.add("b2", "c1");
		manager.add("b2", "c2");
		manager.add("b3", "c2");
		manager.add("b4", "c2");
		manager.add("b4", "c3");
		manager.add("b5", "c2");
//		manager.add("b5", "c3");
		
		manager.add("c1", "d1");
		manager.add("c2", "d1");
		manager.add("c3", "d1");

		File file = new File(SystemUtils.JAVA_IO_TMPDIR, "PackageGraphWithAmbiguousCrossings.png");
		System.out.println(file.getAbsolutePath());
		OutputStream output = new FileOutputStream(file);
		try {
			Grapher<String> grapher = new Grapher<String>(manager);
			grapher.setShape(new BigPackageShape<String>());
			grapher.createPng(output);
		} finally {
			output.close();
		}
	}
}
