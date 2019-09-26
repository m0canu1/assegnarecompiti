import java.time.LocalDateTime;

public class Turn {

    private LocalDateTime startTime;

    public Turn(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
