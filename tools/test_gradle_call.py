from pathlib import Path
import subprocess
root = Path(__file__).resolve().parent.parent
cmd = [str(root / 'gradlew.bat'), ':admin-demo:run', '--args=--capture --screen dashboard --width 420 --height 900 --output build\\visual-captures\\compact\\dashboard-compact-420x900.png --delayMs 1500']
print('CMD:', cmd)
proc = subprocess.run(cmd, cwd=root, capture_output=True, text=True)
print('RETURN:', proc.returncode)
print('STDOUT:')
print(proc.stdout)
print('STDERR:')
print(proc.stderr)
