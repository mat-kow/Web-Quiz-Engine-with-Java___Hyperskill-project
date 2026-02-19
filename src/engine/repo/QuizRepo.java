package engine.repo;

import engine.model.entities.Quiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizRepo extends CrudRepository<Quiz, Long>, PagingAndSortingRepository<Quiz, Long> {}
