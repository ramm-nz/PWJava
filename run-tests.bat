@echo off
REM Playwright Test Execution Script for Windows

setlocal EnableDelayedExpansion

echo ============================================
echo   Playwright Test Execution Script
echo ============================================
echo.

REM Default values
set BROWSER=chromium
set HEADLESS=false
set WORKERS=4
set RETRIES=2
set SCREENSHOT_FAILURE=true
set SCREENSHOT_SUCCESS=false
set PROFILE=default

REM Parse command line arguments
:parse_args
if "%1"=="" goto end_parse
if "%1"=="-b" (
    set BROWSER=%2
    shift
    shift
    goto parse_args
)
if "%1"=="--browser" (
    set BROWSER=%2
    shift
    shift
    goto parse_args
)
if "%1"=="-h" (
    set HEADLESS=true
    shift
    goto parse_args
)
if "%1"=="--headless" (
    set HEADLESS=true
    shift
    goto parse_args
)
if "%1"=="-w" (
    set WORKERS=%2
    shift
    shift
    goto parse_args
)
if "%1"=="--workers" (
    set WORKERS=%2
    shift
    shift
    goto parse_args
)
if "%1"=="-r" (
    set RETRIES=%2
    shift
    shift
    goto parse_args
)
if "%1"=="--retries" (
    set RETRIES=%2
    shift
    shift
    goto parse_args
)
if "%1"=="-p" (
    set PROFILE=%2
    shift
    shift
    goto parse_args
)
if "%1"=="--profile" (
    set PROFILE=%2
    shift
    shift
    goto parse_args
)
if "%1"=="--screenshot-all" (
    set SCREENSHOT_SUCCESS=true
    shift
    goto parse_args
)
if "%1"=="--help" (
    echo Usage: run-tests.bat [OPTIONS]
    echo.
    echo Options:
    echo   -b, --browser ^<name^>     Browser to use (chromium, firefox, webkit) [default: chromium]
    echo   -h, --headless           Run in headless mode [default: false]
    echo   -w, --workers ^<number^>   Number of parallel workers [default: 4]
    echo   -r, --retries ^<number^>   Number of retries for failed tests [default: 2]
    echo   -p, --profile ^<name^>     Maven profile to use (default, ci, firefox, webkit)
    echo   --screenshot-all         Take screenshots for passed tests too
    echo   --help                   Display this help message
    echo.
    echo Examples:
    echo   run-tests.bat -b firefox -w 2
    echo   run-tests.bat --headless -p ci
    echo   run-tests.bat -b webkit --screenshot-all
    exit /b 0
)
shift
goto parse_args

:end_parse

echo Configuration:
echo   Browser: %BROWSER%
echo   Headless: %HEADLESS%
echo   Workers: %WORKERS%
echo   Retries: %RETRIES%
echo   Profile: %PROFILE%
echo   Screenshot on Failure: %SCREENSHOT_FAILURE%
echo   Screenshot on Success: %SCREENSHOT_SUCCESS%
echo.

echo Running tests...
echo.

mvn clean test -P%PROFILE% ^
    -Dbrowser=%BROWSER% ^
    -Dheadless=%HEADLESS% ^
    -Dworkers=%WORKERS% ^
    -Dretries=%RETRIES% ^
    -Dscreenshot.failure=%SCREENSHOT_FAILURE% ^
    -Dscreenshot.success=%SCREENSHOT_SUCCESS%

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Tests completed successfully!
) else (
    echo.
    echo Tests failed!
    exit /b 1
)
