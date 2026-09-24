# ==============================================================================
#  Sobe a stack Docker (postgres + backend + frontend + n8n) numa janela nova.
#
#  Uso (da raiz do repo):   .\subir.cmd        (ou duplo clique no subir.cmd)
#  Parar:                   .\subir.cmd down
#
#  Chamar o .ps1 direto (.\scripts\subir-docker.ps1) falha se a ExecutionPolicy
#  for Restricted (padrão do Windows); o subir.cmd já passa -ExecutionPolicy Bypass.
#
#  Na primeira execução cria infra/.env a partir do .env.example, gerando
#  POSTGRES_PASSWORD, INTERNAL_API_KEY e N8N_ENCRYPTION_KEY automaticamente.
#  Os segredos do sandbox/SearXNG do n8n Assistant são gerados sempre que faltarem.
# ==============================================================================
param([string]$Acao = "up")

$ErrorActionPreference = "Stop"
$infra = (Resolve-Path (Join-Path $PSScriptRoot "..\infra")).Path

if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Host "Docker não encontrado. Instale o Docker Desktop:" -ForegroundColor Red
    Write-Host "  winget install -e --id Docker.DockerDesktop" -ForegroundColor Yellow
    exit 1
}

# No Windows PowerShell 5.1, redirecionar o stderr de um exe com EAP=Stop vira
# erro fatal (NativeCommandError). Por isso o "docker info" roda com EAP local = Continue.
function Test-DockerDaemon {
    $ErrorActionPreference = "Continue"
    docker info *> $null
    return ($LASTEXITCODE -eq 0)
}

if (-not (Test-DockerDaemon)) {
    Write-Host "Docker instalado, mas o daemon não responde. Abrindo o Docker Desktop..." -ForegroundColor Yellow
    $desktop = Join-Path $env:ProgramFiles "Docker\Docker\Docker Desktop.exe"
    if (Test-Path $desktop) { Start-Process $desktop }
    $limite = (Get-Date).AddMinutes(3)
    while (-not (Test-DockerDaemon)) {
        if ((Get-Date) -gt $limite) {
            Write-Host "O Docker não respondeu em 3 minutos. Abra o Docker Desktop manualmente e rode de novo." -ForegroundColor Red
            exit 1
        }
        Start-Sleep -Seconds 3
    }
}

if ($Acao -eq "down") {
    Push-Location $infra; docker compose down; Pop-Location
    exit
}

function Novo-Segredo { [Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Max 256 })) }
function Novo-Hex { -join (1..24 | ForEach-Object { '{0:x2}' -f (Get-Random -Max 256) }) }

$utf8 = New-Object System.Text.UTF8Encoding($false)
$envFile = Join-Path $infra ".env"
if (-not (Test-Path $envFile)) {
    # Senha do postgres sem caracteres que atrapalham URL/shell
    $senhaDb = -join ((48..57) + (65..90) + (97..122) | Get-Random -Count 24 | ForEach-Object { [char]$_ })

    $conteudo = [IO.File]::ReadAllText((Join-Path $infra ".env.example"), $utf8)
    $conteudo = $conteudo -replace '(?m)^POSTGRES_PASSWORD=.*$', "POSTGRES_PASSWORD=$senhaDb"
    $conteudo = $conteudo -replace '(?m)^INTERNAL_API_KEY=.*$', "INTERNAL_API_KEY=$(Novo-Segredo)"
    $conteudo = $conteudo -replace '(?m)^N8N_ENCRYPTION_KEY=.*$', "N8N_ENCRYPTION_KEY=$(Novo-Segredo)"
    [IO.File]::WriteAllText($envFile, $conteudo, $utf8)
    Write-Host "infra/.env criado com segredos gerados." -ForegroundColor Green
}

# Segredos internos do sandbox/SearXNG do n8n Assistant: preenche os que faltam
# ou estão vazios, também num .env antigo (de antes desses serviços existirem).
$conteudo = [IO.File]::ReadAllText($envFile, $utf8)
$gerados = @()
foreach ($chave in "SANDBOX_API_KEY", "SANDBOX_RUNNER_REGISTRATION_TOKEN", "SANDBOX_RUNNER_API_KEY", "SEARXNG_SECRET") {
    if ($conteudo -match "(?m)^$chave=[^\s#]+") { continue }
    if ($conteudo -match "(?m)^$chave=") {
        $conteudo = $conteudo -replace "(?m)^$chave=.*$", "$chave=$(Novo-Hex)"
    } else {
        $conteudo = $conteudo.TrimEnd() + "`n$chave=$(Novo-Hex)`n"
    }
    $gerados += $chave
}
if ($gerados) {
    [IO.File]::WriteAllText($envFile, $conteudo, $utf8)
    Write-Host "infra/.env: gerados $($gerados -join ', ')." -ForegroundColor Green
}

# Janela nova de cmd com os logs ao vivo; fechar a janela NÃO derruba os containers.
Start-Process cmd -WorkingDirectory $infra -ArgumentList "/k", "title FinTech App - docker && docker compose up -d --build && docker compose logs -f"

Write-Host ""
Write-Host "Subindo em outra janela. Quando terminar:" -ForegroundColor Cyan
Write-Host "  App      http://localhost:3000"
Write-Host "  Swagger  http://localhost:8082/swagger-ui.html"
Write-Host "  n8n      http://localhost:5678"
