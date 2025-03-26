package com.electricmind.dependency.sample.jdepend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.Layer;
import com.electricmind.dependency.Node;

import jdepend.framework.JavaClass;
import jdepend.framework.JavaPackage;

class HtmlDependencyChartExporter {
	
	static class Dependencies {
		private String packageName;
		private int count;
		private Set<String> classes;

		Dependencies(String packageName, int count, Set<String> classes) {
			this.packageName = packageName;
			this.count = count;
			this.classes = classes;
		}
		
		public Set<String> getClasses() {
			return classes;
		}
		
		public String getPackageName() {
			return packageName;
		}
		
		public int getCount() {
			return count;
		}
	}

	private File file;

	HtmlDependencyChartExporter(File file) {
		this.file = file;
	}
	
	public void export(DependencyManager<String> dependencyManager,
			Collection<JavaPackage> jdepentResults) throws IOException {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.file))) {
			
			writer.write("<html>");
			writer.newLine();
			writer.write("<head>");
			writer.newLine();
			writer.write("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.11.8/umd/popper.min.js\" integrity=\"sha512-TPh2Oxlg1zp+kz3nFA0C5vVC6leG/6mm1z9+mA81MI5eaUVqasPLO8Cuk4gMF4gUfP5etR73rgU/8PNMsSesoQ==\" crossorigin=\"anonymous\" referrerpolicy=\"no-referrer\"></script>");
			writer.write("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.2.3/css/bootstrap.min.css\" integrity=\"sha512-SbiR/eusphKoMVVXysTKG/7VseWii+Y3FdHrt0EpKgpToZeemhqHeZeLWLhJutz/2ut2Vw1uQEj2MbRF+TVBUA==\" crossorigin=\"anonymous\" referrerpolicy=\"no-referrer\" />");
			writer.write("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.2/js/bootstrap.min.js\" integrity=\"sha512-WW8/jxkELe2CAiE4LvQfwm1rajOS8PHasCCx+knHG0gBHt8EXxS6T6tJRTGuDQVnluuAvMxWF4j8SNFDKceLFg==\" crossorigin=\"anonymous\" referrerpolicy=\"no-referrer\"></script>");
			writer.write("</head>");
			writer.newLine();
			writer.newLine();
			writer.write("<body><div class=\"container\">");
			writer.newLine();
			writer.write("<h1>Package Analysis</h1>");
			writer.newLine();
			writer.write("<table class=\"table table-sm table-bordered table-hover\">");
			writer.newLine();
		
			
			List<String> packageNames = new ArrayList<>(); 
			for (Layer<Node<String>> layer : dependencyManager.getLayeredGraph().getLayers()) {
				for (Node<String> node : layer.getContents()) {
					packageNames.add(node.getName());
				}
			}
			
			writer.write("<thead><tr><th colspan=\"2\"></th>");
			writer.newLine();
			for (String name : packageNames) {
				writer.write("<th>");
				writer.write("" + (packageNames.indexOf(name) + 1));
				writer.write("</th>");
				
			}
			writer.write("</tr></thead>");
			writer.newLine();
			writer.write("<tbody>");
			writer.newLine();

			for (Layer<Node<String>> layer : dependencyManager.getLayeredGraph().getLayers()) {
				String backgroundClassName = "";
				if (layer.getLevel() % 2 == 0) {
					backgroundClassName = "bg-light";
				}
				for (Node<String> node : layer.getContents()) {
					writer.write("<tr>");
					writer.newLine();
					
					writer.write("<td class=\"" + backgroundClassName + "\">");
					writer.write("" + (packageNames.indexOf(node.getName()) + 1));
					writer.write("</td>");
					writer.newLine();
					writer.write("<td class=\"" + backgroundClassName + "\">");
					writer.write(node.getName());
					writer.write("</td>");
					writer.newLine();
					
					for (String name : packageNames) {
						if (name.equals(node.getName())) {
							writer.write("<td class=\"bg-secondary\">");
						} else {
							writer.write("<td class=\"text-center " + backgroundClassName + "\">");
							Dependencies dependencies = dependencyCount(name, node, jdepentResults);
							if (dependencies.getCount() > 0) {
								writer.write("<div data-bs-toggle=\"popover\" data-bs-html=\"true\" data-bs-trigger=\"hover focus\" "
										+ "data-bs-content=\"Classes that depend on &lt;b&gt;" + dependencies.getPackageName() + "&lt;/b&gt;:&lt;br /&gt;&lt;br /&gt;"
										+ dependencies.getClasses()
											.stream()
											.map(c -> StringUtils.substringAfterLast(c, "."))
											.collect(Collectors.joining("&lt;br /&gt;"))
										+ "\" >");
								writer.write("" + dependencies.getCount());
								writer.write("</div>");
							} else {
								writer.write("&nbsp");
							}
						}
						writer.write("</td>");
						writer.newLine();
						
					}

					writer.write("</tr>");
					writer.newLine();
				}
			}

			writer.write("</tbody></table>");
			writer.newLine();
			writer.write("</div>");
			writer.newLine();
			writer.write("<script type=\"text/javascript\">");
			writer.newLine();
			writer.write("const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle=\"popover\"]'));");
			writer.newLine();
			writer.write("var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {");
			writer.newLine();
			writer.write("  return new bootstrap.Popover(popoverTriggerEl)");
			writer.newLine();
			writer.write("});");
			writer.newLine();
			writer.write("</script>");
			writer.newLine();
			writer.write("</body>");
			writer.newLine();
			writer.write("</html>");
			writer.newLine();
		}		
	}

	private Dependencies dependencyCount(String name, Node<String> node, Collection<JavaPackage> jdepentResults) {
		List<JavaPackage> jList = jdepentResults
				.stream()
				.filter(j -> j.getName().equals(node.getName()))
				.collect(Collectors.toList());
		JavaPackage jPackage = jList.get(0);
		
		int count = 0;
		Set<String> classes = new HashSet<>();
		for (JavaClass jClass : jPackage.getClasses()) {
			List<JavaPackage> items = jClass.getImportedPackages().stream().filter(p -> p.getName().equals(name)).collect(Collectors.toList());
			if (items.size() > 0) {
				classes.add(jClass.getName());
			}
			
			count += items.size();
		}
		
		return new Dependencies(name, count, classes);
	}


}
