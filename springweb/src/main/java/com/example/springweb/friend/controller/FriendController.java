package com.example.springweb.friend.controller;

import com.example.springweb.friend.data.dto.FriendDTO;
import com.example.springweb.friend.services.FriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A rest controller for Friend-related operations
 */
@Slf4j
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
     * Response to a GE T /friends/{friendId} request
     * @param friendId the friend's Id
     * @return the friend associated with provided id
     */
    @GetMapping("/{friendId}")
    public FriendDTO getFriend(@PathVariable long friendId) {
        return friendService.getFriend(friendId);
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{friendId}")
    public void deleteFriend(@PathVariable long friendId) {
        friendService.removeFriend(friendId);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected void handleDeleteNotFoundException(EmptyResultDataAccessException erdae) {
        log.error("Provided Friend Id not found", erdae);
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
