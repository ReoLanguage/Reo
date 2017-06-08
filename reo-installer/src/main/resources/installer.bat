echo reo = java -jar %~dp0bin\reo-1.0.jar $* >reo_alias.txt
(echo @echo off  & echo cls  & echo doskey /macrofile=%~dp0reo_alias.txt & echo set CLASSPATH=.;%~dp0bin\reo-runtime-java-lykos.jar;%~dp0bin\reo-runtime-java.jar) >reo_settings.cmd

reo_settings.cmd
