# BCMD Manual
## Usage Example
```java -jar bcmd.jar brc```

```java -jar``` launches the jar file. If you have the correct file associations, you may be able to omit this.

Additionally, if you built BCMD's jar without a main class you may have to specify it.

```brc``` represents the command portion. The commands are to be put together in one word, and are executed in the order they are placed.

In this case, ```b``` (build) is ran first,

```r``` (run) is done second,

and ```c``` (clean) is executed when run finishes.

## Available Commands
```b```: Build (compile) java files as specified in .bcmd into class files.

```r```: Run the project from the class specified in .bcmd.

```c```: Clean (delete) all lingering class files from the directory.