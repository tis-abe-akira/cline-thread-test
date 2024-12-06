# Function to make HTTP request
function Invoke-ThreadPoolTest {
    param (
        [int]$numberOfCalls
    )
    
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8080/api/parallel-calls?numberOfCalls=$numberOfCalls" -Method Get
        Write-Host "`nResults for $numberOfCalls parallel calls:"
        Write-Host "Thread Pool Stats: $($response.threadPoolStats)"
        Write-Host "Individual Results:"
        $response.results | ForEach-Object { Write-Host "  $_" }
    }
    catch {
        Write-Host "Error making request: $_"
    }
}

Write-Host "Testing thread pool behavior..."
Write-Host "Make sure Spring Boot application is running first!"
Write-Host "Press Enter to start testing..."
$null = Read-Host

# Test with different numbers of parallel calls
Write-Host "`nTesting with 5 parallel calls..."
Invoke-ThreadPoolTest -numberOfCalls 5

Write-Host "`nTesting with 10 parallel calls..."
Invoke-ThreadPoolTest -numberOfCalls 10

Write-Host "`nTesting with 20 parallel calls..."
Invoke-ThreadPoolTest -numberOfCalls 20

# Make multiple concurrent requests to simulate multiple users
Write-Host "`nSimulating multiple concurrent users..."
$jobs = 1..3 | ForEach-Object {
    Start-Job -ScriptBlock {
        try {
            $response = Invoke-RestMethod -Uri "http://localhost:8080/api/parallel-calls?numberOfCalls=10" -Method Get
            "Concurrent request completed. Thread Pool Stats: $($response.threadPoolStats)"
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
