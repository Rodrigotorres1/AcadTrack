#!/usr/bin/env pwsh
<#
  Executa o fluxo de demonstração (turmas → disciplinas → aluno → responsável → vínculos →
  simulado → notas → desempenho → retificação) contra a API em execução.
  Usage:
    .\scripts\demo-fluxo-api.ps1
    .\scripts\demo-fluxo-api.ps1 -BaseUrl 'http://localhost:8081'
    .\scripts\demo-fluxo-api.ps1 -SkipRetificacao
  Ajuste -BaseUrl para a porta exibida ao subir com .\scripts\run-backend.ps1
#>

param(
    [string]$BaseUrl = 'http://localhost:8080',
    [switch]$SkipRetificacao
)

$ErrorActionPreference = 'Stop'
$utf8Json = @{ 'Content-Type' = 'application/json; charset=utf-8' }
$base = $BaseUrl.TrimEnd('/')

$suffix = [DateTimeOffset]::UtcNow.ToUnixTimeMilliseconds()
$nomeTurma = "Turma Demo Auto $suffix"
$nomeMat = "Matematica Demo Auto $suffix"
$nomePor = "Portugues Demo Auto $suffix"
$emailAluno = "auto.aluno.$suffix@local.invalid"
$emailResp = "auto.responsavel.$suffix@local.invalid"

function Invoke-Step {
    param(
        [Parameter(Mandatory)][string]$Label,
        [Parameter(Mandatory)][string]$Uri,
        [ValidateSet('GET', 'POST', 'PUT', 'PATCH', 'DELETE')][string]$HttpMethod = 'GET',
        [string]$Body = $null
    )

    Write-Host ''
    Write-Host ">>> $Label" -ForegroundColor Cyan
    Write-Host "$Uri"

    try {
        if ($null -eq $Body -or [string]::IsNullOrWhiteSpace($Body)) {
            return Invoke-RestMethod -Uri $Uri -Method $HttpMethod -Headers $utf8Json
        }
        return Invoke-RestMethod -Uri $Uri -Method $HttpMethod -Body $Body -Headers $utf8Json
    }
    catch {
        Write-Host ($_ | Out-String) -ForegroundColor Red
        throw
    }
}

$t = Invoke-Step -Label 'POST turmas' `
    -Uri "$base/turmas" -HttpMethod 'POST' `
    -Body (ConvertTo-Json @{ nome = $nomeTurma } -Compress)
$turmaId = [long]$t.id

$d1 = Invoke-Step -Label 'POST disciplinas (1)' `
    -Uri "$base/disciplinas" -HttpMethod 'POST' `
    -Body (ConvertTo-Json @{ nome = $nomeMat } -Compress)
$idMat = [long]$d1.id

$d2 = Invoke-Step -Label 'POST disciplinas (2)' `
    -Uri "$base/disciplinas" -HttpMethod 'POST' `
    -Body (ConvertTo-Json @{ nome = $nomePor } -Compress)
$idPor = [long]$d2.id

$a = Invoke-Step -Label 'POST alunos' `
    -Uri "$base/alunos" -HttpMethod 'POST' `
    -Body (ConvertTo-Json @{ nome = 'Aluno Demo Fluxo Automatico'; email = $emailAluno } -Compress)
$alunoId = [long]$a.id

$r = Invoke-Step -Label 'POST responsaveis' `
    -Uri "$base/responsaveis" -HttpMethod 'POST' `
    -Body (ConvertTo-Json @{ nome = 'Responsável Demo Fluxo'; email = $emailResp } -Compress)
$respId = [long]$r.id

Invoke-Step -Label "PUT alunos/$alunoId/turma" `
    -Uri "$base/alunos/$alunoId/turma" -HttpMethod 'PUT' `
    -Body (ConvertTo-Json @{ turmaId = $turmaId } -Compress) | Out-Null

Invoke-Step -Label "PUT alunos/$alunoId/responsavel" `
    -Uri "$base/alunos/$alunoId/responsavel" -HttpMethod 'PUT' `
    -Body (
        ConvertTo-Json @{
            responsavelId        = $respId
            podeVisualizarNotas       = $true
            podeVisualizarSimulados   = $true
            podeVisualizarDesempenho  = $true
        } -Compress
    ) | Out-Null

$simBody = @{ descricao = "Simulado Demo Auto $suffix"; disciplinasIds = @( $idMat, $idPor ) }
$s = Invoke-Step -Label 'POST simulados' `
    -Uri "$base/simulados" -HttpMethod 'POST' `
    -Body (ConvertTo-Json $simBody -Compress -Depth 5)
$simId = [long]$s.id

$n1 = Invoke-Step -Label 'POST notas (Matematica)' `
    -Uri "$base/notas" -HttpMethod 'POST' `
    -Body (
        ConvertTo-Json @{
            alunoId      = $alunoId
            simuladoId   = $simId
            disciplinaId = $idMat
            valor        = 7.0
        } -Compress -Depth 5
    )
$n2 = Invoke-Step -Label 'POST notas (Portugues)' `
    -Uri "$base/notas" -HttpMethod 'POST' `
    -Body (
        ConvertTo-Json @{
            alunoId      = $alunoId
            simuladoId   = $simId
            disciplinaId = $idPor
            valor        = 8.5
        } -Compress -Depth 5
    )

$notaId1 = [long]$n1.id

Write-Host "`n>>> GET desempenho (responsavel/aluno)`n" -ForegroundColor Cyan
$desempenhoUri = "$base/responsaveis/$respId/alunos/$alunoId/desempenho"
Write-Host "$desempenhoUri"
$desempenho = Invoke-RestMethod -Uri $desempenhoUri -Method 'GET' -Headers $utf8Json
$desempenho | ConvertTo-Json -Depth 10

Write-Host "`n IDs guardados nesta corrida:"
Write-Host "  TURMA_ID=$turmaId  MAT_ID=$idMat  PORT_ID=$idPor"
Write-Host "  ALUNO_ID=$alunoId  RESP_ID=$respId  SIMULADO_ID=$simId  NOTA1_ID=$notaId1 NOTA2_ID=$([long]$n2.id)"

if (-not $SkipRetificacao) {
    Write-Host "`n>>> Retificacao automatizada (primeira nota)" -ForegroundColor Cyan

    $reqRet = @{ notaId = $notaId1; justificativa = 'Teste automatico demo - possivel inconsistencia registrada.' }
    $sol = Invoke-Step -Label 'POST retificacoes' `
        -Uri "$base/retificacoes" -HttpMethod 'POST' `
        -Body (ConvertTo-Json $reqRet -Compress)
    $solId = [long]$sol.id

    Invoke-Step -Label "PATCH retificacoes/$solId/em-analise" `
        -Uri "$base/retificacoes/$solId/em-analise" -HttpMethod 'PATCH' -Body '' | Out-Null

    $aprovar = @{
        novoValorNota        = 8.5
        justificativaDecisao = 'Revisao automatica concede ajuste no demo.'
    }
    $final = Invoke-Step -Label "PATCH retificacoes/$solId/aprovar" `
        -Uri "$base/retificacoes/$solId/aprovar" -HttpMethod 'PATCH' `
        -Body (ConvertTo-Json $aprovar -Compress)

    Write-Host "`n Estado final solicitacao:"
    $final | ConvertTo-Json -Depth 6
}

Write-Host "`n Fluxo completo.`n"
