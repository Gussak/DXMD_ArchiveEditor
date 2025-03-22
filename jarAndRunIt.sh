#!/bin/bash

jar cvf ../DXMD_ArchiveEditor.jar *
chmod -v +x ../DXMD_ArchiveEditor.jar

if ! java -jar ../DXMD_ArchiveEditor.jar;then
	java -cp ../DXMD_ArchiveEditor.jar Launcher
fi
