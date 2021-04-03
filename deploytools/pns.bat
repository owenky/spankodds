rem
rem This program is NOT for running alone. It is to be called by another script.
rem this program repacks a jar using P200Ant.jar and sign it via ant.
rem The reason of using ant to carry out the task instead of by a batch program is ant is platform independent.
rem Same ant build file can work on either unix or dos environment.
rem
echo off
if "%2" == "" (
    echo "Usage pns.bat <artifact> <version>"
    echo "NOTE this program is designed to be called by another program"
    goto END
)
call env.bat
del /Q %upload_dir%\%1-%2.jar.pack.gz
call ant -lib "%JAVA_HOME%\jre\lib" -buildfile pns.xml packAndSign -Dtarget_path=%upload_dir%\%1\%2 -Djar_file=%1-%2.jar
del /Q %upload_dir%\%1.jar.pack.gz
rename %upload_dir%\%1-%2.jar.pack.gz  %1.jar.pack.gz

:END
