@echo off
echo ========================================================
echo Stopping FarmConnect Local MySQL Server...
echo ========================================================

"C:\Program Files\MySQL\MySQL Server 8.4\bin\mysqladmin.exe" -u root -proot shutdown
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] MySQL Server has been stopped safely.
) else (
    echo [INFO] MySQL could not be stopped with mysqladmin.
)
