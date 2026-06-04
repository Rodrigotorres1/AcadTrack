# Libera a porta 8080 se estiver ocupada por Java e sobe o backend.
# Uso (na raiz do repo ou de qualquer lugar):
#   powershell -ExecutionPolicy Bypass -File .\scripts\start-backend.ps1
#
# Com porta alternativa:
#   powershell -ExecutionPolicy Bypass -File .\scripts\start-backend.ps1 -Port 8081

param([int] $Port = 8080)

$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
Set-Location $root

$conns = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue
if ($conns) {
    $procId = $conns | Select-Object -ExpandProperty OwningProcess -First 1
    try {
        $proc = Get-Process -Id $procId -ErrorAction Stop
        if ($proc.ProcessName -match '^java') {
            Stop-Process -Id $procId -Force
            Write-Host "Porta $Port liberada (Java PID $procId encerrado)." -ForegroundColor Green
            Start-Sleep -Milliseconds 500
        } else {
            Write-Host "Porta $Port ocupada por $($proc.ProcessName) (PID $procId) - nao encerrei." -ForegroundColor Yellow
        }
    } catch {
        Write-Host "Nao consegui encerrar PID $procId na porta $Port." -ForegroundColor Yellow
    }
} else {
    Write-Host "Porta $Port livre." -ForegroundColor DarkGray
}

Write-Host ""
Write-Host "Subindo backend na porta $Port..." -ForegroundColor Cyan

if ($Port -eq 8080) {
    & "$root\mvnw.cmd" -pl apresentacao-backend -am spring-boot:run
} else {
    $jvmArg = "-Dserver.port=$Port"
    & "$root\mvnw.cmd" -pl apresentacao-backend -am spring-boot:run "-Dspring-boot.run.jvmArguments=$jvmArg"
}
