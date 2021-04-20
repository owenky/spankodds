keytool -genkey -alias sia -keyalg RSA -keysize 2048 -keystore sia.jks -dname "CN=Sports Information Analysis LLC,OU=IT, O=Sports Information Analysis LLC, L=Manalapan , ST=NJ,C=USA"  < siakeystorepass
keytool -certreq -alias sia -file sia.csr -keystore sia.jks < siakeystorepass
echo copy sia.csr to digicit