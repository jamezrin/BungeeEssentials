@echo off
cd libs
echo Installing dependencies...
mvn install:install-file -Dfile=waterfall.jar -DgroupId=io.github.waterfallmc -DartifactId=waterfall -Dversion=1.9-SNAPSHOT -Dpackaging=jar