package engine.controller;

import engine.model.dto.Answer;
import engine.model.entities.Quiz;
import engine.model.dto.SolveResult;
import engine.model.entities.QuizCompletion;
import engine.service.QuizService;
import engine.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizController.class);

    private final QuizService quizService;
    private final UserService userService;

    @Autowired
    public QuizController(QuizService quizService, UserService userService) {
        this.quizService = quizService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<Quiz> createQuiz(@RequestBody @Valid Quiz quiz) {
        LOGGER.debug("POST /quizzes {}", quiz);
        Quiz quizDto = quizService.addQuiz(quiz);
        LOGGER.debug("quiz.id = {}", quizDto.getId());

        return ResponseEntity.ok(quizDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable int id) {
        LOGGER.debug("GET /quizzes/{}", id);
        Quiz quiz = quizService.getQuiz(id);
        if (quiz == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quiz);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Quiz> deleteQuiz(@PathVariable int id) {
        LOGGER.debug("DELETE /quizzes/{}", id);
        Boolean deleted = quizService.delete(id);
        if (deleted == null) {
            return ResponseEntity.notFound().build();
        }
        if (deleted)
            return ResponseEntity.status(204).build();
        return ResponseEntity.status(403).build();
    }

    @PostMapping("/{id}/solve")
    public ResponseEntity<SolveResult> solve(@PathVariable int id, @RequestBody Answer answer) {
        LOGGER.debug("POST /quizzes/{} answer={}", id, answer);
        LOGGER.debug("userId: {}", userService.getCurrent().getId());
        Boolean isCorrect = quizService.solve(id, answer);
        LOGGER.debug("is correct: {}", isCorrect);
        if (isCorrect == null) {
            return ResponseEntity.notFound().build();
        }
        String feedback = isCorrect ? "Congratulations, you're right!" : "Wrong answer! Please, try again.";
        return ResponseEntity.ok(new SolveResult(isCorrect, feedback));
    }

    @GetMapping("")
    public ResponseEntity<Page<Quiz>> getAllQuiz(@RequestParam(defaultValue = "0") String page) {
//    public ResponseEntity<List<QuizDto>> getAllQuiz(@RequestParam(defaultValue = "0") String pageNumber) {
        LOGGER.debug("GET /quizzes pageNo = {}", page);
        return ResponseEntity.ok(quizService.getAll(Integer.parseInt(page)));
    }

    @GetMapping("/completed")
    public ResponseEntity<Page<QuizCompletion>> getCompleted(@RequestParam(defaultValue = "0") String page) {
        LOGGER.debug("GET /quizzes/completed ");
        LOGGER.debug("userId: {}", userService.getCurrent().getId());
        return ResponseEntity.ok(quizService.getCompleted(Integer.parseInt(page)));
    }

}



