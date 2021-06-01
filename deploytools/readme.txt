1£©To deploy spankodds JWS, upload following files to AWS S3 bucket named app.sof300732.com
   NOTE: if libs.jar.pack.gz is already at location http://app.Sof300732.com and it has not changed, don't upload it. When JWS detects libs.jar.pack.gz not changed from
         user's local cache, it will skip downloading it to reduce download time.

2) to generate lib.jar.pack.gz, run
   libs.bat
   sia-libs-1.0.0.jar.pack will be generated in target folder of libs folder, rename it to lib.jar.pack.gz

3) to generate spankodds.pack.gz, run
   upd.bat
   spankodds.pack.gz will be generated in target folder

4) launch.jnlp is in current folder
=================================================================
To create following github repository
 <repository>
           <id>sia-github</id>
           <url>https://github.com/owenky/spankodds/raw/repository</url>
</repository>

see sialib branch and repository branch README.md