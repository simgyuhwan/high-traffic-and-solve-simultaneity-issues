package com.pipeline.producer.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEventVO {
    private String timestamp;
    private String userAgent;
    private String colorName;
    private String userName;

    private UserEventVO(String timestamp, String userAgent, String colorName, String userName) {
        this.timestamp = timestamp;
        this.userAgent = userAgent;
        this.colorName = colorName;
        this.userName = userName;
    }

    public static UserEventVO of(String timestamp, String userAgent, String colorName, String userName) {
        return new UserEventVO(timestamp, userAgent, colorName, userName);
    }
}
