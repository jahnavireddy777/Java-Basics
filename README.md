Concurrent Logging System
A Java-based multithreaded logging system that handles concurrent updates to a shared resource while ensuring ordered and thread-safe log entries. This project demonstrates synchronization, inter-thread communication, and efficient logging using BlockingQueue and a dedicated logger thread.

Features
Thread-safe updates to shared variables

Real-time logging with ordered entries

Asynchronous, non-blocking logging mechanism

Graceful shutdown of threads

Scalable up to 15 worker threads

Technologies Used
Java (Threading, Synchronization, BlockingQueue)

File I/O (BufferedWriter, FileWriter)

Collections (ArrayList, Queue)

Concurrency Utilities (BlockingQueue, Thread, Runnable)

System Workflow
1. User Input
User enters the number of worker threads (between 1 and 15).

Input is validated before proceeding.

2. Logger Initialization
A dedicated logger thread (LogWriter) starts.

It listens to the BlockingQueue for log messages.

3. Worker Threads
Random names are assigned to each thread.

Each thread modifies a shared variable (sharedName) inside a synchronized(lock) block.

Every update is logged by placing a message in the logQueue.

4. Logging System
The LogWriter thread retrieves messages from the queue and writes them to log.txt.

It terminates gracefully after receiving a special "TERMINATE" signal.

Example Output:
[Thread-1] changed sharedName to: Alice
[Thread-3] changed sharedName to: Bob
[Thread-2] changed sharedName to: Charlie
...

How to Run
Prerequisites
Java 8 or higher

IDE like IntelliJ IDEA / VS Code (or command line)

Compile and Run
bash
Copy
Edit
javac ConcurrentLogger.java
java ConcurrentLogger
Follow the prompt and enter the number of worker threads when asked.

File Structure
bash
Copy
Edit
concurrent-logger/
├── ConcurrentLogger.java     # Main class containing the program
├── log.txt                   # Output log file (auto-generated)
└── README.md                 # You're here!

Why Synchronization?
Without synchronized(lock):

Race conditions can occur when multiple threads access and modify sharedName.

Logs might become inconsistent or reflect the wrong order of changes.

Using synchronization:

Guarantees atomic access to shared resources.

Maintains correct order of updates and logs.

How Logging Stays Non-Blocking
Logger thread waits on the BlockingQueue.

Worker threads don’t wait for logs to be written.

Enables concurrent logging with minimal thread contention.

Graceful Shutdown
After all worker threads complete:

"TERMINATE" signal is sent to logger.

Logger exits after finishing remaining logs.

Contributing
Want to add error handling, GUI, or improve performance? Feel free to fork the repo and submit a pull request. Contributions are welcome.

License
This project is licensed under the MIT License.

Acknowledgements
Inspired by real-world logging systems and thread synchronization challenges in concurrent programming.
