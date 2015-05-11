javabin="$JAVA_HOME/bin/java"
jarbin="$JAVA_HOME/bin/jar"
gradleoptions="-Dfile.encoding=UTF-8 --daemon"

cd ../..

# clean sdk module
gradlew :sdk:clean $gradleoptions

# get all sdk dependencies
mv -f "sdk/build.gradle" "sdk/build.gradle.backup"
rm -rf "sdk/dependencies"
cp "sdkandroid-library/scripts/dependencies.build.gradle" "sdk/build.gradle"
gradlew :sdk:getDependencies $gradleoptions
rm "sdk/build.gradle"

# get only needed dependencies
cd "sdk"
rm -rf "libs"
mkdir "libs"
mv "dependencies/jsoup-1.8.2.jar" "libs/jsoup-1.8.2.jar"
rm -rf "dependencies"
cd ..

# process jar dependencies with jarjar
cd "sdk/libs"
touch rules
echo "rule org.jsoup.** com.lonebytesoft.thetaleclient.sdk.lib.org.jsoup.@1" >> rules
for file in *.jar; do
	"$javabin" -jar "../../sdkandroid-library/scripts/jarjar-1.4.jar" process rules "$file" "$file"
	jardir=${file/.jar/}
	unzip -q "$file" -d "$jardir"
	rm -f "$file"
	cd "$jardir""/META-INF/"
	rm -f DEPENDENCIES* LICENSE* NOTICE*
	cd ..
	"$jarbin" cf ../"$file" com/ META-INF/
	cd ..
	rm -rf "$jardir"/
done
rm rules
cd ../..

# rewrite dependencies in source files
cd "sdk"
rm -rf "src.backup"
cp -r "src" "src.backup"
grep -rl "import org.jsoup" "src" | xargs sed -i "s|^\s*import org.jsoup|import com.lonebytesoft.thetaleclient.sdk.lib.org.jsoup|g" 2>/dev/null
cd ..

# modify source for android
cp -f "sdkandroid-library/scripts/AndroidManifest.xml" "sdk/src/main/AndroidManifest.xml"
cp -f "sdkandroid-library/scripts/AbstractRequest.java" "sdk/src/main/java/com/lonebytesoft/thetaleclient/sdk/AbstractRequest.java"
cp -f "sdkandroid-library/scripts/ObjectUtils.java" "sdk/src/main/java/com/lonebytesoft/thetaleclient/sdk/util/ObjectUtils.java"
cp -f "sdkandroid-library/scripts/RequestUtils.java" "sdk/src/main/java/com/lonebytesoft/thetaleclient/sdk/util/RequestUtils.java"
rm -f "sdk/src/main/java/com/lonebytesoft/thetaleclient/sdk/helper/MapHelper.java"

# assemble aar
cp "sdkandroid-library/scripts/compile.build.gradle" "sdk/build.gradle"
gradlew :sdk:clean :sdk:assemble $gradleoptions
mv -f "sdk/build.gradle.backup" "sdk/build.gradle"
mv -f "sdk/build/outputs/aar/sdk-release.aar" "sdkandroid-library/sdk.aar"

# cleanup
cd "sdk"
rm -rf "src"
mv -f "src.backup" "src"
rm -rf "libs"
