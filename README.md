The Concurrent Logging System is designed to handle multiple threads writing updates simultaneously while ensuring the proper order of log entries. This system prevents data corruption and ensures consistent logging using synchronization and a dedicated logger thread.
How the Code Works
Main Components:
•	Shared Variable (sharedName): A common resource that worker threads modify. Initially set to "Name".
•	Worker Threads: Each thread is assigned a random name and updates sharedName while logging the change.
•	Logger Thread (LogWriter): Continuously listens to a queue and writes logs to a file (log.txt).
•	Synchronization (synchronized(lock)): Ensures only one thread updates sharedName at a time, preventing conflicts.
•	BlockingQueue (logQueue): Stores log messages in the correct order until they are written to the log file.
Step-by-Step Execution
Step 1: User Input for Number of Threads
•	The program prompts the user to enter a number of worker threads (between 1 and 15).
•	If an invalid input is given, it asks again until a valid number is entered.
Step 2: Starting the Logger
•	A separate thread (LogWriter) starts, waiting for messages in logQueue.
•	This ensures logs are written in the correct sequence.
Step 3: Initializing and Running Worker Threads
•	A list of names is shuffled to ensure randomness.
•	Worker threads are created and executed, each updating sharedName.
•	Each worker logs its changes by adding messages to logQueue.
Step 4: Logging in Action
•	The LogWriter thread retrieves log messages from logQueue and writes them to log.txt.
•	It ensures real-time logging while worker threads continue execution.
Step 5: Graceful Shutdown
•	The main thread waits for all worker threads to finish.
•	Once completed, it signals the LogWriter thread to terminate by adding a "TERMINATE" message to logQueue.
•	The logger thread shuts down and saves all logs.
Why Synchronization is Important
Without proper synchronization (synchronized(lock)):
•	Race Conditions: Multiple threads modifying sharedName simultaneously can lead to unpredictable results.
•	Inconsistent Logs: Without synchronization, logs may reflect an incorrect sequence of updates.
Using synchronization ensures
-> Consistent updates to sharedName.
-> Accurate logging of every change in order.
How the Logger Works Without Blocking Other Threads
•	BlockingQueue (logQueue): The logger thread waits until a log entry is available, avoiding CPU-intensive busy-waiting.
•	Efficiency: Worker threads can update sharedName and push logs without waiting for logging to complete.
Conclusion
The Concurrent Logging System efficiently manages multiple threads updating a shared resource while ensuring ordered and reliable logging. This system:
•	Uses synchronization to prevent race conditions.
•	Implements a non-blocking logger thread to handle logging asynchronously.
•	Ensures scalability by allowing multiple threads to execute concurrently without interference.
