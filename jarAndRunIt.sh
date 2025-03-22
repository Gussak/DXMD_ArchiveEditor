#!/bin/bash

set -x

javac *.java

jar cfve ../DXMD_ArchiveEditor.jar Launcher *
chmod -v +x ../DXMD_ArchiveEditor.jar

java -jar ../DXMD_ArchiveEditor.jar
#failsafe: java -cp ../DXMD_ArchiveEditor.jar Launcher
