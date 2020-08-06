package wooteco.subway.maps.map.dto;

import wooteco.subway.maps.map.domain.SubwayPath;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.maps.station.dto.StationResponse;
import wooteco.subway.members.member.domain.LoginMember;
import wooteco.subway.members.member.domain.Member;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PathResponseAssembler {

    public static final int DEFAULT_FARE = 1250;

    public static PathResponse assemble(SubwayPath subwayPath, Map<Long, Station> stations, int age) {
        List<StationResponse> stationResponses = subwayPath.extractStationId().stream()
                .map(it -> StationResponse.of(stations.get(it)))
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();

        return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance, calculateFare(distance, age));
    }

    private static int calculateFare(int distance, int age) {
        int fare = DEFAULT_FARE;
        distance -= 10;
        if(distance > 0) {
            fare += (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }
        distance -= 40;
        if(distance > 0) {
            fare += (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
            fare += ((distance - 50) / 8)*100;
        }
        return calculateAgeFare(fare, age);
    }

    private static int calculateAgeFare(int fare, int age) {
        if(age >= 19){
            return fare;
        }
        if(age >= 13) {
            return (int)((fare - 350) * 0.8);
        }
        if(age >= 6) {
            return (int)((fare - 350) * 0.5);
        }
        return 0;
    }
}
