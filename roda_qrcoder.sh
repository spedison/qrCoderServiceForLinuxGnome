$JAVA_HOME/bin/java \
-Xms4m -Xmx16m -XX:MaxMetaspaceSize=24m \
-XX:ReservedCodeCacheSize=10m \
-Xss256k -XX:+UseSerialGC \
-Xint \
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \
-jar ./build/libs/qrCoderService.jar