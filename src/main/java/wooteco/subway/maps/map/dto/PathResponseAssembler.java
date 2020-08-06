package wooteco.subway.maps.map.dto;

import wooteco.subway.maps.map.domain.SubwayPath;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.maps.station.dto.StationResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, Map<Long, Station> stations) {
        List<StationResponse> stationResponses = subwayPath.extractStationId().stream()
                .map(it -> StationResponse.of(stations.get(it)))
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();

        //// TODO: 2020/08/06 여기서 요금 관련 로직 넣어주기
        int fare = 1250;
        fare = calculareFare(distance, fare);

        return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance, fare);
    }

    private static int calculareFare(int distance, int fare) {
        if(distance >= 10) {
            fare += (((distance % 50) - 10) / 5) * 100;
        }
        if(distance >= 50) {
            fare += ((distance - 50) / 8)*100;
        }
        return fare;
    }
}
