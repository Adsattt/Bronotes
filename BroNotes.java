import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.lang.Cloneable;

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
    }

    private static class HeapNode {
        private Task task;
        private long priority;

        public HeapNode(Task task, long priority) {
            this.task = task;
            this.priority = priority;
        }

        public Task getTask() {
            return task;
        }

        public long getPriority() {
            return priority;
        }
    }

    private static class Heap implements Cloneable{
        private ArrayList<HeapNode> heapList;

        public Heap() {
            heapList = new ArrayList<HeapNode>();
        }

        
        // Override the clone() method
        @Override
        public Object clone() throws CloneNotSupportedException {
            Heap clone = (Heap) super.clone();
            clone.heapList = (ArrayList<HeapNode>) this.heapList.clone();
            return clone;
        }

        public void insert(Task task) {
            HeapNode newNode = new HeapNode(task, -task.getDeadline().toEpochDay());
            heapList.add(newNode);
            trickleUp(heapList.size() - 1);
        }

        public Task remove() {
            if (heapList.isEmpty()) {
                return null;
            }
            HeapNode root = heapList.get(0);
            heapList.set(0, heapList.get(heapList.size() - 1));
            heapList.remove(heapList.size() - 1);
            if (!heapList.isEmpty()) {
                trickleDown(0);
            }
            return root.getTask();
        }

        private void trickleUp(int index) {
            int parent = (index - 1) / 2;
            HeapNode bottom = heapList.get(index);

            while (index > 0 && heapList.get(parent).getPriority() < bottom.getPriority()) {
                heapList.set(index, heapList.get(parent));
                index = parent;
                parent = (parent - 1) / 2;
            }

            heapList.set(index, bottom);
        }

        private void trickleDown(int index) {
            int largerChild;
            HeapNode top = heapList.get(index);

            while (index < heapList.size() / 2) {
                int leftChild = 2 * index + 1;
                int rightChild = leftChild + 1;

                if (rightChild < heapList.size() && heapList.get(leftChild).getPriority() < heapList.get(rightChild).getPriority()) {
                    largerChild = rightChild;
                } else {
                    largerChild = leftChild;
                }

                if (top.getPriority() >= heapList.get(largerChild).getPriority()) {
                    break;
                }

                heapList.set(index, heapList.get(largerChild));
                index = largerChild;
            }

            heapList.set(index, top);
        }

        public boolean isEmpty() {
            return heapList.isEmpty();
        }

        
        public void print(){
            while(!isEmpty()){
                Task task = remove();
                System.out.println("Task: " + task.getNama());
                System.out.println("Catatan: " + task.getCatatan());
                System.out.println("Deadline: " + task.getDeadline());
                System.out.println();
            }
        }
    }

    private static class Fungsi implements Cloneable{
        private Heap h;

        public Fungsi(Heap h){
            this.h = h;
        }

        // Override the clone() method
        @Override
        public Object clone() throws CloneNotSupportedException {
            Fungsi clone = (Fungsi) super.clone();
            clone.h = (Heap) this.h.clone();
            return clone;
        }

        public void start(){
            System.out.print("\033[H\033[2J");  
            System.out.flush();
            System.out.println("Selamat datang di To-Do List Masbro!");
        }
    
        public void menu(){
            System.out.println();
            System.out.println("Silahkan pilih menu yang diinginkan.");
            System.out.println("1. Tambah Task di Command Prompt.");
            System.out.println("2. Print To-Do List di Command Prompt.");
            System.out.println("3. Delete Task dengan tanggal terdekat.");
            System.out.println("4. Print To-Do List ke File txt.");
            System.out.println("5. Selesai");
            System.out.print("Input Anda: ");
        }
    
        public void misinput(){
            System.out.println();
            System.out.println("Input salah. Mohon masukkan kembali.");
        }
    
        public void exit(){
            System.out.println();
            System.out.println("Terima kasih telah menggunakan To-Do List Masbro!");
            System.out.println();
            System.exit(0);
        }
    
        public void exitconfirm(){
            System.out.println();
            System.out.printf("Yakin ingin keluar?\n1.Yes\n2.No\nInput Anda: ");
        }

        public void input1(Task input){
            h.insert(input);
            System.out.println();
            System.out.println("Task anda sudah masuk ke To-Do List HeapTree!");
        }
            
        public void input2(){
            System.out.println();
            System.out.println("Berikut adalah To-Do List anda.");
            System.out.println();
        }
    
        public void input3(){
            Task task = h.remove();
            System.out.println("Task yang dihapus:");
            if(task != null){
                System.out.println();
                System.out.println("Task: " + task.getNama());
                System.out.println("Catatan: " + task.getCatatan());
                System.out.println("Deadline: " + task.getDeadline());
            }
        }
    
        public void input4(){
            System.out.println();
            System.out.println("To-Do List anda telah diexport ke todolist.txt");
        }
    
        public Heap getHeap(){
            return h;
        }
    }

    public static void main(String args[]) throws IOException, CloneNotSupportedException{
        Scanner sc = new Scanner(System.in);
        Heap heap = new Heap();
        Fungsi f = new Fungsi(heap);
        
        f.start();
        while(true){
            f.menu();
            String command = sc.nextLine();
            switch(command){
                case "1": 
                    System.out.print("Masukkan nama task: ");
                    String namaTask = sc.nextLine();
                    System.out.print("Masukkan catatan: ");
                    String catatan = sc.nextLine();
                    System.out.print("Masukkan deadline (yyyy-mm-dd): ");
                    String deadlineString = sc.nextLine();
                    LocalDate deadline = LocalDate.parse(deadlineString);
                    Task inputTask = new Task(namaTask, catatan, deadline);
                    f.input1(inputTask);
                    break;
                case "2":
                    f.input2();
                    Fungsi tmp = (Fungsi) f.clone();
                    Heap temp = tmp.getHeap();
                    temp.print();
                    break;
                case "3":
                    f.input3();
                    break;
                case "4":
                    Fungsi txtFungsi = (Fungsi) f.clone();
                    Heap txtHeap = txtFungsi.getHeap();
                    
                    try {
                        FileWriter output = new FileWriter("todolist.txt");

                        while(!txtHeap.isEmpty()){
                            Task task = txtHeap.remove();
                            if(task != null){
                                output.write("Task: " + task.getNama() + "\n");
                                output.write("Catatan: " + task.getCatatan() + "\n");
                                output.write("Deadline: " + task.getDeadline() + "\n\n");
                            }
                        }
                        output.close();
                        f.input4();
                    }
                    catch (Exception e) {
                        e.getStackTrace();
                    }

                    break;
                case "5": 
                    f.exitconfirm();
                    String exit = sc.nextLine();
                    switch(exit){
                        case "1":
                            f.exit();
                            break;
                        case "2":
                            break;
                        default: 
                            f.misinput();
                            break;
                    }
                    break;
                default: f.misinput(); break;
            }
        }
    }
}
