echo off
rem
rem this program packs jars in libs folder and sign it.
rem
echo on
call env.bat
call mvn clean install -f ../libs/libs_pom_xml