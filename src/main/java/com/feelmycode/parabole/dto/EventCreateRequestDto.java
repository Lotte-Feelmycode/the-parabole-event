package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.EventImage;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class EventCreateRequestDto {

    @NotNull
    private String createdBy;

    @NotBlank(message = "이벤트 타입을 선택해주세요.")
    private String type;

    @NotBlank(message = "이벤트 제목을 입력해주세요.")
    private String title;

    @NotNull
    private String startAt;

    @NotNull
    private String endAt;

    @NotBlank(message = "이벤트 설명을 입력해주세요.")
    private String descript;
    private EventImage eventImage;
    private List<EventPrizeCreateRequestDto> eventPrizeCreateRequestDtos;

    public EventCreateRequestDto(String createdBy, String type, String title,
        String startAt, String endAt, String descript, EventImage eventImage,
        List<EventPrizeCreateRequestDto> eventPrizeCreateRequestDtos) {
        this.createdBy = createdBy;
        this.type = type;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.descript = descript;
        this.eventImage = eventImage;
        this.eventPrizeCreateRequestDtos = eventPrizeCreateRequestDtos;
    }

    public void setEventImage(String bannerImg, String detailImg) {
        this.eventImage = new EventImage(bannerImg, detailImg);
    }
}
