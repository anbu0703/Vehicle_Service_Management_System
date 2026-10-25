@echo off
if not exist build mkdir build
javac -encoding UTF-8 -d build src\vsms\*.java
echo Build complete.
