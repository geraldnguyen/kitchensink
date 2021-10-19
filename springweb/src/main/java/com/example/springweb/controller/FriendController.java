package com.example.springweb.controller;

import com.example.springweb.data.dto.FriendDTO;
import com.example.springweb.services.FriendService;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A rest controller for Friend-related operations
 */
@RestController
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }


    /**
     * Response to GET /friends/ request
     * @param name (optional) filter by name
     * @param id (optional) filter by id
     * @return a list of friends
     */
    @GetMapping("/")
    public List<FriendDTO> findFriends(@Nullable @RequestParam String name, @Nullable @RequestParam Long id) {
        if (id != null) {
            return friendService.findByFriendId(id);
        } else if (StringUtils.hasText(name)) {
            return friendService.findByName(name);
        } else {
            return friendService.findAll();
        }
    }

    /**
     * Response to GET /friends/hello/{friend's name} request
     * @param friend a friend's name
     * @return a friendly Hello
     */
    @GetMapping("/hello/{friend}")
    public String hello(@PathVariable String friend) {
        return String.format("Hello %s", friend);
    }

    /**
     * Response to POST /friends/hello/{friend's name} request
     * @param friend a friend's name which is to be saved
     * @return a friendly Hello together with Id of the new friend
     */
    @PostMapping("/hello/{friend}")
    public String helloAndSave(@PathVariable String friend) {
        FriendDTO dto = friendService.add(FriendDTO.builder().name(friend).createDt(LocalDateTime.now()).build());
        return String.format("%s. Your Id is %d", hello(friend), dto.getId());
    }

    /**
     * Un-friend
     * Response to DELETE /friends/{friend's id} request, return 204 if successful
     * @param friendId the id of the friend
     */
    @DeleteMapping("/{friendId}")
    public void deleteFriend(@PathVariable long friendId) {
        friendService.removeFriend(friendId);
    }

    /**
     * Update friend name
     * Response to PATCH /friends/{friend's id}/{new name} request, return update friend info
     * @param friendId the id of the friend
     * @param newName the new name of the friend
     * @return updated friend's info
     */
    @PatchMapping("/{friendId}/{newName}")
    public FriendDTO updateFriend(@PathVariable long friendId, @PathVariable String newName) {
        return friendService.updateName(friendId, newName);
    }
}