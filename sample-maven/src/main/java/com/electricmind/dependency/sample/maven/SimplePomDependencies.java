package com.electricmind.dependency.sample.maven;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.Layer;
import com.electricmind.dependency.Node;
import com.electricmind.dependency.graph.Grapher;
import com.electricmind.dependency.graph.shape.ArtifactShape;
import com.electricmind.dependency.graph.shape.MavenArtifactLabelStrategy;
import com.electricmind.dependency.maven.MavenArtifactName;
import com.electricmind.dependency.maven.pom.PomDependencyResolver;

public class SimplePomDependencies {

	public static void main(String[] args) throws Exception {
		
		PomDependencyResolver resolver = new PomDependencyResolver();
		DependencyManager<MavenArtifactName> dependencies = resolver.findDependencies(new File("./pom.xml"));

		createGraphDiagram(dependencies);
		createSbom(dependencies);
		
	}

	private static void createSbom(DependencyManager<MavenArtifactName> dependencies) throws IOException {
		try (HSSFWorkbook workbook = new HSSFWorkbook()) {
			Sheet sheet = workbook.createSheet();
			workbook.setSheetName(0, "Software Bill of Materials");
			
			int rowNumber = 0;
			for (Layer<Node<MavenArtifactName>> layer : dependencies.getNodeLayers()) {
				for (Node<MavenArtifactName> node : layer.getContents()) {				
					Row row = sheet.createRow(rowNumber++);
					Cell cell1 = row.createCell(0);
					cell1.setCellValue(layer.getLevel());
	
					Cell cell2 = row.createCell(1);
					cell2.setCellValue(node.getItem().getGroupId());
	
					Cell cell3 = row.createCell(2);
					cell3.setCellValue(node.getItem().getArtifactId());
	
					Cell cell4 = row.createCell(3);
					cell4.setCellValue(node.getItem().getVersion());

					Cell cell5 = row.createCell(4);
					cell5.setCellValue(node.getItem().getPackaging());
}
			}
			
			try (OutputStream output = new FileOutputStream("./target/sbom.xls")) {
				workbook.write(output);
			}
		}
	}

	private static void createGraphDiagram(DependencyManager<MavenArtifactName> dependencies)
			throws IOException {
		Grapher<MavenArtifactName> grapher = new Grapher<>(dependencies);
		grapher.getPlot().setShapeFillColorProvider(new ArtifactColorProvider());
		grapher.getPlot().setShadowColor(new Color(0f, 0f, 0f, 0.4f));
		grapher.getPlot().setLayerAlternatingColor(new Color(203, 219, 252));
		grapher.getPlot().setUseWeights(false);
		ArtifactShape<MavenArtifactName> shape = new ArtifactShape<MavenArtifactName>();
		shape.setLabelStrategy(new MavenArtifactLabelStrategy());
		grapher.setShape(shape);
		try (OutputStream output = new FileOutputStream("./target/artifactGraph.svg")) {
			grapher.createSvg(output);
		}
	}
}
