package bg.journey.demo.service;

import bg.journey.demo.dto.payload.RouteCreateDTO;
import bg.journey.demo.dto.response.CategoryDTO;
import bg.journey.demo.dto.response.ReactionDTO;
import bg.journey.demo.dto.response.RouteDetailsViewDTO;
import bg.journey.demo.dto.response.RouteViewDTO;
import bg.journey.demo.exception.CategoryNotFoundException;
import bg.journey.demo.exception.NotAuthorizedException;
import bg.journey.demo.exception.RouteNotFoundException;
import bg.journey.demo.model.entity.CategoryEntity;
import bg.journey.demo.model.entity.RouteEntity;
import bg.journey.demo.model.entity.UserEntity;
import bg.journey.demo.model.enums.ReactionTargetType;
import bg.journey.demo.model.mapper.CategoryMapper;
import bg.journey.demo.model.mapper.PictureMapper;
import bg.journey.demo.model.mapper.ReactionMapper;
import bg.journey.demo.repository.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;
    private final PictureMapper pictureMapper;
    private final ReactionMapper reactionMapper;
    private final CategoryRepository categoryRepository;
    private final ReactionRepository reactionRepository;

    public RouteService(RouteRepository routeRepository,
                        UserRepository userRepository,
                        CategoryMapper categoryMapper,
                        PictureMapper pictureMapper,
                        ReactionMapper reactionMapper,
                        CategoryRepository categoryRepository,
                        ReactionRepository reactionRepository) {
        this.routeRepository = routeRepository;
        this.userRepository = userRepository;
        this.categoryMapper = categoryMapper;
        this.pictureMapper = pictureMapper;
        this.reactionMapper = reactionMapper;
        this.categoryRepository = categoryRepository;
        this.reactionRepository = reactionRepository;
    }

    //    @Cacheable(value = "routesCache")
    public List<RouteViewDTO> getAllRoutes() {
        List<RouteEntity> routeEntities = this.routeRepository.findAll();

        List<RouteViewDTO> result = new ArrayList<>();
        for (RouteEntity e : routeEntities) {

            Set<CategoryDTO> categories = this.categoryRepository.findAllByRoutes(e)
                    .stream()
                    .map(categoryMapper::categoryEntityToCategoryDTO).collect(Collectors.toSet());

            Set<ReactionDTO> reactions = this.reactionRepository
                    .findAllByTargetEntityIdAndReactionTargetType(e.getId(), ReactionTargetType.ROUTE)
                    .stream()
                    .map(reactionMapper::reactionEntityToReactionDTO).collect(Collectors.toSet());

            RouteViewDTO routeViewDTO = RouteViewDTO.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .levelType(e.getLevelType())
                    .description(e.getDescription())
                    .authorUsername(e.getAuthor().getUsername())
                    .mainPicture(pictureMapper.pictureEntityToPictureDTO(e.getMainPicture()))
                    .categories(categories)
                    .reactions(reactions)
                    .build();
            result.add(routeViewDTO);
        }
        return result;
    }

    public RouteDetailsViewDTO getRouteById(Long routeId) {
        RouteEntity e = this.routeRepository.findById(routeId)
                .orElseThrow(RouteNotFoundException::new);

        Set<CategoryDTO> categories = this.categoryRepository.findAllByRoutes(e)
                .stream()
                .map(categoryMapper::categoryEntityToCategoryDTO).collect(Collectors.toSet());

        Set<ReactionDTO> reactions = this.reactionRepository
                .findAllByTargetEntityIdAndReactionTargetType(e.getId(), ReactionTargetType.ROUTE)
                .stream()
                .map(reactionMapper::reactionEntityToReactionDTO).collect(Collectors.toSet());

        return RouteDetailsViewDTO.builder()
                .id(e.getId())
                .name(e.getName())
                .gpxCoordinates(e.getGpxCoordinates())
                .videoUrl(e.getVideoUrl())
                .levelType(e.getLevelType())
                .description(e.getDescription())
                .authorUsername(e.getAuthor().getUsername())
                .mainPicture(pictureMapper.pictureEntityToPictureDTO(e.getMainPicture()))
                .pictures(e.getPictures().stream()
                        .map(pictureMapper::pictureEntityToPictureDTO).collect(Collectors.toSet()))
                .categories(categories)
                .reactions(reactions)
                .build();

    }

    public void createRoute(RouteCreateDTO routeCreateDTO, String principalName) {
        UserEntity author = this.userRepository.findByUsernameOrEmail(principalName, principalName)
                .orElseThrow(NotAuthorizedException::new);

        Set<CategoryEntity> categories = routeCreateDTO.getCategoriesId()
                .stream()
                .map(l -> this.categoryRepository.findById(l)
                        .orElseThrow(CategoryNotFoundException::new)
                ).collect(Collectors.toSet());


        RouteEntity routeEntity = RouteEntity.builder()
                .name(routeCreateDTO.getName())
                .description(routeCreateDTO.getDescription())
                .author(author)
                .levelType(routeCreateDTO.getLevelType())
                .videoUrl(routeCreateDTO.getVideoUrl())
                .gpxCoordinates(routeCreateDTO.getGpxCoordinates())
                .categories(categories)
                .build();

        this.routeRepository.save(routeEntity);
    }
}
