#!/bin/sh
# Compile (needs JDK 11+). Output goes to ./build
mkdir -p build
javac -encoding UTF-8 -d build src/vsms/*.java || exit 1
echo "Build complete."
