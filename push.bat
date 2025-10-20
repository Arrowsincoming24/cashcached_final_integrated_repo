@echo off
echo Configuring Git...
git config user.email "arrowsincoming24@github.com"
git config user.name "Arrowsincoming24"

echo Adding files...
git add .

echo Committing changes...
git commit -m "Add multi-currency support with exchange rates display"

echo Setting up remote...
git branch -M main
git remote remove origin 2>nul
git remote add origin https://github.com/Arrowsincoming24/lab_work_15thoct2025.git

echo Pushing to GitHub...
git push -u origin main --force

echo.
echo ========================================
echo Push to GitHub completed successfully!
echo Repository: Arrowsincoming24/lab_work_15thoct2025
echo ========================================
pause
