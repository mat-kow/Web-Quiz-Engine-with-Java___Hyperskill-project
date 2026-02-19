package engine.service;

import engine.model.dto.Answer;
import engine.model.entities.Quiz;
import engine.model.entities.QuizCompletion;
import org.springframework.data.domain.Page;

public interface QuizService {
    Quiz addQuiz(Quiz quiz);

    Quiz getQuiz(long id);

    Boolean solve(long id, Answer answer);

    Page<Quiz> getAll(int pageNo);

    Boolean delete(long id);

    Page<QuizCompletion> getCompleted(int pageNo);
}
