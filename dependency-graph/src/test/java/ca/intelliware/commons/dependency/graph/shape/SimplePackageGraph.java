package ca.intelliware.commons.dependency.graph.shape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.SystemUtils;

import com.electricmind.dependency.DependencyManager;

import ca.intelliware.commons.dependency.graph.Grapher;

public class SimplePackageGraph {
	
	public static void main(String[] args) throws Exception {
		new SimplePackageGraph().process();
	}

	private void process() throws FileNotFoundException, IOException {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("ca.intelliware.hl7.generator.xsd", "ca.intelliware.hl7.generator");
		manager.add("ca.intelliware.hl7.referral");

		File file = new File(SystemUtils.JAVA_IO_TMPDIR, "SimplePackageGraph.png");
		System.out.println(file.getAbsolutePath());
		OutputStream output = new FileOutputStream(file);
		try {
			Grapher<String> grapher = new Grapher<String>(manager);
			grapher.setShape(new PackageShape<String>());
			grapher.createPng(output, 500, 500);
		} finally {
			output.close();
		}
	}
}
