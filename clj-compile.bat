::@echo off

set ns=main
:: set comp-path=.\src;.\classes
call :setup

:: main "jars":

:: set mycp=%cl%\clojure-1.0.0.jar
set mycp=.\classes\net.xizij.clj.clojure-1.0.0.jar

:: if not "%comp-path%/"=="" call :setcp %comp-path%

call :setcp .\src
call :setcp .\classes

call :setcp %cl%\tlc2

: call :setcp %cl%\duck
: call :setcp %cl%\clojure-contrib.jar

: xnix:   call :setcp %cl%\jline-0.9.94.jar
: call :setcp %cl%\jetty-6.1.15.jar
: call :setcp %cl%\jetty-util-6.1.15.jar
::call :setcp %cl%\servlet-api-2.5-6.1.15.jar
: call :setcp %cl%\servlet-api-2.5-20081211.jar


echo %mycp%
pause

:loop
java -cp %mycp% clojure.main -r
:: -e "(ns %ns%)(in-ns '%ns%)" -i "%ns%.clj" -r
;.\src;.\classes

::pause
goto loop


:setup
set drive=%~d0
set CLOJURE_DIR=%drive%\code\clojure
set cl=%CLOJURE_DIR%\lib
set mycp=
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


