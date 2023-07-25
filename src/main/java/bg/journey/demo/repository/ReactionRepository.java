package bg.journey.demo.repository;

import bg.journey.demo.model.entity.ReactionEntity;
import bg.journey.demo.model.enums.ReactionTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Set;

@Repository
public interface ReactionRepository extends JpaRepository<ReactionEntity, Long> {

    Set<ReactionEntity> findAllByTargetEntityIdAndReactionTargetType(Long id, ReactionTargetType route);
}
