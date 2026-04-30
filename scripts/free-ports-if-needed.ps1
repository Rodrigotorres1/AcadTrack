# Libera apenas se estiver ocupada: portas tipicas do backend AcadTrack.
# So encerra processos java/javaw (Spring Boot). Outros: aviso, nao mata.
#
# Uso (na raiz do repo):
#   powershell -ExecutionPolicy Bypass -File .\scripts\free-ports-if-needed.ps1
#   powershell -ExecutionPolicy Bypass -File .\scripts\free-ports-if-needed.ps1 -Ports 8080,9001,8443
param(
    [int[]] $Ports = @(8080, 9001)
)

$javaLike = @{ 'java' = $true; 'javaw' = $true }

foreach ($port in $Ports) {
    $conns = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue
    if (-not $conns) {
        Write-Host ("[ok] porta {0} livre." -f $port) -ForegroundColor DarkGray
        continue
    }
    $pids = @($conns | ForEach-Object { $_.OwningProcess } | Sort-Object -Unique)

    foreach ($procId in $pids) {
        try {
            $p = Get-Process -Id $procId -ErrorAction Stop
        } catch {
            Write-Host ("[skip] porta {0} PID {1} desconhecido." -f $port, $procId) -ForegroundColor Yellow
            continue
        }
        $name = $p.ProcessName.ToLowerInvariant()
        if (-not $javaLike.ContainsKey($name)) {
            Write-Host ("[aviso] porta {0} ocupada por {1} (PID {2}) - nao encerrei." -f $port, $p.ProcessName, $procId) -ForegroundColor Yellow
            continue
        }
        try {
            Stop-Process -Id $procId -Force -ErrorAction Stop
            Write-Host ("[ok] porta {0} liberada (Java PID {1})." -f $port, $procId) -ForegroundColor Green
        } catch {
            Write-Host ("[erro] nao consegui encerrar PID {0} na porta {1}: {2}" -f $procId, $port, $_.Exception.Message) -ForegroundColor Red
            Write-Host "      Feche o Java manualmente ou use PowerShell como administrador." -ForegroundColor DarkYellow
        }
    }
}
