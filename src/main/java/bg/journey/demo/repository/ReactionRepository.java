package bg.journey.demo.repository;

import bg.journey.demo.model.entity.ReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<ReactionEntity, Long> {
}
