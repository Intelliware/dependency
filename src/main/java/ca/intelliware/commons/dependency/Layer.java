package ca.intelliware.commons.dependency;

import java.util.Collections;
import java.util.Set;

public class Layer<T> {

    private final int level;
    private final Set<T> contents;

    public Layer(int level, Set<T> contents) {
        this.level = level;
        this.contents = Collections.unmodifiableSet(contents);
    }

    public Set<T> getContents() {
        return this.contents;
    }
    public int getLevel() {
        return this.level;
    }

}
