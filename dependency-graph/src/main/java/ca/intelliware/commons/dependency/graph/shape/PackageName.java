package ca.intelliware.commons.dependency.graph.shape;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

class PackageName {
	
	private final String packageName;

	PackageName(String packageName) {
		this.packageName = StringUtils.trimToEmpty(packageName);
	}
	
	int getDepth() {
		return getParts().length;
	}
	
	PackageName removePrefix(PackageName prefix) {
		String[] parts = getParts();
		return new PackageName(StringUtils.join(ArrayUtils.subarray(parts, prefix.getDepth(), parts.length), "."));
	}
	
	PackageName getCommonPrefix(PackageName other) {
		String[] parts = getParts();
		String[] otherParts = other.getParts();
		int index = 0;
		for (int i = 0, length = Math.min(parts.length, otherParts.length); i < length; i++) {
			if (StringUtils.equals(parts[i], otherParts[i])) {
				index = i+1;
			} else {
				break;
			}
		}
		return new PackageName(StringUtils.join(ArrayUtils.subarray(parts, 0, index), "."));
	}

	private String[] getParts() {
		return StringUtils.split(this.packageName, ".");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj.getClass() != getClass()) {
			return false;
		} else {
			PackageName that = (PackageName) obj;
			return new EqualsBuilder().append(this.packageName, that.packageName).isEquals();
		}
	}
	
	@Override
	public String toString() {
		return this.packageName;
	}
}
