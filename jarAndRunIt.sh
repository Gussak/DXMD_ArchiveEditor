#!/bin/bash

set -euEx # xD

javac *.java options/*.java

jar cfve ../DXMD_ArchiveEditor.jar Launcher *
chmod -v +x ../DXMD_ArchiveEditor.jar

# windows dark theme would be: -Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel
# dark theme needs both -D... param and bDarkTheme=true
	bDarkTheme=true \
	fScale=0.65 \
	java \
		-Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel \
		-jar ../DXMD_ArchiveEditor.jar "$(pwd)/../Game.layer.1.all.archive" #it is actually a symlink on my machine
		
#failsafe run in case META-INF fails: java -cp ../DXMD_ArchiveEditor.jar Launcher
