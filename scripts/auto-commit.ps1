param(
    [string]$Source = "manual"
)

$REPO   = "C:\Users\gusta\OneDrive\Documentos\FACULDADE\Fintech APP\fintech-app"
$REMOTE = "origin"
$TARGET = "dev"
$BASE   = "main"

Set-Location $REPO

# ── Abort if nothing changed ──────────────────────────────────────────────────
$rawStatus = git status --porcelain 2>&1
if (-not $rawStatus) { exit 0 }

# ── Capture new vs modified BEFORE staging ────────────────────────────────────
$newFiles      = @(git ls-files --others --exclude-standard 2>&1 | Where-Object { $_.Trim() })
$modifiedFiles = @(git diff --name-only 2>&1 | Where-Object { $_.Trim() })

git add .

$staged = git diff --cached --name-only 2>&1
if (-not $staged) { exit 0 }

$stagedList = @($staged -split "`n" | Where-Object { $_.Trim() })

# ── Determine Conventional Commit type per file ───────────────────────────────
function Get-CommitType($filePath, $isNew) {
    $n = $filePath.ToLower()
    if ($n -match "test|spec")                                                          { return "test" }
    if ($n -match "\.(md|txt|rst)$")                                                    { return "docs" }
    if ($n -match "(pom\.xml|package\.json|package-lock|\.gitignore|application\.|" +
                  "\.properties|\.yml|\.yaml|checkstyle|dockerfile|docker-compose|" +
                  "eslint|prettier|vite\.config|settings\.json|\.mvn)")                 { return "chore" }
    if ($isNew) { return "feat" }
    return "fix"
}

$typeMap   = @{}
$fileNames = @()
foreach ($f in $stagedList) {
    $t = $f.Trim()
    if (-not $t) { continue }
    $isNew         = $newFiles -contains $t
    $typeMap[$t]   = Get-CommitType $t $isNew
    $fileNames    += (Split-Path $t -Leaf)
}

$allTypes   = @($typeMap.Values | Sort-Object -Unique)
$typeOrder  = @("feat","fix","test","docs","chore")
$sortedTypes = @($typeOrder | Where-Object { $allTypes -contains $_ })

# ── Build commit message ──────────────────────────────────────────────────────
if ($stagedList.Count -eq 1) {
    $f        = $stagedList[0].Trim()
    $fileName = Split-Path $f -Leaf
    $type     = $typeMap[$f]

    $diffOutput  = git diff --cached --stat -- $f 2>&1
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

# ── Auto PR: dev -> main (cria apenas se nao existir) ────────────────────────
$prJson = gh pr list --base $BASE --head $TARGET --state open --json number 2>&1
if ($prJson -match "^\[\]$" -or -not $prJson -or $prJson -match "no pull requests") {
    gh pr create `
        --base  $BASE `
        --head  $TARGET `
        --title "chore: auto-pr $TARGET -> $BASE" `
        --body  "Pull request automatico gerado pelo sistema de auto-commit. Revise as alteracoes antes de fazer o merge."
    Write-Host "PR criado: $TARGET -> $BASE"
}
