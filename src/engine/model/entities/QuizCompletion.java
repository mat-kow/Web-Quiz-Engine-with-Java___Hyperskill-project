package engine.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class QuizCompletion {

    @JsonIgnore
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long completionId;

    @JsonProperty("id")
    private long quiz_id;
    private LocalDateTime completedAt;

    @JsonIgnore
    private long userId;

    public QuizCompletion() {
    }

    public QuizCompletion(long quiz_id, LocalDateTime completedAt, long userId) {
        this.quiz_id = quiz_id;
        this.completedAt = completedAt;
        this.userId = userId;
    }

    public long getCompletionId() {
        return completionId;
    }

    public long getQuiz_id() {
        return quiz_id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "QuizCompletion{" +
                "completionId=" + completionId +
                ", quiz_id=" + quiz_id +
                ", completedAt=" + completedAt +
                ", userId=" + userId +
                '}';
    }
}
