package bg.journey.demo.api.v1;

import bg.journey.demo.dto.payload.RouteCreateDTO;
import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.dto.response.RouteDetailsViewDTO;
import bg.journey.demo.dto.response.RouteViewDTO;
import bg.journey.demo.security.UserPrincipal;
import bg.journey.demo.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    //TODO:get categories; set pictures; set main picture; delete; route; react !!!

}
