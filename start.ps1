Write-Host "==============================================" -ForegroundColor Cyan
Write-Host "         AI 写作助手 - 启动脚本" -ForegroundColor Cyan
Write-Host "==============================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "请先在 https://platform.deepseek.com/ 注册并获取 API Key" -ForegroundColor Yellow
Write-Host "API Key 格式: sk-xxx..." -ForegroundColor Yellow
Write-Host ""

$apiKey = Read-Host "请输入你的 DeepSeek API Key"

Write-Host ""
Write-Host "正在设置环境变量..." -ForegroundColor Green
$env:DEEPSEEK_API_KEY = $apiKey

Write-Host ""
Write-Host "正在启动应用..." -ForegroundColor Green
Write-Host "API Key: $($apiKey.Substring(0,5))****" -ForegroundColor Green
Write-Host ""

Set-Location $PSScriptRoot
mvn spring-boot:run
