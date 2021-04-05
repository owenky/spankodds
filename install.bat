rem run it sialibs folder where jars reside
call mvn install:install-file -Dfile=dependencies_xml -DgroupId=com.sia  -DartifactId=common-client-dependency -Dversion=1.0.0 -Dpackaging=pom -DlocalRepositoryPath=.
