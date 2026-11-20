@echo off
chcp 65001 >nul
cd /d "%~dp0"
if not exist out mkdir out
javac -encoding UTF-8 -d out -sourcepath src src\mmalegacy\Main.java src\mmalegacy\Testes.java
if errorlevel 1 (
  echo Erro: verifique se o JDK 17 ou superior esta instalado.
  pause
  exit /b 1
)
java -cp out mmalegacy.Main
if errorlevel 1 pause
