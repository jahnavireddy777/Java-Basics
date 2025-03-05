// Importing Libraries needed 
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class ConcurrentLoggingSystem {
    private static final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private static volatile String sharedName = "Name";
    private static final Object lock = new Object();
    
    private static final String[] names = { 
            "Jaanu", "Sri Haran", "Gurusaran", "Chandan", "Sreya",
            "Asrita", "Jahnavi", "Ritesh", "Geetha", "Albert",
            "Sweety", "Sreeja", "Samitha", "Sanjay", "Sai"
    };
    
    private static int NUM_THREADS;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.S");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter Number of Threads (1-15): ");
            if (scanner.hasNextInt()) {
                NUM_THREADS = scanner.nextInt();
                if (NUM_THREADS >= 1 && NUM_THREADS <= 15) {
                    break;
                } else {
                    System.out.println("Please Enter a Number Between 1 and 15.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        scanner.close();

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        
        Thread loggerThread = new Thread(new LogWriter(), "LoggerThread");
        loggerThread.start();

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(new Worker(names[indices.get(i)]));
        }

        executor.shutdown();
        
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        logQueue.offer("TERMINATE");
        try {
            loggerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All worker threads completed execution. Logs are saved in log.txt.");
    }

    static class Worker implements Runnable {
        private final String newName;

        public Worker(String newName) {
            this.newName = newName;
        }

        @Override
        public void run() {
            synchronized (lock) {
                String previousName = sharedName;
                String timeStamp = LocalDateTime.now().format(formatter);
                String logMessage = String.format("%s - Thread %s: %s has updated value from '%s' to '%s'.",
                        timeStamp, Thread.currentThread().getName(), newName, previousName, newName);
                logQueue.offer(logMessage);
                sharedName = newName;
            }
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3001));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class LogWriter implements Runnable {
        @Override
        public void run() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                while (true) {
                    String log = logQueue.take();
                    if ("TERMINATE".equals(log)) {
                        break;
                    }
                    writer.write(log);
                    writer.newLine();
                    writer.flush();
                }
            } catch (IOException | InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Logger Thread Terminated. Logs saved in log.txt.");
        }
    }
}
