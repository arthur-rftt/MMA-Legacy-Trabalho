@echo off
chcp 65001 >nul
cd /d "%~dp0"
if not exist out mkdir out
javac -encoding UTF-8 -d out -sourcepath src src\mmalegacy\Main.java src\mmalegacy\Testes.java
if errorlevel 1 exit /b 1
java -ea -cp out mmalegacy.Testes
pause
