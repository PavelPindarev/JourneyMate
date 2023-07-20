package bg.journey.demo.repository;

import bg.journey.demo.model.entity.RoleEntity;
import bg.journey.demo.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(RoleEnum roleName);
}
