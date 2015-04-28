# Builds SDK with dependencies included for Android project
#
# Input:
#     Java 7 SDK
#     $jarjar (jarjar-1.4.jar) : JarJar
#     sdk.build.gradle : build script for this build
# Output:
#     sdk.jar : SDK with dependencies included for Android project
#
# $java must be JAVA_HOME for Java 7 SDK
# JarJar and SDK build script may be updated when needed
# JarJar and build script dependencies must require Java version not higher than 7
# Rules for JarJar must include all libraries that are missing in android.jar
# or whose versions are higher than android.jar's

java="$JAVA_HOME"
javabin="$java/bin/java"
javasrc="$java/src.zip"
jarjar="jarjar-1.4.jar"

project="../.."
sdk="$project/sdk"
sdkbuild="$sdk/build.gradle"
sdksource="$sdk/src/main/java"
sdkjar="$sdk/build/libs/sdk.jar"
current="sdkandroid/scripts"
output="../libs/sdk.jar"

rm -rf javasrc
unzip -q "$javasrc" -d javasrc
rm -rf "$sdksource/javax"
mkdir -p "$sdksource/javax"
cp -r javasrc/javax/naming "$sdksource/javax/naming"
rm -rf javasrc

mv -f "$sdkbuild" "$sdkbuild"".backup"
cp sdk.build.gradle "$sdkbuild"
cd "$project"
gradlew :sdk:clean :sdk:assemble -Dfile.encoding=UTF-8
cd "$current"
mv -f "$sdkbuild"".backup" "$sdkbuild"

rm -rf "$sdksource/javax"

touch rules
echo "rule org.apache.** com.lonebytesoft.thetaleclient.sdk.lib.org.apache.@1" >> rules
echo "rule org.json.** com.lonebytesoft.thetaleclient.sdk.lib.org.json.@1" >> rules
echo "rule javax.naming.** com.lonebytesoft.thetaleclient.sdk.lib.javax.naming.@1" >> rules
echo "rule org.jsoup.** com.lonebytesoft.thetaleclient.sdk.lib.org.jsoup.@1" >> rules

"$javabin" -jar "$jarjar" process rules "$sdkjar" "$output"

rm rules
