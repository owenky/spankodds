rem
rem define a set of variables shared by batch programs
rem
rem environment variables to be passed to maven pom must be UPPER CASE !
set current_dir=%CD%
cd ..
set root=%CD%
cd %current_dir%
set LIBS.VERSION=1.0.0
set IMG.VERSION=1.0.0
set JXC.VERSION=1.0.0
set HOME=%HOMEDRIVE%%HOMEPATH%
set upload_dir=%root%\target
set aws_dir=%root%\aws
set maven_home=%HOME%\.m2\repository