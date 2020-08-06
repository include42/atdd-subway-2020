package wooteco.subway.maps.map.ui;

import wooteco.security.core.AuthenticationPrincipal;
import wooteco.subway.maps.map.application.MapService;
import wooteco.subway.maps.map.domain.PathType;
import wooteco.subway.maps.map.dto.MapResponse;
import wooteco.subway.maps.map.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.members.member.domain.LoginMember;
import wooteco.subway.members.member.domain.Member;

import java.util.Optional;

@RestController
public class MapController {
    private MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target,
                                                 @RequestParam PathType type) {
        return ResponseEntity.ok(mapService.findPath(source, target, type, Optional.empty()));
    }

    // TODO: 2020/08/06 인증에 대한 정확한 지식이 없어서, 임시변통으로 라우터를 2개로 나눴음. 이후 로그인/비로그인 모두 하나로 요청을 보내도록 로직 수정할 예정. 
    @GetMapping("/paths/login")
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target,
                                                 @RequestParam PathType type, @AuthenticationPrincipal LoginMember member) {
        return ResponseEntity.ok(mapService.findPath(source, target, type, Optional.of(member)));
    }

    @GetMapping("/maps")
    public ResponseEntity<MapResponse> findMap() {
        MapResponse response = mapService.findMap();
        return ResponseEntity.ok(response);
    }
}
