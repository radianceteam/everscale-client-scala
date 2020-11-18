# Scala TON-SDK client

##Prerequisites
1. Install on your computer latest [nodejs](https://nodejs.org/en/download/)
2. Install on your computer latest [Rust](https://www.rust-lang.org/tools/install)
3. If you use Windows install on your computer one of below enumerated toolchains:
    1. [Mingw-w64](http://mingw-w64.org/doku.php)
    2. [Cygwin](https://cygwin.com/install.html)
    3. [Visual Studio Code](https://code.visualstudio.com/docs/cpp/config-msvc)

##Project structure
Project contains several subprojects:
1. __native__ - cpp project, that you can edit with any cpp IDE you prefer (CLion, Visual Studio Code etc.).
2. __TON-SDK__ - [git submodule](https://git-scm.com/docs/git-submodule) of TON-SDK repo.
3. __ton_client_scala__ - Scala binding itself.

##How to build
1. By hands.
    * Update git submodule
     
    Select branch in TON-SDK you want to build and execute in the folder __TON-SDK__ console next commands:
    
    ```git checkout <branch>```
    
    ```git pull```
    
    * Build ton_client binary library
    
    In the folder __TON-SDK/ton_client/client__ run nodejs build script:
    
    ```node build```
    
    * Copy header file tonclient.h from __TON-SDK/ton_client/client__ to folder __native/include__
    
    * Compile project native with toolchain you prefer
    
    * Compile project ton_client_scala with sbt compile
    
2. In semi-auto mode
    * Set in build.sbt branch value you want to build
    
    * Select sbt project __TON-SDK__ and run sbt command 
    
    ```buildDependentLib```
    
    * Select sbt project __native__ and run sbt command
    
    ```buildBridge```
    
    * Select sbt project __ton_client_scala__ and run sbt command
    
    ```compile```
    
##How to run
__Important!__:
Before run on Windows add to your environment variable __PATH__ absolute path to folder
__TON-SDK/ton_client/client/build__. 
    
