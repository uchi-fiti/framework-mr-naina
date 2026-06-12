#!/bin/bash

#!/bin/bash
find uchi/ -name "*.java" > sources.txt
javac -cp lib/servlet-api.jar -d bin @sources.txt

# Package from inside bin/ so there's no bin/ prefix in the jar
cd bin
jar -cvf ../myservlet.jar .
cd ..

cp myservlet.jar ../framework-test/lib/
echo "JAR built and copied."
