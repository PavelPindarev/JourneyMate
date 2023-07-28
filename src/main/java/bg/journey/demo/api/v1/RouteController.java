package bg.journey.demo.api.v1;

import bg.journey.demo.dto.payload.PictureUploadPayloadDTO;
import bg.journey.demo.dto.payload.RouteCreateDTO;
import bg.journey.demo.dto.response.CategoryDTO;
import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.dto.response.RouteDetailsViewDTO;
import bg.journey.demo.dto.response.RouteViewDTO;
import bg.journey.demo.security.UserPrincipal;
import bg.journey.demo.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

import static bg.journey.demo.config.AppConstants.API_BASE;

@RestController
@RequestMapping(path = API_BASE + "/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<RouteViewDTO>>> getAllRoutes() {
        List<RouteViewDTO> routeViews = routeService.getAllRoutes();

        return ResponseEntity.ok(
                ResponseDTO
                        .<List<RouteViewDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .content(routeViews)
                        .message("Successfully returned all routes!")
                        .build()
        );
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<ResponseDTO<RouteDetailsViewDTO>> getRoute(
            @PathVariable(value = "routeId") Long routeId) {
        RouteDetailsViewDTO routeById = routeService.getRouteById(routeId);
        return ResponseEntity.ok(
                ResponseDTO
                        .<RouteDetailsViewDTO>builder()
                        .status(HttpStatus.OK.value())
                        .content(routeById)
                        .message("Successfully returned route by id!")
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<Object>> createRoute(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                           @Valid @RequestBody RouteCreateDTO routeCreateDTO,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest()
                    .body(
                            ResponseDTO
                                    .builder()
                                    .status(HttpStatus.BAD_REQUEST.value())
                                    .message(String.join(", ", errors))
                                    .content(null)
                                    .build()
                    );
        }
        routeService.createRoute(routeCreateDTO, userPrincipal.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ResponseDTO
                                .builder()
                                .status(HttpStatus.CREATED.value())
                                .message("Successfully created a route!")
                                .content(null)
                                .build()
                );
    }

    @GetMapping("/categories")
    public ResponseEntity<ResponseDTO<Set<CategoryDTO>>> getAllCategories() {
        Set<CategoryDTO> allCategories = routeService.getAllCategories();

        return ResponseEntity.ok(
                ResponseDTO
                        .<Set<CategoryDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .content(allCategories)
                        .message("Successfully returned all categories!")
                        .build()
        );
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') OR @securityExpressionUtility.isRouteOwner(#routeId, principal)")
    @PostMapping(value = "/{routeId}/main-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<Object>> setRouteMainPicture(@PathVariable(value = "routeId") Long routeId,
                                                                   @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                   @RequestParam(required = false) String title,
                                                                   @RequestPart MultipartFile file) {
        routeService.setMainPicture(routeId, PictureUploadPayloadDTO
                .builder()
                .multipartFile(file)
                .title(title)
                .build()
        );

        return ResponseEntity
                .ok(
                        ResponseDTO
                                .builder()
                                .status(HttpStatus.OK.value())
                                .content(null)
                                .message("Route main picture set successfully!")
                                .build()

                );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR @securityExpressionUtility.isRouteOwner(#routeId, principal)")
    @PostMapping(value = "/{routeId}/pictures", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<Object>> addRoutePicture(@PathVariable(value = "routeId") Long routeId,
                                                               @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                               @RequestParam(required = false) String title,
                                                               @RequestPart MultipartFile file) {
        routeService.addPicture(routeId, PictureUploadPayloadDTO
                .builder()
                .multipartFile(file)
                .title(title)
                .build()
        );

        return ResponseEntity
                .ok(
                        ResponseDTO
                                .builder()
                                .status(HttpStatus.OK.value())
                                .content(null)
                                .message("Route pictures added successfully!")
                                .build()

                );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR @securityExpressionUtility.isRouteOwner(#routeId, principal)")
    @DeleteMapping(value = "/{routeId}/main-picture")
    public ResponseEntity<ResponseDTO<Object>> deleteRouteMainPicture(@PathVariable(value = "routeId") Long routeId,
                                                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        routeService.deleteMainPicture(routeId);

        return ResponseEntity
                .ok(
                        ResponseDTO
                                .builder()
                                .status(HttpStatus.OK.value())
                                .content(null)
                                .message("Route main picture deleted successfully!")
                                .build()

                );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR @securityExpressionUtility.isRouteOwner(#routeId, principal)")
    @DeleteMapping(value = "/{routeId}/pictures/{pictureId}")
    public ResponseEntity<ResponseDTO<Object>> deleteAPicture(@PathVariable(value = "routeId") Long routeId,
                                                              @PathVariable(value = "pictureId") Long pictureId,
                                                              @AuthenticationPrincipal UserPrincipal userPrincipal) {
        routeService.deleteAPicture(routeId, pictureId);

        return ResponseEntity
                .ok(
                        ResponseDTO
                                .builder()
                                .status(HttpStatus.OK.value())
                                .content(null)
                                .message("Route picture deleted successfully!")
                                .build()

                );
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') OR @securityExpressionUtility.isRouteOwner(#routeId, principal)")
    @DeleteMapping(value = "/{routeId}")
    public ResponseEntity<ResponseDTO<Object>> deleteARoute(@PathVariable(value = "routeId") Long routeId,
                                                            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        routeService.deleteARoute(routeId);

        return ResponseEntity
                .ok(
                        ResponseDTO
                                .builder()
                                .status(HttpStatus.OK.value())
                                .content(null)
                                .message("Route deleted successfully!")
                                .build()

                );
    }

}
