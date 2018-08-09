@echo off
echo What address would you like to connect to?
set /p UserInputIP=
java -jar Agent.jar %UserInputIP%
pause