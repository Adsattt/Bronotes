import java.util.Scanner;
import java.util.Stack;

class PomodoroTimer {
    private int workTime; // waktu kerja (dalam menit)
    private int breakTime; // waktu istirahat (dalam menit)
    private Stack<Integer> timerStack; // stack untuk menyimpan waktu saat ini
    private boolean isPaused; // status timer (jeda atau tidak)
    private boolean isStopped; // status timer (berhenti atau tidak)

    public PomodoroTimer(int workTime, int breakTime) {
        this.workTime = workTime;
        this.breakTime = breakTime;
        this.timerStack = new Stack<>();
        this.timerStack.push(workTime); // masukkan waktu kerja pertama ke dalam stack
        this.isPaused = false;
        this.isStopped = false;
    }

    public void startTimer() throws InterruptedException {
        while (!this.timerStack.isEmpty() && !isStopped) {
            int currentTime = this.timerStack.pop(); // ambil waktu saat ini dari stack
            System.out.println(currentTime + " minutes left...");

            for (int i = currentTime; i >= 1; i--) {
                if (isPaused) {
                    synchronized (this) {
                        while (isPaused) {
                            wait();
                        }
                    }
                }
                Thread.sleep(1000 * 60); // jeda selama 1 menit
                if (i == 1) {
                    System.out.println("Time's up!");
                    if (currentTime == this.workTime) {
                        System.out.println("Take a break!");
                        this.timerStack.push(this.breakTime); // masukkan waktu istirahat ke dalam stack
                    } else {
                        System.out.println("Back to work!");
                        this.timerStack.push(this.workTime); // masukkan waktu kerja ke dalam stack
                    }
                } else {
                    System.out.println(i - 1 + " minutes left...");
                }
            }
        }
    }

    public synchronized void pauseTimer() {
        this.isPaused = true;
        System.out.println("Timer paused.");
    }

    public synchronized void resumeTimer() {
        this.isPaused = false;
        System.out.println("Timer resumed.");
        notify();
    }

    public void stopTimer() {
        isStopped = true;
        System.out.println("Timer stopped.");
    }
}

public class PomodoroTimerApp {
    public static void main(String[] args) throws InterruptedException {
        PomodoroTimer timer = new PomodoroTimer(25, 5);
        Scanner sc = new Scanner(System.in);

        Thread timerThread = new Thread(() -> {
            try {
                timer.startTimer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        timerThread.start();
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("1. Pause timer");
            System.out.println("2. Resume timer");
            System.out.println("3. Stop timer");
            System.out.println("4. Exit");
            System.out.print("Pilih opsi: ");

            int opsi = sc.nextInt();
            switch (opsi) {
                case 1:
                    timer.pauseTimer();
                    break;
                case 2:
                    timer.resumeTimer();
                    break;
                case 3:
                    timer.stopTimer();
                    timerThread.join();
                    isRunning = false;
                    break;
                case 4:
                    timerThread.join();
                    isRunning = false;
                    break;
                default:
                    System.out.println("Opsi tidak valid!");
                    break;
            }
        }
        sc.close();
    }
}