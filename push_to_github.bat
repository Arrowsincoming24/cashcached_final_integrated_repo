@echo off
git config user.email "arrowsincoming24@github.com"
git config user.name "Arrowsincoming24"
git commit -m "Complete CASHCACHED FD Simulator"
git branch -M main
git remote remove origin
git remote add origin https://github.com/Arrowsincoming24/lab_work_15thoct2025.git
git push -u origin main --force
pause
