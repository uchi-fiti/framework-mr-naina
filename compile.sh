#!/bin/bash

#!/bin/bash
TEST_FOLDER="../framework-test"

find uchi/ -name "*.java" > sources.txt
javac -cp lib/servlet-api.jar:lib/org.json.jar -d bin @sources.txt

# Package from inside bin/ so there's no bin/ prefix in the jar
cd bin
jar -cvf ../myservlet.jar .
cd ..

cp myservlet.jar $TEST_FOLDER/lib/
echo "JAR built and copied."
