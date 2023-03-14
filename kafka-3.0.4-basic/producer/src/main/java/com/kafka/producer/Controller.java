package com.kafka.producer;

import com.kafka.producer.common.Foo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private KafkaTemplate<Object, Object> template;

    @PostMapping("/send/foo/{what}")
    public void sendFoo(@PathVariable String what){
        this.template.send("first", new Foo1(what));
    }
}
