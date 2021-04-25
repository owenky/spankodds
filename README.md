# spankodds
Do NOT delete this branch
contains third party jars and poms ( dependencies_xml) for repository of dependencies of  spankodds client applications. 
Those jars are used for create repository of maven dependencies by install.bat in repository/install.bat
JARS and dependencies_xml are localated in branch jars. To create maven repository, copy jars and dependencies_xml from jars branch to branch repository and run install.bat

activemq-all-5.15.12.jar is no longer used. It is replaced by
<dependency>
<groupId>org.apache.activemq</groupId>
<artifactId>activemq-core</artifactId>
<version>5.7.0</version>
</dependency>
