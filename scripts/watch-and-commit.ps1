$REPO    = Split-Path -Parent $PSScriptRoot
$SCRIPT  = "$REPO\scripts\auto-commit.ps1"
$DEBOUNCE = 30  # segundos de silencio antes de commitar apos ultima mudanca

$IGNORE = @(".git","target","node_modules",".next","build","dist",".gradle","out")

Write-Host "Monitorando $REPO..."
Write-Host "Debounce: ${DEBOUNCE}s | Ctrl+C para parar"

$watcher = New-Object System.IO.FileSystemWatcher
$watcher.Path                  = $REPO
$watcher.IncludeSubdirectories = $true
$watcher.EnableRaisingEvents   = $true
$watcher.NotifyFilter = (
    [System.IO.NotifyFilters]::LastWrite -bor
    [System.IO.NotifyFilters]::FileName  -bor
    [System.IO.NotifyFilters]::DirectoryName
)

$global:lastChange = $null

$handler = {
    $path = $Event.SourceEventArgs.FullPath
    foreach ($ig in $IGNORE) {
        if ($path -match [regex]::Escape($ig)) { return }
    }
    $global:lastChange = Get-Date
}

$subs = @(
    Register-ObjectEvent $watcher "Changed" -Action $handler
    Register-ObjectEvent $watcher "Created" -Action $handler
    Register-ObjectEvent $watcher "Deleted" -Action $handler
    Register-ObjectEvent $watcher "Renamed" -Action $handler
)

try {
    while ($true) {
        Start-Sleep -Seconds 5

        if ($global:lastChange -and ((Get-Date) - $global:lastChange).TotalSeconds -ge $DEBOUNCE) {
            $global:lastChange = $null
            Write-Host "[watcher] Mudancas detectadas, executando auto-commit..."
            & (Join-Path $PSHOME "powershell.exe") -ExecutionPolicy Bypass -NonInteractive -File $SCRIPT -Source "watcher"
        }
    }
} finally {
    $subs | ForEach-Object { Unregister-Event -SubscriptionId $_.Id }
    $watcher.Dispose()
    Write-Host "Watcher encerrado."
}
