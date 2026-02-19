package engine.repo;

import engine.model.entities.QuizCompletion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompletionRepo extends PagingAndSortingRepository<QuizCompletion, Long>, CrudRepository<QuizCompletion, Long> {
    Page<QuizCompletion> findAllByUserId(long userId, Pageable pageable);
}
