@echo off

REM use:
REM   rserver [port] [lang] [protocol]
REM   Languages: Java Python
REM   Protocols: UDP TCP SSL

SET PORT=4269
SET PROTOCOL="TCP"
SET LANG="Java"

IF %LANG%==Java (
	cd Java
		
	REM Compile (if necessary) and run the appropriate java program
	IF %PROTOCOL%==TCP (	
		REM need to compile first?
		IF NOT EXIST TcpServer.class ( 
			javac TcpServer.java
			REM bail out if there is an error
			if errorlevel 1 ( EXIT /B )
		)
		
		REM run it now
		java TcpServer %PORT%
	)
	cd ..
) 

REM echo Host %HOST%
REM echo Port %PORT%
REM echo Protocol %PROTOCOL%
REM echo Language %LANG%