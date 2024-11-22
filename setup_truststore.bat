@echo off
chcp 65001 >nul 2>&1

set TRUSTSTORE_PATH=%cd%\src\main\resources\smtp_truststore.jks
set TRUSTSTORE_PASS=changeit

keytool -list -v -keystore "%TRUSTSTORE_PATH%" -storepass %TRUSTSTORE_PASS%
if %errorlevel% neq 0 (
    echo TrustStore 설정 실패. 인증서를 확인하세요.
    exit /b 1
) else (
    echo TrustStore 설정 완료.
)
