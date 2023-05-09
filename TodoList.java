import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoList {
    private List<Task> tasks;

    public TodoList() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public TodoList filterByDeadline(Date deadline) {
        TodoList filteredList = new TodoList();
        for (Task task : tasks) {
            if (task.getDeadline().equals(deadline)) {
                filteredList.addTask(task);
            }
        }
        return filteredList;
    }

    public void printTasks() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public int getSize() {
        return tasks.size();
    }
}
