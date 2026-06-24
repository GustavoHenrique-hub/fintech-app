param(
    [string]$Source = "manual"
)

$REPO   = Split-Path -Parent $PSScriptRoot
$REMOTE = "origin"
$TARGET = "dev"
$BASE   = "main"

Set-Location $REPO

# ── Abort if nothing changed ──────────────────────────────────────────────────
if (-not (git status --porcelain 2>&1)) { exit 0 }

# ── Capture untracked files BEFORE staging ────────────────────────────────────
$newFiles = @(git ls-files --others --exclude-standard 2>&1 | Where-Object { $_.Trim() })

git add .

$staged = git diff --cached --name-only 2>&1
if (-not $staged) { exit 0 }

$stagedList = @($staged -split "`n" | Where-Object { $_.Trim() })

# ── Per-file insertion count (to distinguish feat vs fix) ─────────────────────
# git diff --cached --numstat outputs: <insertions> <deletions> <file>
$numstat = git diff --cached --numstat 2>&1
$insertionMap = @{}
foreach ($line in ($numstat -split "`n")) {
    if ($line -match '^(\d+)\s+(\d+)\s+(.+)$') {
        $insertionMap[$Matches[3].Trim()] = [int]$Matches[1]
    }
}

# ── Determine Conventional Commit type per file ───────────────────────────────
function Get-CommitType($filePath, $isNew, $insertions) {
    $n = $filePath.ToLower()
    if ($n -match "test|spec")                                                         { return "test"  }
    if ($n -match "\.(md|txt|rst)$")                                                   { return "docs"  }
    if ($n -match "(pom\.xml|package\.json|package-lock|\.gitignore|application\.|" +
                  "\.properties|\.yml|\.yaml|checkstyle|dockerfile|docker-compose|" +
                  "eslint|prettier|vite\.config|settings\.json|\.mvn)")                { return "chore" }
    if ($isNew)             { return "feat" }   # arquivo novo
    if ($insertions -gt 0) { return "feat" }    # linhas novas em arquivo existente
    return "fix"                                # apenas modificações/remoções
}

$typeMap   = @{}
$fileNames = @()
foreach ($f in $stagedList) {
    $t          = $f.Trim()
    if (-not $t) { continue }
    $isNew      = $newFiles -contains $t
    $ins        = if ($insertionMap.ContainsKey($t)) { $insertionMap[$t] } else { 0 }
    $typeMap[$t] = Get-CommitType $t $isNew $ins
    $fileNames  += (Split-Path $t -Leaf)
}

$allTypes    = @($typeMap.Values | Sort-Object -Unique)
$typeOrder   = @("feat","fix","test","docs","chore")
$sortedTypes = @($typeOrder | Where-Object { $allTypes -contains $_ })

# ── Build commit message ──────────────────────────────────────────────────────
if ($stagedList.Count -eq 1) {
    $f        = $stagedList[0].Trim()
    $fileName = Split-Path $f -Leaf
    $type     = $typeMap[$f]

    $diffOutput = git diff --cached --stat -- $f 2>&1
    $ins = ""; $del = ""
    foreach ($line in $diffOutput) {
        if ($line -match "(\d+) insertion") { $ins = "+$($Matches[1])" }
        if ($line -match "(\d+) deletion")  { $del = "-$($Matches[1])" }
    }
    $changes   = @($ins, $del) | Where-Object { $_ }
    $changeStr = if ($changes) { $changes -join " " } else { "modified" }

    $commitMsg = "${type}: $fileName $changeStr"
} else {
    $typeStr     = $sortedTypes -join "/"
    $uniqueNames = @($fileNames | Select-Object -Unique)
    $nameList    = ($uniqueNames | Select-Object -First 6) -join ", "
    if ($uniqueNames.Count -gt 6) { $nameList += ", ..." }
    $commitMsg = "${typeStr}: $nameList"
}

# ── Commit ────────────────────────────────────────────────────────────────────
git commit -m $commitMsg
if ($LASTEXITCODE -ne 0) { exit 1 }

Write-Host "[$Source] Commit: $commitMsg"

# ── Push to origin/dev ────────────────────────────────────────────────────────
git push $REMOTE "HEAD:$TARGET"
if ($LASTEXITCODE -ne 0) {
    Write-Host "Push falhou. Tentando apos fetch..."
    git fetch $REMOTE $TARGET
    git push $REMOTE "HEAD:$TARGET"
}

# ── PR: fecha o anterior e cria um novo com base no commit atual ──────────────
$openPRs = gh pr list --base $BASE --head $TARGET --state open --json number --jq '.[].number' 2>&1
foreach ($prNum in ($openPRs -split "`n" | Where-Object { $_ -match '^\d+$' })) {
    gh pr close $prNum --comment "Fechado automaticamente: novo PR gerado com as atualizacoes mais recentes." 2>&1 | Out-Null
    Write-Host "PR #$prNum fechado."
}

gh pr create `
    --base  $BASE `
    --head  $TARGET `
    --title $commitMsg `
    --body  "Pull request gerado automaticamente pelo sistema de auto-commit.``n``nUltimo commit: **$commitMsg**``n``nRevise as alteracoes antes de fazer o merge."

Write-Host "Novo PR criado: $TARGET -> $BASE"
