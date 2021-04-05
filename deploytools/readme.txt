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