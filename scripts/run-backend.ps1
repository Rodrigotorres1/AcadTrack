# Sobe o Spring Boot em apresentacao-backend sem depender de `mvn` no PATH global.
# Por defeito: primeira porta livre entre 8080 e 8299 (omitir `-Port` equivale a -Port 0).
#
# Fixar porta (falha se estiver ocupada):
#   powershell -ExecutionPolicy Bypass -File .\scripts\run-backend.ps1 -Port 9002
#
# Mais argumentos do Maven depois de -- :
#   powershell -ExecutionPolicy Bypass -File .\scripts\run-backend.ps1 -- -Dmaven.test.skip=true

param(
    # 0 = primeira porta livre no intervalo ListenFrom .. ListenTo. Qualquer valor > 0 usa essa porta (falha se ocupada).
    [int] $Port = 0,
    # Início da pesquisa no modo automatico ($Port -eq 0).
    [int] $ListenFrom = 8080,
    # Fim inclusivo.
    [int] $ListenTo = 8299
)

$ErrorActionPreference = 'Stop'

function Test-TcpListenPortOccupied([int] $TcpPort) {
    $listening = @(Get-NetTCPConnection -LocalPort $TcpPort -State Listen -ErrorAction SilentlyContinue)
    return ($listening.Count -gt 0)
}

function Select-FirstFreePort([int] $Start, [int] $Finish) {
    if ($Finish -lt $Start) {
        Write-Host 'ERRO: ListenTo deve ser >= ListenFrom.' -ForegroundColor Red
        return $null
    }
    for ($p = $Start; $p -le $Finish; $p++) {
        if (-not (Test-TcpListenPortOccupied $p)) {
            return $p
        }
    }
    return $null
}

$RepoRoot = Split-Path $PSScriptRoot -Parent
if (-not (Test-Path (Join-Path $RepoRoot 'pom.xml'))) {
    Write-Host 'ERRO: Execute a partir do repositorio (nao encontrei pom.xml acima de scripts/).' -ForegroundColor Red
    exit 1
}

$mvnExe = $null
if ($env:MAVEN_HOME -and (Test-Path (Join-Path $env:MAVEN_HOME 'bin\mvn.cmd'))) {
    $mvnExe = Join-Path $env:MAVEN_HOME 'bin\mvn.cmd'
}
if (-not $mvnExe) {
    $candidate = Join-Path $env:USERPROFILE 'tools\apache-maven-3.9.9\bin\mvn.cmd'
    if (Test-Path $candidate) { $mvnExe = $candidate }
}
if (-not $mvnExe) {
    Write-Host 'ERRO: Maven nao encontrado. Instale o Maven ou defina MAVEN_HOME (pasta do Maven, ex.: ...\apache-maven-3.9.9) e adicione ...\bin ao PATH do Windows.' -ForegroundColor Red
    exit 1
}

$javaHome = $env:JAVA_HOME
if (-not $javaHome -or -not (Test-Path (Join-Path $javaHome 'bin\java.exe'))) {
    $jdk25 = 'C:\Program Files\Java\jdk-25'
    if (Test-Path (Join-Path $jdk25 'bin\java.exe')) {
        $javaHome = $jdk25
        $env:JAVA_HOME = $javaHome
    }
}
if ($javaHome) {
    $env:Path = "$javaHome\bin;$env:Path"
}

$env:Path = "$(Split-Path $mvnExe -Parent);$env:Path"
Set-Location $RepoRoot

Write-Host "Repositorio: $RepoRoot" -ForegroundColor DarkGray
Write-Host "Maven: $mvnExe" -ForegroundColor DarkGray
if ($javaHome) { Write-Host "JAVA_HOME: $javaHome" -ForegroundColor DarkGray }

$chosenPort = $null
if ($Port -gt 0) {
    if (Test-TcpListenPortOccupied $Port) {
        Write-Host ("ERRO: porta {0} ja esta ocupada. Use -Port (outra porta) ou -Port 0 para escolher automaticamente." -f $Port) -ForegroundColor Red
        exit 1
    }
    $chosenPort = $Port
    Write-Host ("Porta fixa solicitada pelo usuario (-Port {0})" -f $chosenPort) -ForegroundColor Cyan
}
else {
    $chosenPort = Select-FirstFreePort -Start $ListenFrom -Finish $ListenTo
    if (-not $chosenPort) {
        Write-Host ("ERRO: nenhuma porta livre entre {0} e {1}. Feche outro servidor ou aumente ListenTo." -f $ListenFrom, $ListenTo) -ForegroundColor Red
        exit 1
    }
    Write-Host ("Porta automatica escolhida (primeira livre no intervalo [{0}-{1}]): {2}" -f $ListenFrom, $ListenTo, $chosenPort) -ForegroundColor Green
}

$swaggerUi = ('http://localhost:{0}/swagger-ui/index.html' -f $chosenPort)
Write-Host "Swagger: $swaggerUi" -ForegroundColor Cyan

# Porta como propriedade de sistema JVM (evita erro do Maven no Windows com
# -Dspring-boot.run.arguments=--server.port=... que partiria '--server.port' em opcao Maven).
$mvnJvmArg = '-Dspring-boot.run.jvmArguments=-Dserver.port=' + $chosenPort.ToString()

$runArgs = @(
    'spring-boot:run',
    '-pl', 'apresentacao-backend',
    $mvnJvmArg
)

if ($args.Count -gt 0) {
    foreach ($extra in $args) {
        if ($extra -match 'spring-boot\.run\.(arguments|jvmArguments)') {
            Write-Host 'AVISO: argumentos Maven que redefinem spring-boot.run.jvmArguments ou .arguments podem entrar em conflito com a porta definida pelo script.' -ForegroundColor Yellow
        }
        $runArgs += $extra
    }
}

$state = @{ banner = $false }
& $mvnExe @runArgs 2>&1 | ForEach-Object {
    $line = if ($null -eq $_) {
        ''
    }
    elseif ($_.GetType().Name -eq 'ErrorRecord') {
        $_.ToString()
    }
    else {
        "$_"
    }
    Write-Host $line
    # Apos linha oficial "Started AcadTrackApplication", repetir Swagger bem visivel.
    if (-not $state.banner -and ($line -match 'Started\s+AcadTrackApplication\b')) {
        Write-Host ''
        Write-Host '  --- Swagger UI (abrir no browser) ---------------------------' -ForegroundColor Green
        Write-Host "  $($swaggerUi)" -ForegroundColor Cyan
        Write-Host ('  --- OpenAPI: http://localhost:{0}/v3/api-docs ---' -f $chosenPort) -ForegroundColor DarkGray
        Write-Host '  ---------------------------------------------------------------' -ForegroundColor Green
        Write-Host ''
        $state.banner = $true
    }
}
exit $LASTEXITCODE
