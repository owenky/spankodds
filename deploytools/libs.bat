echo off
rem
rem this program packs jars in libs folder and sign it.
rem
echo on
call env.bat
call mvn install -f ../libs_pom_xml
call pns.bat libs %LIBS.VERSION%
if %ERRORLEVEL%  NEQ 0 (
     echo "******* pns.bat libs %LIBS.VERSION% failed ********"
    exit /B 1
)