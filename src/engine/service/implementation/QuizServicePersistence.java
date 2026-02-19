package engine.service.implementation;

import engine.model.dto.Answer;
import engine.model.entities.Quiz;
import engine.model.entities.QuizCompletion;
import engine.model.entities.User;
import engine.repo.CompletionRepo;
import engine.repo.QuizRepo;
import engine.service.QuizService;
import engine.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuizServicePersistence implements QuizService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizServicePersistence.class);
    private final QuizRepo quizRepo;
    private final UserService userService;
    private final CompletionRepo completionRepo;

    @Autowired
    public QuizServicePersistence(QuizRepo quizRepo, UserService userService, CompletionRepo completionRepo) {
        this.quizRepo = quizRepo;
        this.userService = userService;
        this.completionRepo = completionRepo;
    }

    @Override
    public Quiz addQuiz(Quiz quiz) {
        User current = userService.getCurrent();
        quiz.setAuthor(current);
        return quizRepo.save(quiz);
    }

    @Override
    public Quiz getQuiz(long id) {
        Optional<Quiz> optionalQuiz = quizRepo.findById(id);
        return optionalQuiz.orElse(null);
    }

    @Override
    public Boolean solve(long id, Answer answer) {
        Optional<Quiz> optionalQuiz = quizRepo.findById(id);
        if (optionalQuiz.isEmpty()) return null;

        Quiz quiz = optionalQuiz.get();
        List<Integer> right = quiz.getAnswer();
        List<Integer> answerList = answer.getAnswer();

        boolean isCorrect;
        if (right == null || right.isEmpty()) {
            isCorrect = answerList == null || answerList.isEmpty();
        }
        else if (answerList == null || answerList.isEmpty())
            isCorrect = false;
        else {
            right.sort(Integer::compareTo);
            answerList.sort(Integer::compareTo);
            isCorrect = right.equals(answerList);
        }

        if (isCorrect)
            completionRepo.save(new QuizCompletion(quiz.getId(), LocalDateTime.now(), userService.getCurrent().getId()));

        return isCorrect;
    }

    @Override
    public Page<Quiz> getAll(int pageNo) {
        Page<Quiz> all = quizRepo.findAll(PageRequest.of(pageNo, 10));
        LOGGER.debug(pageToPretyString(all, Quiz::toString));
        return all;
    }

    @Override
    public Boolean delete(long id) {
        Optional<Quiz> optionalQuiz = quizRepo.findById(id);
        if (optionalQuiz.isEmpty()) return null;
        Quiz quiz = optionalQuiz.get();
        User current = userService.getCurrent();
        if (Objects.equals(quiz.getAuthor().getId(), current.getId())) {
            quizRepo.delete(quiz);
            return true;
        }
        return false;
    }

    @Override
    public Page<QuizCompletion> getCompleted(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("completedAt").descending());
        return completionRepo.findAllByUserId(userService.getCurrent().getId(), pageable);
    }

    private static<T> String pageToPretyString(Page<T> page, Function<T, String> toStringFunction) {
        return page.stream().map(toStringFunction).collect(Collectors.joining("\n"));
    }
}
