import java.util.Queue;
import java.util.concurrent.atomic.LongAdder;

public class CallGenerator implements Runnable{

    private static final LongAdder callCounter = new LongAdder();

    public static long getCallCounter() {
        return callCounter.sum();
    }

    private final Queue<Call> queue;
    private final int sleepTime;

    public CallGenerator(Queue<Call> queue, int callsPerSecond) {
        this.queue = queue;
        this.sleepTime = 1000 / callsPerSecond;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            queue.add(new Call());
            callCounter.increment();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
