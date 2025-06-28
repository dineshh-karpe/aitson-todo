package com.aitson.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomHealthIndicator {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getHealthStatus() {
        Map<String, Object> healthInfo = new HashMap<>();
        
        // Database Health Check
        healthInfo.put("database", checkDatabaseHealth());
        
        // System Health Check
        healthInfo.put("system", checkSystemHealth());
        
        // Application Health Check
        healthInfo.put("application", checkApplicationHealth());
        
        // Overall status
        boolean isHealthy = isDatabaseHealthy() && isSystemHealthy();
        healthInfo.put("status", isHealthy ? "UP" : "DOWN");
        healthInfo.put("timestamp", System.currentTimeMillis());
        
        return healthInfo;
    }
    
    private Map<String, Object> checkDatabaseHealth() {
        Map<String, Object> dbInfo = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                String result = jdbcTemplate.queryForObject("SELECT 'OK' as status", String.class);
                String dbProduct = connection.getMetaData().getDatabaseProductName() + " " + 
                                  connection.getMetaData().getDatabaseProductVersion();
                
                dbInfo.put("status", "UP");
                dbInfo.put("database", dbProduct);
                dbInfo.put("connection", "OK");
            } else {
                dbInfo.put("status", "DOWN");
                dbInfo.put("error", "Database connection is not valid");
            }
        } catch (SQLException e) {
            dbInfo.put("status", "DOWN");
            dbInfo.put("error", "Database connection failed: " + e.getMessage());
        }
        
        return dbInfo;
    }
    
    private Map<String, Object> checkSystemHealth() {
        Map<String, Object> systemInfo = new HashMap<>();
        
        // Memory Information
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long usedHeap = memoryBean.getHeapMemoryUsage().getUsed();
        long maxHeap = memoryBean.getHeapMemoryUsage().getMax();
        double heapUsagePercent = (double) usedHeap / maxHeap * 100;
        
        systemInfo.put("heapUsage", String.format("%.2f%%", heapUsagePercent));
        systemInfo.put("usedHeap", formatBytes(usedHeap));
        systemInfo.put("maxHeap", formatBytes(maxHeap));
        
        // System Information
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        systemInfo.put("systemLoad", String.format("%.2f", osBean.getSystemLoadAverage()));
        systemInfo.put("availableProcessors", osBean.getAvailableProcessors());
        
        // Determine system health based on heap usage
        if (heapUsagePercent < 90) {
            systemInfo.put("status", "UP");
        } else {
            systemInfo.put("status", "DOWN");
            systemInfo.put("warning", "High heap usage detected");
        }
        
        return systemInfo;
    }
    
    private Map<String, Object> checkApplicationHealth() {
        Map<String, Object> appInfo = new HashMap<>();
        
        // Application uptime
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        appInfo.put("uptime", formatUptime(uptime));
        appInfo.put("startTime", ManagementFactory.getRuntimeMXBean().getStartTime());
        
        // Thread information
        int threadCount = ManagementFactory.getThreadMXBean().getThreadCount();
        appInfo.put("activeThreads", threadCount);
        appInfo.put("status", "UP");
        
        return appInfo;
    }
    
    private boolean isDatabaseHealthy() {
        Map<String, Object> dbHealth = checkDatabaseHealth();
        return "UP".equals(dbHealth.get("status"));
    }
    
    private boolean isSystemHealthy() {
        Map<String, Object> systemHealth = checkSystemHealth();
        return "UP".equals(systemHealth.get("status"));
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    private String formatUptime(long uptime) {
        long days = uptime / (24 * 60 * 60 * 1000);
        long hours = (uptime % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        long minutes = (uptime % (60 * 60 * 1000)) / (60 * 1000);
        long seconds = (uptime % (60 * 1000)) / 1000;
        
        return String.format("%dd %dh %dm %ds", days, hours, minutes, seconds);
    }
} 