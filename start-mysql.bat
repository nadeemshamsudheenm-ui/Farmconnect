@echo off
echo ========================================================
echo Starting FarmConnect Local MySQL Server (Port 3306)...
echo ========================================================

netstat -ano | findstr :3306 >nul
if %ERRORLEVEL% EQU 0 (
    echo [INFO] MySQL is already running on port 3306!
    exit /b 0
)

echo [INFO] Launching MySQL Server 8.4 daemon...
start "FarmConnect MySQL Server" /B "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysqld.exe" --defaults-file="%~dp0.mysql\my.ini" --console
timeout /t 3 /nobreak >nul

netstat -ano | findstr :3306 >nul
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] MySQL Server is now running on port 3306.
) else (
    echo [WARNING] MySQL is starting up. Check .mysql\my.ini or logs if it doesn't become available.
)
