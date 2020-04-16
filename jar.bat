cd target\classes
jar cvf ..\..\jars\utils.jar *
#copy utils.jar ..\..
cd ..\..\
cd jars

#jar cvf utils.jar target\classes\*
mvn install:install-file -DgroupId=com.stockmanagement -DartifactId=stockmanagement-utils -Dversion=1.0.0.FINAL -Dfile=utils.jar -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
Xcopy /E /I * ..
cd ..