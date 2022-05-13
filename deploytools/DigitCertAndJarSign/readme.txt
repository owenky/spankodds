To Renew certificate from digitcert.com

1) renew certificate in digitcert account
   need to upload sia.csr to digitcert, if sia.csr does not exist, run genCsr.bat to generate one
2) after renewal approved, download *.p7b from digitcert and overwrite existing sia.p7b with downloaded one.
3) run keyimport.bat to update sia.jks ( existing sia.jks expires ). sia.jks is used by code sigining program defined in pomn.xml.