@echo off
rem Atalho: sobe a stack Docker numa janela nova.  "subir.cmd down" derruba.
"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\subir-docker.ps1" %*
