@echo off
rem Usage: run.bat            -> connect to MySQL (config\db.properties)
rem        run.bat --demo     -> sample data, no MySQL needed
java -cp "build;lib/*" vsms.App %*
