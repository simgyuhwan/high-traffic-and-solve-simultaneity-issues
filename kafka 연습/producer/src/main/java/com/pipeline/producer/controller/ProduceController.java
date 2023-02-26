package com.pipeline.producer.controller;

import com.google.gson.Gson;
import com.pipeline.producer.vo.UserEventVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ProduceController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/api/select")
    public void selectColor(@RequestHeader("user-agent") String userAgentName, @RequestParam("color") String colorName,
                            @RequestParam("user") String userName) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        Date now = new Date();
        Gson gson = new Gson();

        UserEventVO userEventVO = UserEventVO.of(sdfDate.format(now), userAgentName, colorName, userName);
        String jsonColorLog = gson.toJson(userEventVO);

        kafkaTemplate.send("select-color", jsonColorLog).addCallback(
                new ListenableFutureCallback<SendResult<String, String>>() {

                    @Override
                    public void onFailure(Throwable ex) {
                        log.error(ex.getMessage(), ex);
                    }

                    @Override
                    public void onSuccess(SendResult<String, String> result) {
                        log.info(result.toString());
                    }
                }
        );
    }
}
