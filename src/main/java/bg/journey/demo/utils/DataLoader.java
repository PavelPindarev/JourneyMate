package bg.journey.demo.utils;


import bg.journey.demo.model.entity.*;
import bg.journey.demo.model.enums.CategoryType;
import bg.journey.demo.model.enums.LevelEnum;
import bg.journey.demo.model.enums.ReactionEnum;
import bg.journey.demo.model.enums.RoleEnum;
import bg.journey.demo.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Set;

@AllArgsConstructor
@Component
@ConditionalOnProperty("app.dataLoader")
public class DataLoader implements ApplicationRunner {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final CategoryRepository categoryRepository;

    private final CommentRepository commentRepository;

    private final PictureRepository pictureRepository;

    private final ReactionRepository reactionRepository;

    private final RouteRepository routeRepository;


    @Transactional
    public void run(ApplicationArguments args) {
        if (roleRepository.count() == 0) {
            //set user roles
            RoleEntity adminRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.ADMIN).build());
            RoleEntity userRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.USER).build());
//USERS
            userRepository.save(
                    UserEntity
                            .builder()
                            .username("deleted")
                            .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                            .enabled(false)
                            .roles(Set.of(userRole))
                            .firstName("Deleted")
                            .lastName("Deleted")
                            .email("deleted@deleted.com")
                            .build()
            );

            UserEntity user = userRepository.save(
                    UserEntity
                            .builder()
                            .username("user4o")
                            .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                            .enabled(true)
                            .roles(Set.of(userRole))
                            .firstName("User")
                            .lastName("Userov")
                            .email("test@test.com")
                            .build()
            );

            UserEntity admin = userRepository.save(
                    UserEntity
                            .builder()
                            .username("admin")
                            .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                            .enabled(true)
                            .roles(Set.of(adminRole))
                            .firstName("Admin")
                            .lastName("Adminov")
                            .email("admin@admin.com")
                            .build()
            );

//REACTIONS
            ReactionEntity likeReaction = reactionRepository.save(
                    ReactionEntity
                            .builder()
                            .reactionType(ReactionEnum.LIKE)
                            .author(admin)
                            .build()
            );
            ReactionEntity dislikeReaction = reactionRepository.save(
                    ReactionEntity
                            .builder()
                            .reactionType(ReactionEnum.DISLIKE)
                            .author(user)
                            .build()
            );

//CATEGORIES
            CategoryEntity category1 = categoryRepository.save(
                    CategoryEntity
                            .builder()
                            .name(CategoryType.PEDESTRIAN)
                            .description("Tracks for pedestrians.")
                            .build()
            );
            CategoryEntity category2 = categoryRepository.save(
                    CategoryEntity
                            .builder()
                            .name(CategoryType.BICYCLE)
                            .description("Tracks for bicycles.")
                            .build()
            );
            CategoryEntity category3 = categoryRepository.save(
                    CategoryEntity
                            .builder()
                            .name(CategoryType.CAR)
                            .description("Tracks for cars.")
                            .build()
            );

//ROUTES
            RouteEntity route1 = routeRepository.save(
                    RouteEntity
                            .builder()
                            .name("Връх Кумата")
                            .gpxCoordinates("<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"J!Track Gallery - http://jtrackgallery.net/forum\" version=\"1.1\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >")
                            .description("Един от най-удобните изходни пунктове за изкачване на Черни връх (2290 м) във Витоша е хижа Кумата.")
                            .levelType(LevelEnum.BEGINNER)
                            .author(user)
                            .categories(Set.of(category1, category2))
                            .build()
            );
        }
    }
}
