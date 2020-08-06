package wooteco.subway.maps.map.documentation;

import io.restassured.RestAssured;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.context.WebApplicationContext;
import wooteco.security.core.TokenResponse;
import wooteco.subway.common.documentation.Documentation;
import wooteco.subway.maps.line.dto.LineResponse;
import wooteco.subway.maps.line.dto.LineStationResponse;
import wooteco.subway.maps.map.application.MapService;
import wooteco.subway.maps.map.application.PathService;
import wooteco.subway.maps.map.domain.PathType;
import wooteco.subway.maps.map.dto.MapResponse;
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.map.ui.MapController;
import wooteco.subway.maps.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@WebMvcTest(controllers = {MapController.class})
public class MapDocumentation extends Documentation {
    @Autowired
    private MapController mapController;
    @MockBean
    private MapService mapService;

    protected TokenResponse tokenResponse;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation){
        super.setUp(context, restDocumentation);
        tokenResponse = new TokenResponse("token");
    }

    @Test
    void getPaths() {
        Map<String, Object> params = new HashMap<>();
        params.put("source", 1L);
        params.put("target", 2L);
        params.put("type", PathType.DURATION);

        List<StationResponse> stationResponses = Lists.newArrayList(
                new StationResponse(1L, "삼성역", LocalDateTime.now(),LocalDateTime.now()),
                new StationResponse(2L, "잠실역", LocalDateTime.now(),LocalDateTime.now())
        );

        when(mapService.findPath(1L,2L,PathType.DURATION)).thenReturn(new PathResponse(stationResponses, 3,4,1250));

        given().log().all().
                header("authorization", "Bearer " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
                params(params).
        when().
                get("/paths").
        then().
                log().all().apply(document("maps/findpath",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName("Authorization").description("Bearer auth credentials")),
                responseFields(
                        fieldWithPath("duration").type(JsonFieldType.NUMBER).description("경로 시간"),
                        fieldWithPath("distance").type(JsonFieldType.NUMBER).description("경로 거리"),
                        fieldWithPath("fare").type(JsonFieldType.NUMBER).description("경로 요금"),
                        fieldWithPath("stations[]").type(JsonFieldType.ARRAY).description("경로 역 목록"),
                        fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("경로 역 ID"),
                        fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("경로 역 이름")))).
                extract();
    }

    @Test
    void getMaps() {
        List<LineStationResponse> lineStationResponses = Lists.newArrayList(
                new LineStationResponse(new StationResponse(1L, "삼성역", LocalDateTime.now(),LocalDateTime.now()),1L,1L,10,10)
        );

        List<LineResponse> lineResponses = Lists.newArrayList(
                new LineResponse(1L, "노선1", "빨간색", LocalTime.now(), LocalTime.now(),10,lineStationResponses,LocalDateTime.now(), LocalDateTime.now()),
                new LineResponse(2L, "노선2", "핑크색", LocalTime.now(), LocalTime.now(),10,lineStationResponses,LocalDateTime.now(), LocalDateTime.now()),
                new LineResponse(3L, "노선3", "노란색", LocalTime.now(), LocalTime.now(),10,lineStationResponses,LocalDateTime.now(), LocalDateTime.now())
        );
        when(mapService.findMap()).thenReturn(new MapResponse(lineResponses));

        given().log().all().
                header("authorization", "Bearer " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                get("/maps").
        then().
                log().all().
                apply(document("maps/findmaps",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName("Authorization").description("Bearer auth credentials")),
                responseFields(
                        fieldWithPath("lineResponses[]").type(JsonFieldType.ARRAY).description("지도 노선 목록"),
                        fieldWithPath("lineResponses[].id").type(JsonFieldType.NUMBER).description("지도 노선 ID"),
                        fieldWithPath("lineResponses[].name").type(JsonFieldType.STRING).description("지도 노선 이름"),
                        fieldWithPath("lineResponses[].color").type(JsonFieldType.STRING).description("지도 노선 색상"),
                        fieldWithPath("lineResponses[].startTime").type(JsonFieldType.STRING).description("지도 노선 시작시간"),
                        fieldWithPath("lineResponses[].endTime").type(JsonFieldType.STRING).description("지도 노선 종료시간"),
                        fieldWithPath("lineResponses[].intervalTime").type(JsonFieldType.NUMBER).description("지도 노선 시간 간격"),
                        fieldWithPath("lineResponses[].stations[]").type(JsonFieldType.ARRAY).description("지도 노선 구간 목록"),
                        fieldWithPath("lineResponses[].stations[].station").type(JsonFieldType.OBJECT).description("지도 노선 구간 역 정보"),
                        fieldWithPath("lineResponses[].stations[].station.id").type(JsonFieldType.NUMBER).description("지도 노선 구간 역 ID"),
                        fieldWithPath("lineResponses[].stations[].station.name").type(JsonFieldType.STRING).description("지도 노선 구간 역 이름"),
                        fieldWithPath("lineResponses[].stations[].preStationId").type(JsonFieldType.NUMBER).description("지도 노선 구간 이전역 ID"),
                        fieldWithPath("lineResponses[].stations[].lineId").type(JsonFieldType.NUMBER).description("지도 노선 구간 노선 ID"),
                        fieldWithPath("lineResponses[].stations[].distance").type(JsonFieldType.NUMBER).description("지도 노선 구간 거리"),
                        fieldWithPath("lineResponses[].stations[].duration").type(JsonFieldType.NUMBER).description("지도 노선 구간 시간"),
                        fieldWithPath("lineResponses[].createdDate").type(JsonFieldType.STRING).description("지도 노선 생성 시간"),
                        fieldWithPath("lineResponses[].modifiedDate").type(JsonFieldType.STRING).description("지도 노선 수정 시간")))).
                extract();
    }
}
