import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


    class Task {
    private String name;
    private String description;
    private Date deadline;

    public Task(String name, String description, Date deadline) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDeadline() {
        return deadline;
    }
}


    class TodoList {
    private List<Task> tasks;

    public TodoList() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void printTasks() {
        for (Task task : tasks) {
            System.out.println(task.getName() + " (" + task.getDescription() + ") - Deadline: " + task.getDeadline());
        }
    }

    public List<Task> filterTasksByDeadline(Date deadline) {
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDeadline().equals(deadline)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
}


public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static TodoList todoList = new TodoList();

    public static void main(String[] args) {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n==== Aplikasi Todo List ====");
            System.out.println("1. Tambah Tugas");
            System.out.println("2. Lihat Daftar Tugas");
            System.out.println("3. Filter Tugas Berdasarkan Deadline");
            System.out.println("4. Keluar");

            System.out.print("Pilih menu: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    addTask();
                    break;
                case "2":
                    printTasks();
                    break;
                case "3":
                    filterTasksByDeadline();
                    break;
                case "4":
                    isRunning = false;
                    break;
                default:
                    System.out.println("Menu tidak valid!");
                    break;
            }
        }
        private static void addTask() {
            System.out.println("==== Tambah Tugas ====");
            System.out.print("Judul tugas: ");
            String title = scanner.nextLine();
            System.out.print("Deskripsi tugas: ");
            String description = scanner.nextLine();
            System.out.print("Deadline (format: dd/MM/yyyy): ");
            String deadlineStr = scanner.nextLine();
    
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date deadline = null;
            try {
                deadline = dateFormat.parse(deadlineStr);
            } catch (ParseException e) {
                System.out.println("Format deadline tidak valid!");
                return;
            }

            Task task = new Task(title, description, deadline);
            todoList.addTask(task);
            System.out.println("Tugas berhasil ditambahkan!");
        }
        private static void printTasks() {
            System.out.println("==== Daftar Tugas ====");
            todoList.printTasks();
        }
    
        private static void filterTasksByDeadline() {
            System.out.println("==== Filter Tugas Berdasarkan Deadline ====");
            System.out.print("Deadline (format: dd/MM/yyyy): ");
            String deadlineStr = scanner.nextLine();
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date deadline = null;
            try {
                deadline = dateFormat.parse(deadlineStr);
            } catch (ParseException e) {
                System.out.println("Format deadline tidak valid!");
                return;
            }

            List<Task> filteredTasks = todoList.filterByDeadline(deadline);
            if (filteredTasks.isEmpty()) {
                System.out.println("Tidak ada tugas dengan deadline tersebut.");
            } else {
                System.out.println("Daftar tugas dengan deadline " + deadlineStr + "====");
                for (Task task : filteredTasks) {
                    System.out.println(task.getName() + " (" + task.getDescription() + ")");
                }
            }
        }
    }
}