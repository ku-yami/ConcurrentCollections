import java.util.Queue;

public class Operator implements Runnable{
    private final Queue<Call> calls;
    private final int ANSWER_TIME_MS = 3_500; // Answer time 3.5 seconds

    public Operator(Queue<Call> calls) {
        this.calls = calls;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Call call = calls.poll();
                if (call != null) {
                    Thread.sleep(ANSWER_TIME_MS);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
