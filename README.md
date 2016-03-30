![Intelliware Logo](http://www.intelliware.com/wp-content/uploads/intelliware-logo.png "Intelliware Software Development")

# Description

This software component is a small Java module used to analyze a set of dependencies
between things and determine "layers" of the dependency. Basically, the dependency 
tree can be imagined as a [directed graph](https://en.wikipedia.org/wiki/Directed_graph).

Layers work like this: 

1. Items that depend on no other things can be imagined as layer 0.
2. Items that depend on only items from layer 0 make up layer 1.
3. Items that depend on things from layer 1 (plus, possibly, items from layer 0) make up
layer 2.
4. And so forth...

In an ideal world, layers a non-cyclic, but many networks of dependencies fail to be 
non-cyclic. This code can still layerize cyclic graphs using a mathematical algorithm 
called the Greedy Cycle-Removal Algorithm. 

This code has been open-sourced since 2008, but hasn't really been in a place that's 
easy to find.

# What Was This Code used for?

Originally, this code was written to help analyze Java package dependencies.  It was 
inspired by [Edwin Hautus' paper](http://ehautus.home.xs4all.nl/papers/PASTA.pdf) on 
static package analysis (and the PASTA tool). I was a big fan of PASTA's approach to 
package visualization, but felt that the tool was limited in the way it handled 
subpackages. This dependency code was written as a first piece of an alternative 
package dependency analysis tool.

Subsequently, this code was used in other domains, including the processing of HL7 
assets (including, for example, analyzing dependencies between HL7 MIF files and 
graphing the relationships between different HL7 message parts).

