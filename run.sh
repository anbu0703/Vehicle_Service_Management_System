#!/bin/sh
# Usage: ./run.sh          -> connect to MySQL (config/db.properties)
#        ./run.sh --demo   -> sample data, no MySQL needed
java -cp "build:lib/*" vsms.App "$@"
