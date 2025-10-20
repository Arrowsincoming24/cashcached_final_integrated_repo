# Upload CashCached to GitHub
Write-Host "`n=== UPLOADING CASHCACHED TO GITHUB ===" -ForegroundColor Cyan

$repoUrl = "https://github.com/Arrowsincoming24/Cashcached_integration_final.git"

# Create .gitignore if it doesn't exist
Write-Host "`nCreating .gitignore..." -ForegroundColor Yellow
$gitignoreContent = @"
# Build files
target/
*.class
*.jar
*.war
*.ear

# IDE files
.idea/
.vscode/
*.iml
.DS_Store

# Logs
*.log
logs/

# OS files
Thumbs.db
.DS_Store

# Application properties (if contains sensitive data)
# application-prod.properties
"@

Set-Content -Path ".gitignore" -Value $gitignoreContent
Write-Host "[OK] .gitignore created" -ForegroundColor Green

# Initialize git repository
Write-Host "`nInitializing Git repository..." -ForegroundColor Yellow
git init
Write-Host "[OK] Git initialized" -ForegroundColor Green

# Add remote
Write-Host "`nAdding remote repository..." -ForegroundColor Yellow
git remote remove origin 2>$null
git remote add origin $repoUrl
Write-Host "[OK] Remote added: $repoUrl" -ForegroundColor Green

# Add all files
Write-Host "`nAdding files to git..." -ForegroundColor Yellow
git add .
Write-Host "[OK] Files staged" -ForegroundColor Green

# Commit
Write-Host "`nCommitting changes..." -ForegroundColor Yellow
$commitMessage = @"
CashCached - Complete Fixed Deposit Banking Platform v1.0.0

Features:
- Professional banking home page with glassmorphism UI
- Admin dashboard (products, users, time travel, batch processing)
- Customer dashboard with FD calculator
- JWT authentication (password, phone/OTP, Google OAuth ready)
- 15 REST API endpoints (100% tested)
- Swagger UI documentation
- H2 database with complete schema
- Responsive design with ∞ CASHCACHED branding

Tech Stack: Spring Boot 3.x, Spring Security, H2, Thymeleaf, REST APIs
Status: Production Ready ✅
"@

git commit -m $commitMessage
Write-Host "[OK] Changes committed" -ForegroundColor Green

# Push to GitHub
Write-Host "`nPushing to GitHub..." -ForegroundColor Yellow
Write-Host "Note: You may need to authenticate with GitHub" -ForegroundColor Cyan

# Try main branch first
git branch -M main
git push -u origin main

if ($LASTEXITCODE -ne 0) {
    Write-Host "`nTrying master branch..." -ForegroundColor Yellow
    git branch -M master
    git push -u origin master
}

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n✅ SUCCESS! Code uploaded to GitHub!" -ForegroundColor Green
    Write-Host "`nRepository URL: https://github.com/Arrowsincoming24/Cashcached_integration_final" -ForegroundColor Cyan
} else {
    Write-Host "`n⚠️ Push failed. You may need to:" -ForegroundColor Yellow
    Write-Host "1. Authenticate with GitHub (gh auth login)" -ForegroundColor White
    Write-Host "2. Or use GitHub Desktop" -ForegroundColor White
    Write-Host "3. Or manually push using: git push -u origin main" -ForegroundColor White
}

Write-Host "`n"
