package com.electricmind.dependency.maven.pom;

import java.util.ArrayList;
import java.util.List;

public class VersionNumber implements Comparable<VersionNumber> {
	
	static class Part implements Comparable<Part> {
		String s;
		
		Part(String s) {
			this.s = s;
		}

		@Override
		public int compareTo(Part o) {
			if (isNumeric(this.s) && isNumeric(o.s)) {
				Integer v1 = Integer.valueOf(this.s);
				Integer v2 = Integer.valueOf(o.s);
				return v1.compareTo(v2);
			} else {
				return this.s.compareTo(o.s);
			}
		}
		
		@Override
		public String toString() {
			return this.s;
		}
	}

	private String version;

	public VersionNumber(String version) {
		this.version = version;
	}
	
	static boolean isNumeric(String s) {
		return s.matches("[0-9]*");
	}

	@Override
	public int compareTo(VersionNumber version) {
		List<Part> parts1 = getParts();
		List<Part> parts2 = version.getParts();
		
		int result = 0;
		for (int i = 0; i < Math.max(parts1.size(), parts2.size()); i++) {
			if (i >= parts1.size()) {
				result = -1;
				break;
			} else if (i >= parts2.size()) {
				result = 1;
				break;
			} else {
				Part part1 = parts1.get(i);
				Part part2 = parts2.get(i);
				
				
				result = part1.compareTo(part2);
				if  (result != 0) {
					break;
				}
			}
		}
		return result;
	}
	
	private List<Part> getParts() {
		String[] parts = this.version.split("\\.");
		List<Part> result = new ArrayList<>();
		for (int i = 0; i < parts.length; i++) {
			result.add(new Part(parts[i]));
		}
		return result;
	}

	public boolean isGreaterThan(VersionNumber version) {
		return compareTo(version) > 0;
	}

	public String getVersion() {
		return this.version;
	}
}
