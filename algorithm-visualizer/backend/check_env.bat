@echo off
echo Checking Java installation...
java -version
echo.

echo Checking Maven installation...
mvn -v
echo.

echo Checking environment variables...
echo JAVA_HOME: %JAVA_HOME%
echo PATH: %PATH%
echo.

echo Running Maven clean install...
call mvn clean install -e

if %ERRORLEVEL% NEQ 0 (
    echo Maven build failed with error level %ERRORLEVEL%
    pause
) else (
    echo Maven build completed successfully
    pause
)
