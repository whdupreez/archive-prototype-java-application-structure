@echo off
setlocal EnableExtensions EnableDelayedExpansion

REM ---------------------------------------------------------------------------
REM  Manages the application service.
REM
REM  Copyright 2014 Willy du Preez
REM ---------------------------------------------------------------------------

REM TODO Install as User
REM TODO Check if JAVA_HOME set
REM TODO Command update

REM ---------------------------------------------------------------------------
REM  Initialize
REM ---------------------------------------------------------------------------

REM - Determine the location of APP_HOME.
set "BATCH_DIR=%~dp0%"
cd > nul
pushd %BATCH_DIR%..
set "APP_HOME=%CD%"
popd
set BATCH_DIR=

REM - Set the prunsrv.exe executable and defaults.
if "%PROCESSOR_ARCHITECTURE%"=="AMD64" (
   echo Using the X86-64bit version of prunsrv
   set "PRUNSRV=%APP_HOME%\system\daemon\windows\amd64\prunsrv.exe"
) else (
   echo Using the X86-32bit version of prunsrv
   set "PRUNSRV=%APP_HOME%\system\daemon\windows\prunsrv.exe"
)

set LOG_PATH=%APP_HOME%\data\log
set LOG_LEVEL=INFO

set STOP_TIMEOUT=30

set STARTUP_MODE=manual

REM - Load the Service Configuration.
if [%APPLICATION_CONF%]==[] (
   set "APPLICATION_CONF=%APP_HOME%\bin\application.conf.bat"
)
if exist "%APPLICATION_CONF%" (
   call "%APPLICATION_CONF%" %*
) else (
   echo Error: Config file not found "%APPLICATION_CONF%"
   goto :eof
)

REM ---------------------------------------------------------------------------
REM  Determine Command
REM ---------------------------------------------------------------------------

if [%1]==[]              goto cmdUsage
if /I [%1]==[install]    goto cmdInstall
if /I [%1]==[uninstall]  goto cmdUninstall
if /I [%1]==[update]     goto cmdUpdate
if /I [%1]==[run]        goto cmdRun
if /I [%1]==[start]      goto cmdStart
if /I [%1]==[stop]       goto cmdStop
if /I [%1]==[restart]    goto cmdRestart

goto :cmdUsage

REM ---------------------------------------------------------------------------
REM  Execute Command
REM ---------------------------------------------------------------------------

:cmdUsage

echo(
echo Manages the application service.
echo(
echo SERVICE install^|uninstall^|start^|stop^|restart [args]
echo(
echo   install       Installs the service.
echo   uninstall     Uninstalls the service.
echo   update        Updates the service parameters.
echo   run           Run as console application.
echo   start         Starts the service.
echo   stop          Stops the service.
echo   restart       Stops and starts the service.
echo(
echo   [args]        Command specific arguments. See description below.
echo(
echo(
echo Install args:
echo(
echo   test          Test

goto :eof

:cmdInstall

%PRUNSRV% install %APP_NAME%^
 --Install %PRUNSRV%^
 --DisplayName=%APP_DISPLAYNAME%^
 --Description %APP_DESCRIPTION%^
 --LogLevel=%LOG_LEVEL% --LogPath="%LOG_PATH%" --LogPrefix=service^
 --StdOutput=auto --StdError=auto^
 --Classpath=%APP_HOME%\..\target\java-application-structure-0.0.1-SNAPSHOT.jar^
 --StartMode=jvm --StartClass=com.willydupreez.prototype.structure.Launcher^
 --StartParams start;"%APP_HOME%"^
 --StopParams stop^
 --StopMode=jvm --StopClass=com.willydupreez.prototype.structure.Launcher^
 --StopTimeout=%STOP_TIMEOUT%^
 --Startup=%STARTUP_MODE%^
 --PidFile=app-pid

goto :eof

:cmdUninstall

%PRUNSRV% stop %APP_NAME%
if [%errorlevel%]==[0] (
  %PRUNSRV% delete %APP_NAME%
) else (
  echo Failed to stop %APP_NAME%
)

goto :eof

:cmdUpdate

%PRUNSRV% stop %APP_NAME%
if [%errorlevel%]==[0] (
   %PRUNSRV% update %APP_NAME%
) else (
   echo Failed to stop %APP_NAME%
)

goto :eof

:cmdRun

%PRUNSRV% run %APP_NAME%

goto :eof

:cmdStart

%PRUNSRV% start %APP_NAME%

goto :eof

:cmdStop

%PRUNSRV% stop %APP_NAME%

goto :eof

:cmdRestart

%PRUNSRV% stop %APP_NAME%
if [%errorlevel%]==[0] (
   %PRUNSRV% start %APP_NAME%
) else (
   echo Failed to stop %APP_NAME%
)

goto :eof
