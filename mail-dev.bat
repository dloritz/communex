@echo off

set ns=main

call :setup 


:: main "jars":

set mycp=%cl%\clojure-1.0.0.jar
call :setcp %cl%\tlc2



call :setcp %cl%\duck
call :setcp %cl%\clojure-contrib.jar

::xnix:   call :setcp %cl%\jline-0.9.94.jar
call :setcp %cl%\jetty-6.1.15.jar
call :setcp %cl%\jetty-util-6.1.15.jar
::call :setcp %cl%\servlet-api-2.5-6.1.15.jar
call :setcp %cl%\servlet-api-2.5-20081211.jar


:loop
java -cp %mycp% clojure.main -e "(ns %ns%)(in-ns '%ns%)" -i "%ns%.clj" -r
::pause
goto loop


:setup
set drive=%~d0
set CLOJURE_DIR=%drive%\code\clojure
set cl=%CLOJURE_DIR%\lib
call :thisfol
echo %tf%
goto :eof



:setcp
set mycp=%mycp%;%1
goto :eof

:thisfol
set p=%~p0
set p=%p:~0,-1%
set tf=
:tfloop
if [%p:~-1%]==[\] goto :eof
set tf=%p:~-1%%tf%
set p=%p:~0,-1%
goto :tfloop

:: jline.ConsoleRunner
:: clojure.lang.Repl cljinit.clj


