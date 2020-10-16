@rem ***************************************************************************
@rem Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
@rem
@rem See the NOTICE file(s) distributed with this work for additional
@rem information regarding copyright ownership.
@rem
@rem This program and the accompanying materials are made available under the terms
@rem of the MIT License which is available at https://opensource.org/licenses/MIT
@rem
@rem SPDX-License-Identifier: MIT
@rem ***************************************************************************

@echo off

:: start_eclipse.bat
::
:: This batch file can be used to start Eclipse on a Windows system, and
:: automatically detects a proper JDK to use to start Eclipse. This batch file
:: supports Windows XP and above. Only Sun/Oracle JDKs are supported. Both
:: x86 and x64 versions of Eclipse, as well as x86 and x64 versions of
:: Windows, are supported. The batch file automatically detects those versions
:: and looks for a compatible JDK. It selects the most recent compatible JDK
:: found on the system, and then runs Eclipse with it.
::
:: To use this batch file, put it in the Eclipse directory (the directory that
:: contains the "eclipse.exe" file), and run the batch file.
::
:: The batch file contains extensive error checking, for missing files,
:: missing JDKs, incompatible JDKs, etc.


SETLOCAL

:: ############
:: ### PATH ###
:: ############

:: Some users don't have c:\windows\system32 and c:\windows (or whatever they
:: are on their system) in their PATH. This means 'findstr' etc can't be found.

set PATH=%PATH%;%SystemRoot%\system32;%windir%\system32;c:\windows\system32;%SystemRoot%;%windir%;c:\windows


:: ####################
:: ### ECLIPSE_ARCH ###
:: ####################

:: eclipse-modeling-juno-win32.zip has in eclipse.ini:
::   plugins/org.eclipse.equinox.launcher.win32.win32.x86_1.1.200.v20120522-1813

:: eclipse-modeling-juno-win32-x86_64.zip has in eclipse.ini:
::   plugins/org.eclipse.equinox.launcher.win32.win32.x86_64_1.1.200.v20120522-1813


if not exist eclipse.ini (
  echo ERROR: Eclipse not found: "eclipse.ini" file is missing. Please put this batch file in your Eclipse directory.
  pause
  exit /B 1
)

findstr /R "^plugins/org\.eclipse\.equinox\.launcher\.win32\.win32" eclipse.ini > NUL
if %ERRORLEVEL% NEQ 0 (
  echo ERROR: Eclipse not found: "eclipse.ini" file is invalid.
  pause
  exit /B 1
)

findstr /R "^plugins/org\.eclipse\.equinox\.launcher\.win32\.win32" eclipse.ini | findstr /R "x86_64" > NUL
if %ERRORLEVEL% NEQ 0 (
  set ECLIPSE_ARCH=32
) else (
  set ECLIPSE_ARCH=64
)

echo Eclipse: %ECLIPSE_ARCH%-bit


:: ################
:: ### WIN_ARCH ###
:: ################

if defined ProgramFiles(x86) (
  set WIN_ARCH=64
) else (
  set WIN_ARCH=32
)

echo Windows: %WIN_ARCH%-bit


:: ##########################
:: ### Check write access ###
:: ##########################

2>nul (
  >>start-eclipse-test-write-access.tmp echo off
) && (
  echo Write access: OK
) || (
  echo ERROR: Can't write files to the Eclipse directory ^(no write access available^). Please extract Eclipse into a directory where the current user has enough rights to create new files.
  pause
  exit /B 1
)

del start-eclipse-test-write-access.tmp


:: ###############
:: ### JDK_REG ###
:: ###############

if %ECLIPSE_ARCH%==32 (
  if %WIN_ARCH%==32 (
    :: 32-bit Eclipse on 32-bit Windows
    set JDK_REG=HKLM\SOFTWARE\JavaSoft\Java Development Kit

  ) else (
    :: 32-bit Eclipse on 64-bit Windows
    set JDK_REG=HKLM\SOFTWARE\Wow6432Node\JavaSoft\Java Development Kit
  )

) else (
  if %WIN_ARCH%==32 (
    :: 64-bit Eclipse on 32-bit Windows
    echo ERROR: Can't run 64-bit Eclipse on 32-bit Windows. Please download and use a 32-bit version of Eclipse.
    pause
    exit /B 1

  ) else (
    :: 64-bit Eclipse on 64-bit Windows
    set JDK_REG=HKLM\SOFTWARE\JavaSoft\Java Development Kit
  )
)

echo JDK registry key: %JDK_REG%


:: ###############
:: ### JDK_VER ###
:: ###############

if exist start_eclipse.tmp (
  del start_eclipse.tmp
)

reg.exe QUERY "%JDK_REG%" /V CurrentVersion 2>NUL > start_eclipse.tmp
for /F "tokens=2*" %%a in ('findstr REG_SZ start_eclipse.tmp') do set JDK_VER=%%b
::for /F "tokens=2*" %%a in ('reg.exe QUERY "%JDK_REG%" /V CurrentVersion 2^>NUL ^| findstr REG_SZ') do set JDK_VER=%%b
if %ERRORLEVEL% NEQ 0 (
  echo ERROR: Java Development Kit ^(JDK^) not found: Failed to obtain version of JDK. Make sure a %ECLIPSE_ARCH%-bit JDK is installed.
  pause
  exit /B 1
)

echo JDK version: %JDK_VER%

del start_eclipse.tmp

:: ################
:: ### JDK_HOME ###
:: ################

for /F "tokens=2*" %%a in ('reg.exe QUERY "%JDK_REG%\%JDK_VER%" /V JavaHome 2^>NUL ^| findstr REG_SZ') do set JDK_HOME=%%b
if %ERRORLEVEL% NEQ 0 (
  echo ERROR: Java Development Kit ^(JDK^) not found: Failed to obtain location of JDK. Make sure a %ECLIPSE_ARCH%-bit JDK is installed.
  pause
  exit /B 1
)

echo JDK directory: %JDK_HOME%


:: ################
:: ### Test JDK ###
:: ################

echo Testing: "%JDK_HOME%\jre\bin\java" -version
"%JDK_HOME%\jre\bin\java" -version
if %ERRORLEVEL% NEQ 0 (
  echo ERROR: Java test failed.
  pause
  exit /B 1
)


:: ################
:: ### JVM_PATH ###
:: ################

set JVM_PATH=%JDK_HOME%\jre\bin\javaw.exe

if not exist "%JVM_PATH%" (
  echo ERROR: JVM not found: "%JVM_PATH%" file is missing.
  pause
  exit /B 1
)

echo Java Virtual Machine: %JVM_PATH%


:: #####################
:: ### Start Eclipse ###
:: #####################

if not exist eclipse.exe (
  echo ERROR: Eclipse not found: "eclipse.exe" file is missing. Please put this batch file in your Eclipse directory.
  pause
  exit /B 1
)

:: Start Eclipse with JDK.
start eclipse.exe -vm "%JVM_PATH%"


ENDLOCAL
