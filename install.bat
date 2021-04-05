rem run it sialibs folder where jars reside
call mvn install:install-file -Dfile=dependencies_xml -DgroupId=com.sia  -DartifactId=common-client-dependency -Dversion=1.0.0 -Dpackaging=pom -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=swingx-action-1.6.5-1.jar -DgroupId=com.sia  -DartifactId=swingx-action -Dversion=1.6.5 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=swingx-autocomplete-1.6.5-1.jar -DgroupId=com.sia  -DartifactId=swingx-autocomplete -Dversion=1.6.5 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=swingx-common-1.6.5-1.jar -DgroupId=com.sia  -DartifactId=swingx-common -Dversion=1.6.5 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=swingx-core-1.6.5-1.jar -DgroupId=com.sia  -DartifactId=swingx-core -Dversion=1.6.5 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=swingx-graphics-1.6.5-1.jar -DgroupId=com.sia  -DartifactId=swingx-graphics -Dversion=1.6.5 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=swingx-painters-1.6.5-1.jar -DgroupId=com.sia  -DartifactId=swingx-painters -Dversion=1.6.5 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=swingx-plaf-1.6.5-1.jar -DgroupId=com.sia  -DartifactId=swingx-plaf -Dversion=1.6.5 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=activemq-all-5.15.12.jar -DgroupId=com.sia  -DartifactId=activemq-all -Dversion=5.15.12 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=jide-grids.jar -DgroupId=com.sia  -DartifactId=jide-grids -Dversion=0.0.0 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=jide-common.jar -DgroupId=com.sia  -DartifactId=jide-common -Dversion=0.0.0 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=jide-components.jar -DgroupId=com.sia  -DartifactId=jide-components -Dversion=0.0.0 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=jide-dialogs.jar -DgroupId=com.sia  -DartifactId=jide-dialogs -Dversion=0.0.0 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=jide-dock.jar -DgroupId=com.sia  -DartifactId=jide-dock -Dversion=0.0.0 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=jide-pivot.jar -DgroupId=com.sia  -DartifactId=jide-pivot -Dversion=0.0.0 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=jide-shortcut.jar -DgroupId=com.sia  -DartifactId=jide-shortcut -Dversion=0.0.0 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
call mvn install:install-file -Dfile=keytool-maven-plugin-1.5.jar -DgroupId=org.codehaus.mojo  -DartifactId=keytool-maven-plugin -Dversion=1.5 -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true