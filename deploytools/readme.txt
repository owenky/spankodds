1£©To deploy spankodds JWS, upload following files to remote location http://Sof300732.com/app/
   libs.jar.pack.gz, spankodds.pack.gz spankodds.jnlp
   NOTE: if libs.jar.pack.gz is already at location http://Sof300732.com/app/ and it has not changed, don't upload it. When JWS detects libs.jar.pack.gz not changed from
         user's local cache, it will skip downloading it to reduce download time.

2) to generate lib.jar.pack.gz, run
   libs.bat
   lib.jar.pack.gz will be generated in target folder

3) to generate spankodds.pack.gz, run
   upd.bat
   spankodds.pack.gz will be generated in target folder

4) spankodds.jnlp is in current folder
----------------------------------------------------------------------------------------------------------
Steps to create maven repository of dependencies in dependencies:  ( use spankodds example)
1) Clone your project in a separate folder
   git clone git@github.com:owenky/spankodds.git sialibs
2) cd sialibs
3) Create a new branch (here named repository)
   git branch repository   #(look like branch name has to be repository)
4) Switch to that branch
   git checkout repository
5) Remove all files except .git folders
   del *.*
6) Install your jar in that directory
   copy *jars from jars folder to sialibs
   copy libs/install.bat to sialibs
   run install.bat
7) add following to dependencies_xml
   <repositories>
           <repository>
               <id>sia-github</id>
               <url> https://github.com/owenky/spankodds/raw/repository</url>
           </repository>
       </repositories>