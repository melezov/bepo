@echo off
setlocal
pushd "%~dp0"

:: Get time and date without independent of locale
for /F "usebackq tokens=1,2 delims==" %%i in (`wmic os get LocalDateTime /VALUE 2^>NUL`) do if '.%%i.'=='.LocalDateTime.' set ldt=%%j
set SNAPSHOT=%ldt:~0,8%-%ldt:~8,6%

set HEAD_PACKAGE=com
set PACKAGE=%HEAD_PACKAGE%.mentatlabs.bepo.api

set CLIENT_MODEL_JAR=bepo-client-model-%SNAPSHOT%.jar
set CLIENT_MODEL_SRC=bepo-client-model-%SNAPSHOT%-sources.jar

set SERVER_MODEL_JAR=bepo-server-model-%SNAPSHOT%.jar
set SERVER_MODEL_SRC=bepo-server-model-%SNAPSHOT%-sources.jar

echo Cleaning old client compilation ...
if exist temp\client-compile rmdir /S /Q temp\client-compile
if exist temp\client-output rmdir /S /Q temp\client-output
if exist temp\client-source rmdir /S /Q temp\client-source

mkdir temp\client-compile
mkdir temp\client-output
mkdir temp\client-source

echo Compiling client model to temp\client-output\%CLIENT_MODEL_JAR% ...
java -jar dsl-clc-1.5.1-SNAPSHOT.jar ^
  dsl=dsl ^
  temp=temp\client-compile ^
  compiler=dsl-compiler-1.4.5841.42425.exe ^
  download ^
  namespace=%PACKAGE% ^
  dependencies:java_client=temp\client-dependencies ^
  java_client=temp\client-output\%CLIENT_MODEL_JAR% ^
  settings=manual-json,joda-time
IF ERRORLEVEL 1 goto :error

:: Copy sources so that we can archive them
move temp\client-compile\JAVA_CLIENT\%HEAD_PACKAGE% temp\client-source > NUL

echo Cleaning old server compilation ...
if exist temp\server-compile rmdir /S /Q temp\server-compile
if exist temp\server-output rmdir /S /Q temp\server-output
if exist temp\server-source rmdir /S /Q temp\server-source

mkdir temp\server-compile
mkdir temp\server-output
mkdir temp\server-source

echo Compiling server model to temp\server-output\%SERVER_MODEL_JAR% ...
java -jar dsl-clc-1.5.1-SNAPSHOT.jar ^
  dsl=dsl ^
  temp=temp\server-compile ^
  compiler=dsl-compiler-1.4.5841.42425.exe ^
  download ^
  namespace=%PACKAGE% ^
  dependencies:revenj.java=temp\server-dependencies ^
  revenj.java=temp\server-output\%SERVER_MODEL_JAR% ^
  settings=manual-json ^
  sql=sql ^
  migration ^
  "postgres=localhost:5432/bepo?user=bepo&password=bepo" ^
  apply
IF ERRORLEVEL 1 goto :error

:: Copy sources so that we can archive them
move temp\server-compile\REVENJ_JAVA\%HEAD_PACKAGE% temp\server-source > NUL

:: Format SQL script and Java sources
:: This also "fixes" the paths in database_migration scripts
echo Running code formatter ...
java -jar dsl-clc-formatter-0.2.1.jar ^
  sql ^
  temp\client-source ^
  temp\server-source
IF ERRORLEVEL 1 goto :error

:: Creates the source package
echo Archiving client sources to temp\client-output\%CLIENT_MODEL_SRC% ...
jar cfM temp\client-output\%CLIENT_MODEL_SRC% -C temp\client-source\ .
IF ERRORLEVEL 1 goto :error

echo Archiving server sources to temp\client-output\%SERVER_MODEL_SRC% ...
jar cfM temp\server-output\%SERVER_MODEL_SRC% -C temp\server-source\ .
IF ERRORLEVEL 1 goto :error

:done
popd
goto :EOF

:error
echo An error has occurred, aborting compilation!
goto :done
