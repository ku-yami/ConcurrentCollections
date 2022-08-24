import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final int CALLS_PER_SECOND = 60;
    public static final int WORK_TIME_MS = 5_000; // Время работы приложения в миллисекундах
    private static final int NUM_OF_OPERATORS = 100;

    public static void main(String[] args) throws InterruptedException {

        // Очередь звонков. Реализация потокобезопасна и не блокирует потоки.
        Queue<Call> queue = new ConcurrentLinkedQueue<>();

        // Создание потока генерирующего звонки.
        Thread generator = new Thread(new CallGenerator(queue, CALLS_PER_SECOND));
        generator.start();

        // Создание пула операторов.
        ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_OPERATORS);
        for (int i = 0; i < NUM_OF_OPERATORS; i++) {
            executor.execute(new Operator(queue));
        }

        Thread.sleep(WORK_TIME_MS); // Имитация работы приложения

        // Завершаем работу потоков
        generator.interrupt();

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        long callCounter = CallGenerator.getCallCounter();
        System.out.println("Звонков создано: " + callCounter);

        System.out.println("Осталось звонков в очереди: " + queue.size());
    }
}
