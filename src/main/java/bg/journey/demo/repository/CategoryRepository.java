package bg.journey.demo.repository;

import bg.journey.demo.model.entity.CategoryEntity;
import bg.journey.demo.model.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Set<CategoryEntity> findAllByRoutes(RouteEntity e);
}
