installer.bat
reg add "HKCU\Software\Microsoft\Command Processor" /v AutoRun ^
  /t REG_EXPAND_SZ /d "%~dp0reo_settings.cmd" /f

