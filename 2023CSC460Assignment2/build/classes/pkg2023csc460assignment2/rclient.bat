@echo off

REM use:
REM   rclient [port] [host] [lang] [protocol]
REM   Languages: Java Python
REM   Protocols: UDP TCP SSL

SET HOST=""
SET PORT=4269
SET PROTOCOL="TCP"
SET LANG="Java"

REM go through provided command line arguments
:Loop
IF "%1"=="" GOTO Continue  
	REM what is this command line argument?
	REM anything other than digits present?
	REM https://stackoverflow.com/questions/17584282/how-to-check-if-a-parameter-or-variable-is-a-number-in-a-batch-script
	SET "var="&for /f "delims=0123456789" %%i in ("%1") do set var=%%i
	if defined var (SET HOST=%1)	
	
SHIFT
GOTO Loop
:Continue

REM Did they provide a host?
IF %HOST%=="" (
	SET /P "HOST=Enter the host name/IP (default 'localhost'): " || SET "HOST=localhost"
)

IF %LANG%==Java (
	REM Compile (if necessary) and run the appropriate java program

	cd Java
	IF %PROTOCOL%==TCP (	
		REM need to compile first?
		IF NOT EXIST TcpClient.class ( 
			javac TcpClient.java
			REM bail out if there is an error
			if errorlevel 1 ( EXIT /B )
		)
		
		REM run it now
		java TcpClient %HOST% %PORT%
	) 		
	cd ..
)
REM echo Host %HOST%
REM echo Port %PORT%
REM echo Protocol %PROTOCOL%
REM echo Language %LANG%