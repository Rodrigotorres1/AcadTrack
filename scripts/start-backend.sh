#!/bin/bash
# Libera a porta 8080 se estiver ocupada por Java e sobe o backend.
# Uso (na raiz do repo ou de qualquer lugar):
#   bash scripts/start-backend.sh
#
# Com porta alternativa:
#   bash scripts/start-backend.sh 8081

PORT=${1:-8080}
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

# Tenta liberar via PowerShell (Windows/WSL)
if command -v powershell.exe &>/dev/null; then
    PID=$(powershell.exe -Command "
        \$c = Get-NetTCPConnection -LocalPort $PORT -State Listen -EA SilentlyContinue
        if (\$c) { \$c | Select -Expand OwningProcess -First 1 }
    " 2>/dev/null | tr -d '\r\n ')

    if [ -n "$PID" ] && [ "$PID" != "0" ]; then
        echo "[ok] Liberando porta $PORT (PID $PID)..."
        powershell.exe -Command "Stop-Process -Id $PID -Force" 2>/dev/null
        sleep 1
    else
        echo "[ok] Porta $PORT livre."
    fi
fi

echo ""
echo "Subindo backend na porta $PORT..."

if [ "$PORT" = "8080" ]; then
    ./mvnw -pl apresentacao-backend -am spring-boot:run
else
    ./mvnw -pl apresentacao-backend -am spring-boot:run \
        "-Dspring-boot.run.jvmArguments=-Dserver.port=$PORT"
fi
