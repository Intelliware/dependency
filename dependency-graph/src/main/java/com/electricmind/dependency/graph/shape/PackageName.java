package com.electricmind.dependency.graph.shape;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

class PackageName {
	
	private final String packageName;

	PackageName(String packageName) {
		this.packageName = StringUtils.trimToEmpty(packageName);
	}
	
	int getDepth() {
		return getParts().length;
	}
	
	boolean startsWith(PackageName prefix) {
		return this.packageName.startsWith(prefix.packageName);
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

	int getPartCount() {
		return getParts().length;
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
