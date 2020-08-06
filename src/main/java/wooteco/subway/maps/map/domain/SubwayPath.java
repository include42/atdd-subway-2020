package wooteco.subway.maps.map.domain;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayPath {

    public static final int DEFAULT_FARE = 1250;

    private List<LineStationEdge> lineStationEdges;

    public SubwayPath(List<LineStationEdge> lineStationEdges) {
        this.lineStationEdges = lineStationEdges;
    }

    public List<LineStationEdge> getLineStationEdges() {
        return lineStationEdges;
    }

    public List<Long> extractStationId() {
        List<Long> stationIds = Lists.newArrayList(lineStationEdges.get(0).getLineStation().getPreStationId());
        stationIds.addAll(lineStationEdges.stream()
                .map(it -> it.getLineStation().getStationId())
                .collect(Collectors.toList()));

        return stationIds;
    }

    public int calculateDuration() {
        return lineStationEdges.stream().mapToInt(it -> it.getLineStation().getDuration()).sum();
    }

    public int calculateDistance() {
        return lineStationEdges.stream().mapToInt(it -> it.getLineStation().getDistance()).sum();
    }

    public int calculateFare(int distance, int age) {
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
        return calculateAgeFare(fare, age) + calculateExtraFare();
    }

    private int calculateAgeFare(int fare, int age) {
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

    private int calculateExtraFare() {
        return lineStationEdges.
                stream().
                map(LineStationEdge::getExtraFare).
                reduce(0, Integer::sum);
    }
}
