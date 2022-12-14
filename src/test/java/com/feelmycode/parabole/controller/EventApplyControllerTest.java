//package com.feelmycode.parabole.controller;
//
//import static io.restassured.RestAssured.given;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
//import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
//import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;
//
//import com.feelmycode.parabole.dto.EventApplyDto;
//import com.feelmycode.parabole.global.util.JwtUtils;
//import com.feelmycode.parabole.repository.EventParticipantRepository;
//import com.feelmycode.parabole.service.EventParticipantService;
//import groovy.util.logging.Slf4j;
//import io.restassured.RestAssured;
//import io.restassured.builder.RequestSpecBuilder;
//import io.restassured.http.ContentType;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import net.minidev.json.JSONObject;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.restdocs.JUnitRestDocumentation;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.junit.jupiter.api.Test;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@RunWith(SpringJUnit4ClassRunner.class)
//public class EventApplyControllerTest {
//
//    String outputDirectory = "./src/docs/asciidoc/snippets";
//    @LocalServerPort
//    int port;
//
//    @Rule
//    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(outputDirectory);
//
//    private RequestSpecification spec;
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    EventParticipantRepository eventParticipantRepository;
//
//    @Autowired
//    EventParticipantService eventParticipantService;
//
//    @Before
//    public void setUp() {
//        RestAssured.port = port;
//        this.spec = new RequestSpecBuilder().addFilter(
//                documentationConfiguration(this.restDocumentation))
//            .build();
//    }
//
//    //Email= test@test.com userId =1 sellerId =1 ?????? test
//    String sellerToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwicGhvbmUiOiIwMTA1Nzc4NTAyMyIsIm5pY2tuYW1lIjoidGVzdCIsInVzZXJJZCI6MSwiZW1haWwiOiJ0ZXN0QHRlc3QuY29tIiwidXNlcm5hbWUiOiJ0ZXN0IiwiaWF0IjoxNjY4MzkzNDE5LCJleHAiOjE2Njg0Nzk4MTl9.dapdQCoGRkP_HUmQyyCamIWuywGALcvzQRmu2zBoKKY";
//
//    //Email=1111 userId=11 ?????? 1111
//    String userToken = "eyJhbGciOiJIUzI1NiJ9.eyJzZWxsZXJTdG9yZW5hbWUiOiJ1MS1zMSdzIHN0b3JlIiwicm9sZSI6IlJPTEVfU0VMTEVSIiwic2VsbGVySWQiOjEsInBob25lIjoiMDEwNTc3ODUwMjMiLCJuaWNrbmFtZSI6InRlc3QiLCJ1c2VySWQiOjEsImVtYWlsIjoidGVzdEB0ZXN0LmNvbSIsInVzZXJuYW1lIjoidGVzdCIsImlhdCI6MTY2ODQxMDYwNiwiZXhwIjoxNjY4NDk3MDA2fQ.fyAqUxldrU_cSadbVXFop7DlKQHgri69Lm6l_d1tpho";
//
//    @Test
//    @DisplayName("????????? ??????")
//    public void test_insertEventApply() {
//        //given
//        Long eventId = 1L;
//        Long userId = jwtUtils.extractUserId(userToken);
//        EventApplyDto requestDto = new EventApplyDto(null, 1L, 1L);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("eventId", eventId);
//        jsonObject.put("eventPrizeId", 1L);
//        Response resp = given(this.spec)
//            .accept(ContentType.JSON)
//            .contentType(ContentType.JSON)
//            .header("Authorization", "Bearer " + userToken)
//            .filter(
//                document(
//                    "eventparticipant",
//                    preprocessRequest(modifyUris().scheme("http").host("parabole.com"),
//                        prettyPrint()),
//                    preprocessResponse(prettyPrint()),
//                    responseFields(
//                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
//                        fieldWithPath("data").type(JsonFieldType.NULL).description("?????? ??????")
//                    )
//                )
//            ).when().port(port)
//            .body(jsonObject.toJSONString())
//            .post("/api/v1/event/participant");
//
//        //then
//        Assertions.assertEquals(HttpStatus.CREATED.value(), resp.statusCode());
//        eventParticipantRepository.deleteByUserIdAndEventId(userId, eventId);
//    }
//
//    @Test
//    @DisplayName("????????? ?????? ??????")
//    public void test1_eventApplyCheck() {
//        Long eventId = 2L;
//        JSONObject request = new JSONObject();
//
//        request.put("eventId", eventId);
//
//        Response resp = given(this.spec)
//            .accept(ContentType.JSON)
//            .body(request)
//            .header("Authorization", "Bearer " + userToken)
//            .contentType(ContentType.JSON)
//            .filter(
//                document(
//                    "eventparticipant-check",
//                    preprocessRequest(modifyUris().scheme("https").host("parabole.com"),
//                        prettyPrint()),
//                    requestFields(
//                        fieldWithPath("eventId").type(JsonFieldType.NUMBER).description("????????? ?????????")
//                    ),
//                    responseFields(
//                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
//                        fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("?????? ??????")
//                    )
//                )
//            ).when().port(port)
//            .post("/api/v1/event/participant/check");
//
//        //then
//
//        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
//    }
//
////    @Test
////    @DisplayName("?????? ????????? ?????? ????????? ??????")
////    public void test02_getUserEventParticipants() {
////        Response resp = given(this.spec)
////            .header("Authorization", "Bearer " + userToken)
////            .filter(document(
////                    "user-eventparticipant",
////                    preprocessRequest(modifyUris().scheme("http").host("parabole.com"),
////                        prettyPrint()),
////                    preprocessResponse(prettyPrint()),
////
////                    responseFields(
////                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
////                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
////                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ??????"),
////                        fieldWithPath("data.[].userId").type(JsonFieldType.NUMBER).description("?????? ??????"),
////                        fieldWithPath("data.[].eventId").type(JsonFieldType.NUMBER)
////                            .description("????????? ??????"),
////                        fieldWithPath("data.[].eventTimeStartAt").type(JsonFieldType.STRING)
////                            .description("????????? ????????????(yyyy-MM-dd'T'HH:mm:ss.SSSSSS)"),
////                        fieldWithPath("data.[].eventTitle").type(JsonFieldType.STRING)
////                            .description("????????? ??????"),
////                        fieldWithPath("data.[].startAt").type(JsonFieldType.STRING)
////                            .description("????????? ????????????(yyyy-MM-dd'T'HH:mm:ss)"),
////                        fieldWithPath("data.[].endAt").type(JsonFieldType.STRING)
////                            .description("????????? ????????????(yyyy-MM-dd'T'HH:mm:ss)"),
////                        fieldWithPath("data.[].status").type(JsonFieldType.NUMBER)
////                            .description("????????? ??????"),
////                        fieldWithPath("data.[].eventImg").type(JsonFieldType.STRING)
////                            .description("????????? ?????????")
////                    )
////                )
////            ).when().port(port)
////            .get("/api/v1/event/user/participant");
////
////
////        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
////    }
//}
