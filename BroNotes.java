import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;

import DataStructure.TaskHashTable;
import DataStructure.TaskVector;


public class BroNotes {
    private static class Task {
        private String nama;
        private String catatan;
        private LocalDate deadline;

        public Task(String nama, String catatan, LocalDate deadline) {
            this.nama = nama;
            this.catatan = catatan;
            this.deadline = deadline;
        }

        public String getNama() {
            return nama;
        }

        public String getCatatan() {
            return catatan;
        }

        public LocalDate getDeadline() {
            return deadline;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public void setCatatan(String catatan) {
            this.catatan = catatan;
        }

        public void setDeadline(LocalDate deadline) {
            this.deadline = deadline;
        }

        public int getDaysUntilDeadline() {
            LocalDate today = LocalDate.now();
            return (int) ChronoUnit.DAYS.between(today, deadline);
        }
    }
    
    private static class TaskManager {
        private TaskHashTable<String, TaskVector<Task>> taskHashTable;
        private String currentUser;

        public TaskManager() {
            taskHashTable = new TaskHashTable<String, TaskVector<Task>>(97);
            currentUser = null;
        }

        public void registerUser(String username) {
            taskHashTable.put(username, new TaskVector<>());
        }

        public void loginUser(String username) {
            if (taskHashTable.containsKey(username)) {
                currentUser = username;
                System.out.println("Selamat datang, " + currentUser + "!");
            } else {
                System.out.println("Username tidak ditemukan.");
            }
        }

        public void logoutUser() {
            currentUser = null;
            System.out.println("Anda telah keluar.");
        }

        public boolean isUserLoggedIn() {
            return currentUser != null;
        }

        public void addTask(Task task) {
            TaskVector<Task> taskList = taskHashTable.get(currentUser);
            taskList.add(task);
            System.out.println("Task berhasil ditambahkan.");
        }

        public void removeTask(int index) {
            TaskVector<Task> taskList = taskHashTable.get(currentUser);
            if (index >= 0 && index < taskList.size()) {
                Task removedTask = taskList.remove(index);
                System.out.println("Task berhasil dihapus:");
                System.out.println("Task: " + removedTask.getNama());
                System.out.println("Catatan: " + removedTask.getCatatan());
                System.out.println("Deadline: " + removedTask.getDeadline());
            } else {
                System.out.println("Indeks task tidak valid.");
            }
        }

        public void editTask(int index, String newName, String newCatatan, 
                            LocalDate newDeadline) {
            TaskVector<Task> taskList = taskHashTable.get(currentUser);
            if (index >= 0 && index < taskList.size()) {
                Task task = taskList.get(index);
                task.setNama(newName);
                task.setCatatan(newCatatan);
                task.setDeadline(newDeadline);
                System.out.println("Task berhasil diubah.");
            } else {
                System.out.println("Indeks task tidak valid.");
            }
        }

