# Qbranch: Alternative implementation of Microsoft Bond.

This is my fun project of alternative implementation of
[Microsoft Bond](https://github.com/Microsoft/Bond). The project is
trying to implement Bond protocol and API in Java. QBranch is
mostly written in [Kotlin](https://kotlinlang.org) with a mix of 

All real 007 fan will know why I pick this project name. :)

# Difference between QBranch and Microsoft Bond

People may have noticed Bond has an on-going effort to support Java (see
``jvm`` branch of https://github.com/Microsoft/Bond), so a good question
is why does developers want QBranch.

A short question is simple: you may not want QBranch at all. Or a longer
version: you may choose QBranch or Bond/Java based on your project
needs. Unlike official Bond/Java binding, QBranch is more a unofficial,
fun project for myself. The origin of two projects result in multiple
differences:

1. **Different programming languages.** QBranch is written in Kotlin,
   because I want to find a project to learn a new programming language.
   Bond/Java is implemented in Java based on their own consideration of
   maximum compatibility.

2. **Different feature set**. Due to resource limitation, QBranch does not
   try to implement every feature from Bond, while I pick some features
   useful based on my experience of using Bond. For example, QBranch
   implements only CompactBinary protocol for now. I may add more
   features based on feedbacks or my work experiences, but there's not
   guarantee.

3. **Different integration approach.** Bond/Java must follow existing
   design of Bond, e.g. it uses gbc as code generation tool. QBranch
   focus on only Java world, so I can use Java-based tools like ANTLR
   to implement a Bond compiler in Java, which provides better
   integration when using it with other Java build systems like
   Maven/Gradle.

4. **Different design decisions.** As a result of learning Java/Kotlin
   reflection system, QBranch tries to implement Bond protocol, which
   requires minimal generated code but put most decoding/encoding logic
   in core code. Meanwhile, Bond/Java implements most encoding/decoding
   logic in generated code, which may cause performance gain over
   QBranch (to be confirmed, really!) but larger output.

So developers can choose them based on their own consideration. If you
want a complete feature set of Bond with official support, you may go
with official Bond/Java. Or, if you care more about integration with
Java world, you may consider QBranch.

## Compatibility guarantee

No matter how different it is, there's one goal that should be always
kept: QBranch keeps protocol level compatibility with official Bond. Any
byte buffer encoded by Microsoft Bond should be decoded by QBranch
with same output, if the protocol is claimed supported by QBranch.
QBranch will never introduce incompatible extension to existing Bond
protocol.

# Build QBranch

By 2017, QBranch depends on a private gbc implementation in 
[my own Bond fork](https://github.com/fuzhouch/bond) to translate .bond
file to .kt. So developers must follow the steps below to build QBranch.

```
    REM Assume you are working at c:\myproject from a CMD window.
    REM Make sure you have Java 7 or above installed.
 
    cd c:\myproject
    git clone https://github.com/fuzhouch/bond ktbond
    pushd ktbond
    REM Build gbc following guideline in https://github.com/Microsoft/Bond
    popd
    git clone https://github.com/fuzhouch/qbranch qbranch

    pushd qbranch
    gradlew build ^
         -PbondCompilerPath=c:\myproject\ktbond\build\compiler\build\gbc\gbc ^
         -PbondIdlPath=c:\myproject\ktbond\idl\bond
    REM Now you should be able to see all jars in each project.
    popd
