# Cleanup old and irrelevant files
Write-Host "Cleaning up old files..." -ForegroundColor Cyan

$filesToRemove = @(
    "ACCESS_GUIDE.md",
    "ALL_FIXES_COMPLETE.md",
    "ALL_ISSUES_FIXED.md",
    "BRANDING_UPDATE_COMPLETE.md",
    "COLOR_SCHEME_FINAL.md",
    "COMPLETE_SOLUTION.md",
    "COMPLETE_VERIFICATION.ps1",
    "DASHBOARD_WORKFLOW.md",
    "FINAL_COMPLETE_REPORT.md",
    "FINAL_COMPLETE_STATUS.md",
    "FINAL_FIX_SUMMARY.md",
    "FINAL_IMPLEMENTATION_SUMMARY.md",
    "FINAL_STATUS.md",
    "FINAL_TEST_SUMMARY.md",
    "FINAL_UPDATES.md",
    "FIXED_ISSUES_SUMMARY.md",
    "INTEGRATION_SUMMARY.md",
    "LATEST_CHANGES.md",
    "MINIMALIST_DESIGN_UPDATE.md",
    "NEW_DASHBOARDS_GUIDE.md",
    "PREMIUM_UI_IMPLEMENTATION.md",
    "SEXY_DASHBOARDS_COMPLETE.md",
    "THEME_CONSISTENCY_COMPLETE.md",
    "UI_CONSISTENCY_PLAN.md",
    "comprehensive-test.ps1",
    "debug-endpoints.ps1",
    "FINAL-TEST.ps1",
    "git_push.ps1",
    "push.bat",
    "push_to_github.bat",
    "quick-check.ps1",
    "test-bean.ps1",
    "test-customer.ps1",
    "test-direct.ps1",
    "test-endpoints.ps1",
    "test-new-endpoint.ps1",
    "test-products-summary.ps1",
    "test-products.ps1",
    "test-simple.ps1",
    "upload-to-github.ps1",
    "verify-ui-apis.ps1",
    "TEST_RESULTS.md",
    "TEST_SCRIPT.md"
)

$removed = 0
$notFound = 0

foreach ($file in $filesToRemove) {
    if (Test-Path $file) {
        Remove-Item $file -Force
        Write-Host "[REMOVED] $file" -ForegroundColor Yellow
        $removed++
    } else {
        $notFound++
    }
}

Write-Host "`nCleanup complete!" -ForegroundColor Green
Write-Host "Files removed: $removed" -ForegroundColor Cyan
Write-Host "Files not found: $notFound" -ForegroundColor Gray
