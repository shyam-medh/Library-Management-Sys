@echo off
echo Compiling...
javac -encoding UTF-8 -d build/classes -cp "lib\*" src\*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)
echo Compilation successful!
echo Starting Library Management System...
java -cp "build\classes;lib\*" Loading1
