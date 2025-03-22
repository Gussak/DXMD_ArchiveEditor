#!/bin/bash

set -euEx # xD

javac *.java

jar cfve ../DXMD_ArchiveEditor.jar Launcher *
chmod -v +x ../DXMD_ArchiveEditor.jar

# windows: -Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel
fScale=0.65f java -Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel -jar ../DXMD_ArchiveEditor.jar "$(pwd)/Game.layer.1.all.archive" #it is actually a symlink
#failsafe: java -cp ../DXMD_ArchiveEditor.jar Launcher
