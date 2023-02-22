@echo off
REM Remove any existing keystore, certificates
DEL MyServerKeystore.jks server.key server.pem

REM Create a new server key based on configuration file
echo Generating Key based on config file
openssl req -new -x509 -config GenKey.conf -newkey rsa:2048 -days 365 -nodes -keyout server.key -out server.pem

REM convert to PKCS12 format
echo Converting to PKS12 format
openssl pkcs12 -export -in server.pem -inkey server.key -name server -out server.p12

REM add it to the Java Keystore
REM keytool -import -storepass 123456 -alias server -file server.p12 -keystore MyServerKeystore.jks
echo Adding to MyServerKeystore.jks
keytool -importkeystore -deststorepass 123456 -destkeystore MyServerKeystore.jks -srckeystore server.p12 -srcstoretype PKCS12

REM don't need the .p12 file now
DEL server.p12