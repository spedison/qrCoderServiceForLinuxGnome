$JAVA_HOME/bin/java \
-Xms4m -Xmx8m -XX:MaxMetaspaceSize=12m \
-XX:ReservedCodeCacheSize=5m \
-Xss256k -XX:+UseSerialGC \
-Xint \
-jar /mnt/dados/git/QrCoderService/build/libs/qrCoderService.jar

### -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \
