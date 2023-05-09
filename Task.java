import java.util.Date;

public class Task {
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

    @Override
    public String toString() {
        return name + " (" + description + ") - Deadline: " + deadline;
    }
}
