IT'S EASY, WHAT?!
IT'S POWERFUL, WHAT?!
IT HAS SUPPORT FOR JAR LIBRARIES, WHAT?!
USE BCMD FOR ALL YOUR PROJECTS... AND THAT'S THE BOTTOM LINE

Basic Usage:
java -jar bcmd.jar brc

BCMD accepts single-letter arguments in one string. How they are ordered determines the order of which they are executed.

Arguments:
b: Build
r: Run
c: Clean (delete all class files)
j: Create jar

.bcmd File:
BCMD uses a standard properties file to store project settings. If you don't want to deal with manually writing all the settings you can just run BCMD once and it will create the file automatically for you in the current directory.

.bcmd Properties:
cp: Classpath. Syntax depends on your JDK version.
include: Files to include for building. This is usually *.java.
jarinclude: Files to include for creating jars. Multiple files or kinds of files can be specified by separating each with a space.
jdk: Path to your JDK's bin folder. You will likely have to change this.
main: Main class for running and for jars.