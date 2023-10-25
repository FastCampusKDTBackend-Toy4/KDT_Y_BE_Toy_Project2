package com.kdt_y_be_toy_project2.global.error;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timeStamp, String message) {

}
