@echo off
echo ==============================================
echo         AI 写作助手 - 启动脚本
echo ==============================================
echo.
echo 请先在 https://platform.deepseek.com/ 注册并获取 API Key
echo API Key 格式: sk-xxx...
echo.

set /p DEEPSEEK_API_KEY=请输入你的 DeepSeek API Key: 

echo.
echo 正在设置环境变量...
set "DEEPSEEK_API_KEY=%DEEPSEEK_API_KEY%"

echo.
echo 正在启动应用...
echo API Key: %DEEPSEEK_API_KEY:~0,5%****
echo.

cd /d "%~dp0"
mvn spring-boot:run
