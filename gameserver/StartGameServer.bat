@echo off
title First-Team: Game Server Console
:start
echo Starting GameServer.
echo.
REM java  -cp ./../libs/*;l2jserver.jar net.sf.l2j.gameserver.GameServer
java -server -Xmx4048m  -Dfile.encoding=UTF-8 -Xmx3G -cp config/xml;../serverslibs/*; l2ft.gameserver.GameServer 

if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end
:restart
echo.
echo Server restarted ...
echo.
goto start
:error
echo.
echo Server terminated abnormaly ...
echo.
:end
echo.
echo Server terminated ...
echo.

pause
