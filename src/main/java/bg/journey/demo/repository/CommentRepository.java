package bg.journey.demo.repository;

import bg.journey.demo.model.entity.CommentEntity;
import bg.journey.demo.model.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByRoute(RouteEntity routeEntity);
}
