import java.util.Scanner;

class Catatan {
    private int tanggal;
    private String catatan;

    Catatan(int yymmdd, String catatan)
    {
        this.tanggal = yymmdd;
        this.catatan = catatan;
    }

    public int getTanggal()
    {
        return tanggal;
    }

    public String getCatatan()
    {
        return catatan;
    }
}

class MinHeap {
    private Catatan[] Heap;
    private int size;
    private int maxsize;

    public MinHeap(int maxsize)
    {
        this.maxsize = maxsize;
        this.size = 0;
        Heap = new Catatan[this.maxsize];
    }

    private int parent(int pos) { return (pos - 1) / 2; }
 
    private int leftChild(int pos) { return (2 * pos) + 1; }
 
    private int rightChild(int pos) { return (2 * pos) + 2; }
 
    private boolean isLeaf(int pos)
    {
        if (pos > (size / 2) && pos <= size) {
            return true;
        }
        return false;
    }

    private void swap(int fpos, int spos)
    {
        Catatan tmp;
        tmp = Heap[fpos];
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }

    private void MinHeapify(int pos)
    {
        if (isLeaf(pos))
            return;
 
        if (Heap[pos].getTanggal() < Heap[leftChild(pos)].getTanggal()
            || Heap[pos].getTanggal() < Heap[rightChild(pos)].getTanggal()) {
 
            if (Heap[leftChild(pos)].getTanggal()
                > Heap[rightChild(pos)].getTanggal()) {
                swap(pos, leftChild(pos));
                MinHeapify(leftChild(pos));
            }
            else {
                swap(pos, rightChild(pos));
                MinHeapify(rightChild(pos));
            }
        }
    }

    public Catatan extractMax()
    {
        Catatan popped = Heap[0];
        Heap[0] = Heap[--size];
        MinHeapify(0);
        return popped;
    }

    public void insert(Catatan element)
    {
        Heap[size] = element;
 
        // Traverse up and fix violated property
        int current = size;
        while (Heap[current].getTanggal() > Heap[parent(current)].getTanggal()) {
            swap(current, parent(current));
            current = parent(current);
        }
        size++;
    }

    public void print()
    {
        int num = 1;
        for (int i = 0; i < size; i++) {
            Catatan tmp = extractMax();
            System.out.println(num + ". Tanggal(yymmdd): " + tmp.getTanggal() + " Catatan: " + tmp.getCatatan());
            num++;
        }
    }
}

class Fungsi {
    MinHeap h;
    public Fungsi(MinHeap h){
        this.h = h;
    }

    public void start(){
        System.out.println("Selamat datang di Catatan Masbro.");
    }

    public void menu(){
        System.out.println("Silahkan pilih menu yang diinginkan.");
        System.out.println("1. Tambah Catatan di Command Prompt.");
        System.out.println("2. Print Catatan di Command Prompt.");
        System.out.println("3. Delete Catatan dengan tanggal terdekat.");
        System.out.println("4. Print Catatan ke File txt.");
        System.out.println("5. Selesai");
        System.out.print("Input Anda: ");
    }

    public void misinput(){
        System.out.println("Input salah. Mohon masukkan kembali.");
    }

    public void exit(){
        System.out.println("Terima kasih telah menggunakan Catatan Masbro!");
        System.exit(0);
    }

    public void exitconfirm(){
        System.out.printf("Yakin ingin keluar?\n1.Yes\n2.No\nInput Anda: ");
    }

    public void input11(){
        System.out.print("Tanggal (yymmdd)(contoh: 230515): ");
    }
    
    public void input12(){
        System.out.println("Catatan:");
    }
    
    public void input13(Catatan input){
        h.insert(input);
        System.out.println("Catatan anda sudah masuk ke heaptree!");
    }
        
    public void input2(){
        System.out.println("Berikut adalah catatan anda.");
    }

    public void input3(){
        Catatan tmp = h.extractMax();
        System.out.println("Catatan yang dihapus:");
        System.out.println("Tanggal(yymmdd): " + tmp.getTanggal() + " Catatan: " + tmp.getCatatan());
    }

    public MinHeap getMinHeap(){
        return h;
    }


}

public class CatatanMasbro {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        int size = 50, command, exit, tanggal;
        String catatan;
        MinHeap heap = new MinHeap(size);
        Fungsi f = new Fungsi(heap);
        
        f.start();
        while(true){
            f.menu();
            command = sc.nextInt();
            switch(command){
                case 1: 
                    f.input11();
                    tanggal = sc.nextInt();
                    f.input12();
                    catatan = sc.nextLine();
                    Catatan note = new Catatan(tanggal, catatan);
                    f.input13(note);
                case 2:
                    f.input2();
                    MinHeap temp = f.getMinHeap();
                    temp.print();
                case 3:
                    f.input3();
                case 4:
                case 5: 
                    f.exitconfirm();
                    exit = sc.nextInt();
                    switch(exit){
                        case 1:
                            f.exit();
                        case 2:break;
                        default: f.misinput();
                    }
                    break;
                default: f.misinput();
            }
        }
    }
}