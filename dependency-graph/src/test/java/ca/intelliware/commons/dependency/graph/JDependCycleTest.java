package ca.intelliware.commons.dependency.graph;

import java.io.IOException;
import java.util.Collection;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import junit.framework.TestCase;

public class JDependCycleTest extends TestCase {

    private JDepend jdepend;

    protected void setUp() throws IOException {

        jdepend = new JDepend();

        jdepend.addDirectory("target/classes");
        jdepend.addDirectory("target/test-classes");
    }

    /**
     * Tests that a package dependency cycle does not
     * exist for any of the analyzed packages.
     */
    @SuppressWarnings("unchecked")
	public void testAllPackages() {
    	Collection<JavaPackage> packages = jdepend.analyze();
    	for (JavaPackage javaPackage : packages) {
    		assertFalse(javaPackage.getName() + " contains cycles", javaPackage.containsCycle());
		}
    }
}