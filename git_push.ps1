git config user.email "arrowsincoming24@github.com"
git config user.name "Arrowsincoming24"
git add .
git commit -m "Add multi-currency support for FD products"
git branch -M main
git remote remove origin 2>$null
git remote add origin https://github.com/Arrowsincoming24/lab_work_15thoct2025.git
git push -u origin main --force
Write-Host "Push complete!"
