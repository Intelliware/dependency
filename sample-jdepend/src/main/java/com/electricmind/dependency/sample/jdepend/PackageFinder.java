package com.electricmind.dependency.sample.jdepend;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.StringUtils;

class PackageFinder extends DirectoryWalker {

	private final File startDirectory;

	public PackageFinder(File startDirectory) {
		this.startDirectory = startDirectory;
	}

    @SuppressWarnings("unchecked")
	@Override
	protected boolean handleDirectory(File directory, int depth, Collection results) throws IOException {
    	if ("CVS".equals(directory.getName()) || ".svn".equals(directory.getName()) || ".git".equals(directory.getName())) {
    		return false;
    	} else {
    		if (directory.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".class")).length > 0) {
    			results.add(getPackageName(directory));
    		}
    		return super.handleDirectory(directory, depth, results);
    	}
	}

    public String getPackageName(File directory) {
		String rootName = this.startDirectory.getAbsolutePath() + File.separator;
		if (directory.getAbsolutePath().startsWith(rootName)) {
			String fileName = StringUtils.substringAfter(directory.getAbsolutePath(), rootName);
			return fileName.replace(File.separatorChar, '.');
		} else {
			throw new IllegalArgumentException(rootName
					+ " doesn't look like the root of " + directory.getAbsolutePath());
		}
    }

	public Set<String> findAllPackages() throws IOException {
        Set<String> results = new HashSet<String>();
        if (this.startDirectory.exists() && this.startDirectory.isDirectory()) {
        	walk(this.startDirectory, results);
        }
        return results;
    }
}