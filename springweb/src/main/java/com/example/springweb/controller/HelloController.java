package com.example.springweb.controller;

import com.example.springweb.data.dto.FriendDTO;
import com.example.springweb.services.FriendService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/hello")
public class HelloController {
    private final FriendService friendService;

    public HelloController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/")
    public List<FriendDTO> findAllFriends() {
        return friendService.findAll();
    }

    @GetMapping("/{friend}")
    public String hello(@PathVariable String friend) {
        return String.format("Hello %s", friend);
    }

    @PostMapping("/{friend}")
    public String helloAndSave(@PathVariable String friend) {
        FriendDTO dto = friendService.add(FriendDTO.builder().name(friend).createDt(LocalDateTime.now()).build());
        return String.format("%s. Your Id is %d", hello(friend), dto.getId());
    }
}
