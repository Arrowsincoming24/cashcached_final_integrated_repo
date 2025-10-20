package com.bank.fdsimulator.service;

import com.bank.fdsimulator.entity.FdStatus;
import com.bank.fdsimulator.entity.FixedDeposit;
import com.bank.fdsimulator.repository.FixedDepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Batch Processing Service for automated FD operations
 * Handles maturity processing, interest calculations, and status updates
 */
@Service
@Transactional
public class BatchProcessingService {
    
    @Autowired
    private FixedDepositRepository fdRepository;
    
    @Autowired
    private TimeTravelService timeTravelService;
    
    @Autowired
    private FixedDepositService fdService;
    
    /**
     * Process all matured FDs and update their status
     */
    public BatchProcessingResult processMaturedDeposits() {
        LocalDate currentDate = timeTravelService.getCurrentDate();
        List<FixedDeposit> activeFds = fdRepository.findByStatus(FdStatus.ACTIVE);
        
        int processedCount = 0;
        int errorCount = 0;
        BigDecimal totalMaturedAmount = BigDecimal.ZERO;
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔄 BATCH PROCESSING: Matured Deposits");
        System.out.println("=".repeat(60));
        System.out.println("Current Date: " + currentDate);
        System.out.println("Active FDs to check: " + activeFds.size());
        
        for (FixedDeposit fd : activeFds) {
            try {
                if (fd.getMaturityDate() != null && !fd.getMaturityDate().toLocalDate().isAfter(currentDate)) {
                    // FD has matured
                    fd.setStatus(FdStatus.MATURED);
                    
                    // Calculate final maturity amount
                    BigDecimal maturityAmount = fdService.calculateMaturityAmount(
                        fd.getPrincipalAmount(),
                        fd.getInterestRate(),
                        fd.getTenureInMonths()
                    );
                    fd.setMaturityAmount(maturityAmount);
                    
                    fdRepository.save(fd);
                    processedCount++;
                    totalMaturedAmount = totalMaturedAmount.add(maturityAmount);
                    
                    System.out.println("✓ Matured FD #" + fd.getId() + " - Amount: " + maturityAmount);
                }
            } catch (Exception e) {
                errorCount++;
                System.err.println("✗ Error processing FD #" + fd.getId() + ": " + e.getMessage());
            }
        }
        
        System.out.println("=".repeat(60));
        System.out.println("Processed: " + processedCount + " | Errors: " + errorCount);
        System.out.println("Total Matured Amount: " + totalMaturedAmount);
        System.out.println("=".repeat(60) + "\n");
        
        return new BatchProcessingResult(processedCount, errorCount, totalMaturedAmount);
    }
    
    /**
     * Process interest accrual for all active FDs
     */
    public BatchProcessingResult processInterestAccrual() {
        LocalDate currentDate = timeTravelService.getCurrentDate();
        List<FixedDeposit> activeFds = fdRepository.findByStatus(FdStatus.ACTIVE);
        
        int processedCount = 0;
        int errorCount = 0;
        BigDecimal totalInterest = BigDecimal.ZERO;
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("💰 BATCH PROCESSING: Interest Accrual");
        System.out.println("=".repeat(60));
        System.out.println("Current Date: " + currentDate);
        
        for (FixedDeposit fd : activeFds) {
            try {
                // Calculate accrued interest up to current date
                BigDecimal interest = fdService.calculateInterest(
                    fd.getPrincipalAmount(),
                    fd.getInterestRate(),
                    fd.getTenureInMonths()
                );
                
                totalInterest = totalInterest.add(interest);
                processedCount++;
                
            } catch (Exception e) {
                errorCount++;
                System.err.println("✗ Error processing interest for FD #" + fd.getId());
            }
        }
        
        System.out.println("=".repeat(60));
        System.out.println("Processed: " + processedCount + " | Total Interest: " + totalInterest);
        System.out.println("=".repeat(60) + "\n");
        
        return new BatchProcessingResult(processedCount, errorCount, totalInterest);
    }
    
    /**
     * Process FDs nearing maturity (within specified days)
     */
    public List<FixedDeposit> findFdsNearingMaturity(int daysThreshold) {
        LocalDate currentDate = timeTravelService.getCurrentDate();
        LocalDate thresholdDate = currentDate.plusDays(daysThreshold);
        
        List<FixedDeposit> activeFds = fdRepository.findByStatus(FdStatus.ACTIVE);
        
        return activeFds.stream()
            .filter(fd -> fd.getMaturityDate() != null)
            .filter(fd -> !fd.getMaturityDate().toLocalDate().isAfter(thresholdDate))
            .filter(fd -> fd.getMaturityDate().toLocalDate().isAfter(currentDate))
            .collect(Collectors.toList());
    }
    
