package ca.intelliware.commons.dependency;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class DependencyTestCase {

	protected DirectedGraph<String> createSimpleGraphWithCycles() {
		Set<String> all = asSet("steve", "ken", "diana", "dion", "bc", "play-doh");
		Map<String,Set<String>> efferents = new HashMap<String, Set<String>>();
		efferents.put("steve", asSet("diana", "dion"));
		efferents.put("diana", asSet("dion"));
		efferents.put("ken", asSet("diana", "steve", "bc"));
		efferents.put("dion", asSet("ken"));
		efferents.put("bc", asSet("play-doh"));

		return new DirectedGraph<String>(all, efferents);
	}

	protected DirectedGraph<String> createSimpleGraphWithSelfCycle() {
		Set<String> all = asSet("steve", "ken", "diana", "dion", "bc", "play-doh");
		Map<String,Set<String>> efferents = new HashMap<String, Set<String>>();
		efferents.put("steve", asSet("diana", "dion"));
		efferents.put("diana", asSet("dion"));
		efferents.put("ken", asSet("diana", "steve", "bc"));
		efferents.put("dion", asSet("dion"));
		efferents.put("bc", asSet("play-doh"));

		return new DirectedGraph<String>(all, efferents);
	}
	protected DirectedGraph<String> createSimpleGraphWithNoCycles() {
		Set<String> all = asSet("steve", "ken", "diana", "dion", "bc", "play-doh");
		Map<String,Set<String>> efferents = new HashMap<String, Set<String>>();
		efferents.put("steve", asSet("diana", "dion"));
		efferents.put("diana", asSet("dion"));
		efferents.put("ken", asSet("diana", "steve", "bc"));
		efferents.put("bc", asSet("play-doh"));

		return new DirectedGraph<String>(all, efferents);
	}

	private Set<String> asSet(String... strings) {
		return new HashSet<String>(Arrays.asList(strings));
	}

}