        public void searchTaskByName(String name) {
            TaskVector<Task> taskList = taskHashTable.get(currentUser);
            boolean found = false;
            for (Object temp : taskList) {
                Task task = (Task) temp;
                if (task.getNama().equals(name)) {
                    System.out.println("Task ditemukan:");
                    System.out.println("Task: " + task.getNama());
                    System.out.println("Catatan: " + task.getCatatan());
                    System.out.println("Deadline: " + task.getDeadline());
                    System.out.println("Reminder: " + task.getDaysUntilDeadline() 
                    + " hari menuju deadline");
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Task dengan nama tersebut tidak ditemukan.");
            }
        }

        public void searchTaskByDeadline(LocalDate deadline) {
            TaskVector<Task> taskList = taskHashTable.get(currentUser);
            boolean found = false;
            for (Object temp : taskList) {
                Task task = (Task) temp;
                if (task.getDeadline().equals(deadline)) {
                    System.out.println("Task ditemukan:");
                    System.out.println("Task: " + task.getNama());
                    System.out.println("Catatan: " + task.getCatatan());
                    System.out.println("Deadline: " + task.getDeadline());
                    System.out.println("Reminder: " + task.getDaysUntilDeadline() 
                    + " hari menuju deadline");
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Task dengan deadline tersebut tidak ditemukan.");
            }
        }

        public void sortTasksByDeadline() {
            TaskVector<Task> taskList = taskHashTable.get(currentUser);
            taskList.sort((t1, t2) -> t1.getDeadline().compareTo(t2.getDeadline()));
            System.out.println("Daftar task berhasil diurutkan berdasarkan deadline.");
        }

        public void exportTasksToTxt(String filename) throws IOException {
            TaskVector<Task> taskList = taskHashTable.get(currentUser);
            PrintWriter output = new PrintWriter(filename + ".txt");

            for (Object temp : taskList) {
                Task task = (Task) temp;
                output.println("Task: " + task.getNama());
                output.println("Catatan: " + task.getCatatan());
                output.println("Deadline: " + task.getDeadline());
                output.println();
            }
            output.close();
            System.out.println("Daftar task berhasil diekspor ke " + filename + ".");
        }

        public void displayTasks() {
            TaskVector<Task> taskList = taskHashTable.get(currentUser);
            if (taskList.isEmpty()) {
                System.out.println("Tidak ada task yang tersedia.");
            } else {
                System.out.println("Daftar Task:");
                for (int i = 0; i < taskList.size(); i++) {
                    Task task = taskList.get(i);
                    System.out.println("Indeks: " + i);
                    System.out.println("Task: " + task.getNama());
                    System.out.println("Catatan: " + task.getCatatan());
                    System.out.println("Deadline: " + task.getDeadline());
                    System.out.println("Reminder: " + task.getDaysUntilDeadline() 
                    + " hari menuju deadline");
                    System.out.println();
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        System.out.println("Selamat datang di BroNotes!");
        while (true) {
            System.out.println();
            System.out.println("Silakan pilih menu:");
            System.out.println("1. Register\t | 7. Search Task by Name");
            System.out.println("2. Login\t | 8. Search Task by Deadline");
            System.out.println("3. Logout\t | 9. Sort Tasks by Deadline");
            System.out.println("4. Add Task\t | 10. Export Tasks to txt");
            System.out.println("5. Remove Task\t | 11. Display Tasks");
            System.out.println("6. Edit Task\t | 12. Exit");
            System.out.print("Input Anda: ");
            String command = sc.nextLine();

            switch (command) {
                case "1":
                    System.out.print("Masukkan username: ");
                    String username = sc.nextLine();
                    taskManager.registerUser(username);
                    break;
                case "2":
                    System.out.print("Masukkan username: ");
                    String loginUsername = sc.nextLine();
                    taskManager.loginUser(loginUsername);
                    break;
                case "3":
                    taskManager.logoutUser();
                    break;
                case "4":
                    if (taskManager.isUserLoggedIn()) {
                        System.out.print("Masukkan nama task: ");
                        String namaTask = sc.nextLine();
                        System.out.print("Masukkan catatan: ");
                        String catatan = sc.nextLine();
                        System.out.print("Masukkan deadline (yyyy-mm-dd): ");
                        String deadlineString = sc.nextLine();
                        LocalDate deadline = LocalDate.parse(deadlineString);
                        Task task = new Task(namaTask, catatan, deadline);
                        taskManager.addTask(task);
                    } else {
                        System.out.println("Anda harus login terlebih dahulu.");
                    }
                    break;
                case "5":
                    if (taskManager.isUserLoggedIn()) {
                        System.out.print("Masukkan indeks task yang ingin dihapus: ");
                        int removeIndex = sc.nextInt();
                        sc.nextLine(); // consume the newline character
                        taskManager.removeTask(removeIndex);
                    } else {
                        System.out.println("Anda harus login terlebih dahulu.");
                    }
                    break;
                case "6":
                    if (taskManager.isUserLoggedIn()) {
                        System.out.print("Masukkan indeks task yang ingin diubah: ");
                        int editIndex = sc.nextInt();
                        sc.nextLine(); // consume the newline character
                        System.out.print("Masukkan nama task baru: ");
                        String newName = sc.nextLine();
                        System.out.print("Masukkan catatan baru: ");
                        String newCatatan = sc.nextLine();
                        System.out.print("Masukkan deadline baru (yyyy-mm-dd): ");
                        String newDeadlineString = sc.nextLine();
                        LocalDate newDeadline = LocalDate.parse(newDeadlineString);
                        taskManager.editTask(editIndex, newName, newCatatan, newDeadline);
                    } else {
                        System.out.println("Anda harus login terlebih dahulu.");
                    }
                    break;
                case "7":
                    if (taskManager.isUserLoggedIn()) {
                        System.out.print("Masukkan nama task yang ingin dicari: ");
                        String searchName = sc.nextLine();
                        taskManager.searchTaskByName(searchName);
                    } else {
                        System.out.println("Anda harus login terlebih dahulu.");
                    }
                    break;
                case "8":
                    if (taskManager.isUserLoggedIn()) {
                        System.out.print("Masukkan deadline task yang ingin dicari (yyyy-mm-dd): ");
                        String searchDeadlineString = sc.nextLine();
                        LocalDate searchDeadline = LocalDate.parse(searchDeadlineString);
                        taskManager.searchTaskByDeadline(searchDeadline);
                    } else {
                        System.out.println("Anda harus login terlebih dahulu.");
                    }
                    break;
                case "9":
                    if (taskManager.isUserLoggedIn()) {
                        taskManager.sortTasksByDeadline();
                    } else {
                        System.out.println("Anda harus login terlebih dahulu.");
                    }
                    break;
                case "10":
                    if (taskManager.isUserLoggedIn()) {
                        System.out.print("Masukkan nama file txt untuk menyimpan daftar task: ");
                        String filename = sc.nextLine();
                        try {
                            taskManager.exportTasksToTxt(filename);
                        } catch (IOException e) {
                            System.out.println("Terjadi kesalahan saat menulis ke file.");
                        }
                    } else {
                        System.out.println("Anda harus login terlebih dahulu.");
                    }
                    break;
                case "11":
                    if (taskManager.isUserLoggedIn()) {
                        taskManager.displayTasks();
                    } else {
                        System.out.println("Anda harus login terlebih dahulu.");
                    }
                    break;
                case "12":
                    System.out.println("Terima kasih telah menggunakan BroNotes!");
                    System.exit(0);
                default:
                    System.out.println("Input tidak valid. Silakan masukkan angka 1-12 sesuai pilihan menu.");
                    break;
            }
        }
    }
}