    /**
     * Auto-renew matured FDs (if auto-renewal is enabled)
     */
    public BatchProcessingResult processAutoRenewals() {
        List<FixedDeposit> maturedFds = fdRepository.findByStatus(FdStatus.MATURED);
        
        int renewedCount = 0;
        int errorCount = 0;
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔄 BATCH PROCESSING: Auto Renewals");
        System.out.println("=".repeat(60));
        
        for (FixedDeposit fd : maturedFds) {
            try {
                // Check if auto-renewal is enabled (you can add this field to FD entity)
                // For now, we'll skip actual renewal
                System.out.println("FD #" + fd.getId() + " eligible for renewal");
            } catch (Exception e) {
                errorCount++;
            }
        }
        
        System.out.println("=".repeat(60));
        System.out.println("Renewed: " + renewedCount + " | Errors: " + errorCount);
        System.out.println("=".repeat(60) + "\n");
        
        return new BatchProcessingResult(renewedCount, errorCount, BigDecimal.ZERO);
    }
    
    /**
     * Generate summary report of all FDs
     */
    public FdSummaryReport generateSummaryReport() {
        LocalDate currentDate = timeTravelService.getCurrentDate();
        
        List<FixedDeposit> allFds = fdRepository.findAll();
        long activeCount = allFds.stream().filter(fd -> fd.getStatus() == FdStatus.ACTIVE).count();
        long maturedCount = allFds.stream().filter(fd -> fd.getStatus() == FdStatus.MATURED).count();
        long closedCount = allFds.stream().filter(fd -> fd.getStatus() == FdStatus.CANCELLED).count();
        
        BigDecimal totalPrincipal = allFds.stream()
            .filter(fd -> fd.getStatus() == FdStatus.ACTIVE)
            .map(FixedDeposit::getPrincipalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalMaturityValue = allFds.stream()
            .filter(fd -> fd.getStatus() == FdStatus.ACTIVE)
            .map(fd -> fdService.calculateMaturityAmount(
                fd.getPrincipalAmount(),
                fd.getInterestRate(),
                fd.getTenureInMonths()
            ))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new FdSummaryReport(
            currentDate,
            allFds.size(),
            activeCount,
            maturedCount,
            closedCount,
            totalPrincipal,
            totalMaturityValue
        );
    }
    
    /**
     * Result class for batch processing operations
     */
    public static class BatchProcessingResult {
        private final int processedCount;
        private final int errorCount;
        private final BigDecimal totalAmount;
        
        public BatchProcessingResult(int processedCount, int errorCount, BigDecimal totalAmount) {
            this.processedCount = processedCount;
            this.errorCount = errorCount;
            this.totalAmount = totalAmount;
        }
        
        public int getProcessedCount() { return processedCount; }
        public int getErrorCount() { return errorCount; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public boolean isSuccessful() { return errorCount == 0; }
    }
    
    /**
     * Summary report class
     */
    public static class FdSummaryReport {
        private final LocalDate reportDate;
        private final long totalFds;
        private final long activeFds;
        private final long maturedFds;
        private final long closedFds;
        private final BigDecimal totalPrincipal;
        private final BigDecimal totalMaturityValue;
        
        public FdSummaryReport(LocalDate reportDate, long totalFds, long activeFds, 
                              long maturedFds, long closedFds, BigDecimal totalPrincipal, 
                              BigDecimal totalMaturityValue) {
            this.reportDate = reportDate;
            this.totalFds = totalFds;
            this.activeFds = activeFds;
            this.maturedFds = maturedFds;
            this.closedFds = closedFds;
            this.totalPrincipal = totalPrincipal;
            this.totalMaturityValue = totalMaturityValue;
        }
        
        public LocalDate getReportDate() { return reportDate; }
        public long getTotalFds() { return totalFds; }
        public long getActiveFds() { return activeFds; }
        public long getMaturedFds() { return maturedFds; }
        public long getClosedFds() { return closedFds; }
        public BigDecimal getTotalPrincipal() { return totalPrincipal; }
        public BigDecimal getTotalMaturityValue() { return totalMaturityValue; }
    }
}
