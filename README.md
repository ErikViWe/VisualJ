# VisualJ: VisualJ
A Java library and BlueJ extension to visualize the program flow and changing of data structures of simple algorithms, as well as their time complexity.

<p align="center">
  <img src="https://img.shields.io/badge/Project%20functional-true-brightgreen.svg">
	 <img src="https://img.shields.io/badge/Language-Java-blue.svg">
	 <img src="https://img.shields.io/badge/Priority-closed-brightgreen.svg">
</p>

To get this running:

1. Run ``git clone https://gitlab.com/VisualJ/VisualJ.git``.
2. Copy the following files to the ``lib/`` subfolder:
 1. ``bluejext.jar`` from BlueJ
 2. [JUNG 2.0.1 and all of its dependencies](https://sourceforge.net/projects/jung/files/jung/jung-2.0.1/jung2-2_0_1.zip/download)
 3. ``jfreechart-1.5.0-SNAPSHOT.jar`` and all of its dependencies (download from [here](https://github.com/jfree/jfreechart) and compile with ``maven clean install -DskipTests``).
 4. ``commons-lang3-3.4.jar`` from Apache Commons Lang (download [here](http://mirror.dkd.de/apache//commons/lang/binaries/commons-lang3-3.4-bin.zip))
 5. ``miglayout-core-5.1-20160712.220015-473.jar`` and ``miglayout-swing-5.1-20160712.220036-473.jar`` from MigLayout (download [here](https://oss.sonatype.org/content/repositories/snapshots/com/miglayout/miglayout-core/5.1-SNAPSHOT/miglayout-core-5.1-20160712.220015-473.jar) and [here](https://oss.sonatype.org/content/repositories/snapshots/com/miglayout/miglayout-swing/5.1-SNAPSHOT/miglayout-swing-5.1-20160712.220036-473.jar))
3. Run both of the provided Ant build scripts.
4. Move ``VisualJ.jar`` to ``BlueJ\lib\userlib``.
5. Move ``VisualJPlugin.jar`` to ``BlueJ\lib\extensions``.

VisualJ was developed for the PSE which is part of the Bachelor's degree program. It is a teamproject, in which the students put theory into practice. 
The following students have worked on this project:
Mehdi Dado
Florian Lanzinger
Robin Schulz
Tobias Stickling
Erik Weinstock

A short presentation of the most important functions can be found here: https://youtu.be/xrSHnGlFBK0
The presentation was held in german.
