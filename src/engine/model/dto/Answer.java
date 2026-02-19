package engine.model.dto;

import java.util.List;

public class Answer {
    private List<Integer> answer;

    public List<Integer> getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answer=" + answer +
                '}';
    }
}