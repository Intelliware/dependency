package com.electricmind.dependency.sample.npm;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageLock {

	Map<String, PackageInfo> packages;

	public Map<String, PackageInfo> getPackages() {
		return this.packages;
	}

	public void setPackages(Map<String, PackageInfo> packages) {
		this.packages = packages;
	}

}
