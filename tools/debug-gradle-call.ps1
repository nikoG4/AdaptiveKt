$gradle = 'C:\Users\ll\Desktop\kt ui\gradlew.bat'
$task = ':admin-demo:run'
$command = '--capture --screen dashboard --width 420 --height 900 --output build\\visual-captures\\compact\\dashboard-compact-420x900.png --delayMs 1500'
Write-Host "Command token: $task"
Write-Host "Arg token: --args=$command"
& $gradle $task ('--args=' + $command)
Write-Host "Return code: $LASTEXITCODE"
