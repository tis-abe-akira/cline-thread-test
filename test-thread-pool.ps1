# Set encoding to UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

# HTTP request function
function Invoke-ThreadPoolTest {
    param (
        [int]$numberOfCalls
    )
    
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8080/api/parallel-calls?numberOfCalls=$numberOfCalls" -Method Get
        Write-Host "`nResults for $numberOfCalls parallel calls:"
        Write-Host "Thread Pool Status: $($response.threadPoolStats)"
        Write-Host "Individual Results:"
        $response.results | ForEach-Object { Write-Host "  $_" }
    }
    catch {
        Write-Host "Request Error: $_"
    }
}

Write-Host "Starting Thread Pool Test..."
Write-Host "Please make sure Spring Boot application is running!"
Write-Host "Press Enter to start testing..."
$null = Read-Host

# Test with different parallel calls
Write-Host "`nTesting with 5 parallel calls..."
Invoke-ThreadPoolTest -numberOfCalls 5

Write-Host "`nTesting with 10 parallel calls..."
Invoke-ThreadPoolTest -numberOfCalls 10

Write-Host "`nTesting with 20 parallel calls..."
Invoke-ThreadPoolTest -numberOfCalls 20

# Simulate multiple concurrent clients
Write-Host "`nSimulating multiple concurrent clients..."
$jobs = 1..3 | ForEach-Object {
    Start-Job -ScriptBlock {
        try {
            $response = Invoke-RestMethod -Uri "http://localhost:8080/api/parallel-calls?numberOfCalls=10" -Method Get
            "Concurrent request completed. Thread Pool Status: $($response.threadPoolStats)"
        }
        catch {
            "Error in concurrent request: $_"
        }
    }
}

# Wait for all jobs to complete
$jobs | Wait-Job | Receive-Job

Write-Host "`nTest completed. Press Enter to exit..."
$null = Read-Host
