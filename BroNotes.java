import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

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
    }

    private static class TaskMap<K, V> {
        private Entry<K, V>[] buckets;
        private int capacity;
        private int size;
    
        public TaskMap(int capacity) {
            this.capacity = capacity;
            this.size = 0;
            this.buckets = new Entry[capacity];
        }
    
        private static class Entry<K, V> {
            K key;
            V value;
            Entry<K, V> next;
    
            public Entry(K key, V value) {
                this.key = key;
                this.value = value;
                this.next = null;
            }
        }
    
        private int getBucketIndex(K key) {
            int hashCode = key.hashCode();
            return Math.abs(hashCode % capacity);
        }
    
        public void put(K key, V value) {
            int bucketIndex = getBucketIndex(key);
            Entry<K, V> entry = new Entry<>(key, value);
    
            if (buckets[bucketIndex] == null) {
                buckets[bucketIndex] = entry;
                size++;
            } else {
                Entry<K, V> current = buckets[bucketIndex];
                while (current.next != null) {
                    if (current.key.equals(key)) {
                        current.value = value;
                        return;
                    }
                    current = current.next;
                }
                if (current.key.equals(key)) {
                    current.value = value;
                } else {
                    current.next = entry;
                    size++;
                }
            }
        }
    
        public V get(K key) {
            int bucketIndex = getBucketIndex(key);
            Entry<K, V> current = buckets[bucketIndex];
    
            while (current != null) {
                if (current.key.equals(key)) {
                    return current.value;
                }
                current = current.next;
            }
    
            return null;
        }

        public boolean containsKey(K key) {
            int bucketIndex = getBucketIndex(key);
            Entry<K, V> current = buckets[bucketIndex];
    
            while (current != null) {
                if (current.key.equals(key)) {
                    return true;
                }
                current = current.next;
            }
    
            return false;
        }
    }

    private static class TaskArrayList<E> implements Iterable {
        private static final int INITIAL_CAPACITY = 10;
        private Object[] elements;
        private int size;
        
        public TaskArrayList() {
            elements = new Object[INITIAL_CAPACITY];
            size = 0;
        }
        
        public void add(E element) {
            if (size == elements.length) {
                resize();
            }
            elements[size++] = element;
        }
        
        @SuppressWarnings("unchecked")
        public E get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            return (E) elements[index];
        }

        public void set(int index, E element) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            elements[index] = element;
        }
        
        public E remove(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            System.arraycopy(elements, index + 1, elements, index, size - index - 1);
            E removed = (E) elements[--size];
            elements[--size] = null;
            return removed;
        }
        
        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }
        
        private void resize() {
            int newCapacity = elements.length * 2;
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }

        @Override
        public Iterator iterator() {
            return new TaskArrayListIterator();
        }

        private class TaskArrayListIterator implements Iterator<E> {
            private int currentIndex = 0;
            
            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }
            
            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                return (E) elements[currentIndex++];
            }
            
            @Override
            public void remove() {
                System.out.println("remove() operation is not supported.");
            }
        }

        public void sort(Comparator<? super E> comparator) {
            quicksort(0, size - 1, comparator);
        }
        
        private void quicksort(int low, int high, Comparator<? super E> comparator) {
            if (low < high) {
                int pivotIndex = partition(low, high, comparator);
                quicksort(low, pivotIndex - 1, comparator);
                quicksort(pivotIndex + 1, high, comparator);
            }
        }
        
        private int partition(int low, int high, Comparator<? super E> comparator) {
            E pivot = get(high);
            int i = low - 1;
            
            for (int j = low; j < high; j++) {
                if (comparator.compare(get(j), pivot) <= 0) {
                    i++;
                    swap(i, j);
                }
            }
            
            swap(i + 1, high);
            return i + 1;
        }

        private void swap(int i, int j) {
            E temp = get(i);
            set(i, get(j));
            set(j, temp);
        }
    }
    
    private static class TaskManager {
        private TaskMap<String, TaskArrayList<Task>> taskMap;
        private String currentUser;

        public TaskManager() {
            taskMap = new TaskMap<String, TaskArrayList<Task>>(97);
            currentUser = null;
        }

        public void registerUser(String username) {
            taskMap.put(username, new TaskArrayList<>());
        }

        public void loginUser(String username) {
            if (taskMap.containsKey(username)) {
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
            TaskArrayList<Task> taskList = taskMap.get(currentUser);
            taskList.add(task);
            System.out.println("Task berhasil ditambahkan.");
        }

        public void removeTask(int index) {
            TaskArrayList<Task> taskList = taskMap.get(currentUser);
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

        public void editTask(int index, String newName, String newCatatan, LocalDate newDeadline) {
            TaskArrayList<Task> taskList = taskMap.get(currentUser);
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
            TaskArrayList<Task> taskList = taskMap.get(currentUser);
            boolean found = false;
            for (Object temp : taskList) {
                Task task = (Task) temp;
                if (task.getNama().equals(name)) {
                    System.out.println("Task ditemukan:");
                    System.out.println("Task: " + task.getNama());
                    System.out.println("Catatan: " + task.getCatatan());
                    System.out.println("Deadline: " + task.getDeadline());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Task dengan nama tersebut tidak ditemukan.");
            }
        }

        public void searchTaskByDeadline(LocalDate deadline) {
            TaskArrayList<Task> taskList = taskMap.get(currentUser);
            boolean found = false;
            for (Object temp : taskList) {
                Task task = (Task) temp;
                if (task.getDeadline().equals(deadline)) {
                    System.out.println("Task ditemukan:");
                    System.out.println("Task: " + task.getNama());
                    System.out.println("Catatan: " + task.getCatatan());
                    System.out.println("Deadline: " + task.getDeadline());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Task dengan deadline tersebut tidak ditemukan.");
            }
        }

        public void sortTasksByDeadline() {
            TaskArrayList<Task> taskList = taskMap.get(currentUser);
            taskList.sort((t1, t2) -> t1.getDeadline().compareTo(t2.getDeadline()));
            System.out.println("Daftar task berhasil diurutkan berdasarkan deadline.");
        }

        public void exportTasksToTxt(String filename) throws IOException {
            TaskArrayList<Task> taskList = taskMap.get(currentUser);
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
            TaskArrayList<Task> taskList = taskMap.get(currentUser);
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
