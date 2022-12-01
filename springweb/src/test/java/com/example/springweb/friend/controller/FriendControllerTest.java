package com.example.springweb.friend.controller;

import com.example.springweb.friend.data.dto.FriendDTO;
import com.example.springweb.friend.controller.FriendController;
import com.example.springweb.friend.exception.FriendNotFoundException;
import com.example.springweb.friend.services.FriendService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FriendController.class)
class FriendControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendService friendService;

    @Nested
    class FindFriends {
        @Test
        void findAll() throws Exception {
            when(friendService.findAll()).thenReturn(List.of(
                FriendDTO.builder().id(1L).build(),
                FriendDTO.builder().id(2L).build(),
                FriendDTO.builder().id(3L).build()
            ));

            mockMvc.perform(get("/friends/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[ {'id': 1}, {'id': 2}, {'id': 3}]"))
                ;
        }

        @Test
        void findByName() throws Exception {
            when(friendService.findByName("world")).thenReturn(List.of(
                FriendDTO.builder().id(1L).name("world").build()
            ));

            mockMvc.perform(get("/friends/?name=world"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[ {'id': 1, 'name': 'world'}]"))
            ;
        }

        @Test
        void findById() throws Exception {
            when(friendService.findByFriendId(1L)).thenReturn(List.of(
                FriendDTO.builder().id(1L).name("world").build()
            ));

            mockMvc.perform(get("/friends/?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[ {'id': 1, 'name': 'world'}]"))
            ;
        }


    }
    @Nested
    class GetFriend {
        @Test
        void successful() throws Exception {
            when(friendService.getFriend(123L)).thenReturn(FriendDTO.builder().id(1L).name("world").build());

            mockMvc.perform(get("/friends/123"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id': 1, 'name': 'world'}"))
            ;
        }

        @Test
        void friendNotFound() throws Exception {
            when(friendService.getFriend(123L)).thenThrow(new NoSuchElementException());

            mockMvc.perform(get("/friends/123"))
                .andExpect(status().isNotFound());
        }

    }

    @Nested
    class HelloFriend {
        @Test
        void justHello() throws Exception {
            String name = "world";
            mockMvc.perform(get("/friends/hello/" + name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello " + name)));
        }

        @Test
        void helloAndSave() throws Exception {
            LocalDateTime beforeTs = LocalDateTime.now().minusMinutes(1);
            String name = "world";

            when(friendService.add(any(FriendDTO.class))).thenAnswer(a -> {
               FriendDTO newFriend = a.getArgument(0);
               assertEquals(name, newFriend.getName());
               assertTrue(newFriend.getCreateDt().isAfter(beforeTs));

               newFriend.setId(123L);
               return newFriend;
            });

            mockMvc.perform(post("/friends/hello/" + name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.format("Hello %s. Your Id is %d", name, 123L))));
        }
    }

    @Nested
    class DeleteFriend {
        @Test
        void successful() throws Exception {
            mockMvc.perform(delete("/friends/123"))
                .andExpect(status().isNoContent());

            verify(friendService).removeFriend(123L);
        }

        @Test
        void friendNotFound() throws Exception {
            doThrow(new EmptyResultDataAccessException(1)).when(friendService).removeFriend(123L);

            mockMvc.perform(delete("/friends/123"))
                .andDo(print())
                .andExpect(status().isNotFound());
        }
    }

    @Nested
    class UpdateFriend {
        @Test
        void successful() throws Exception {
            when(friendService.updateName(123, "newName"))
                .thenReturn(FriendDTO.builder().id(123L).name("newName").build());

            mockMvc.perform(patch("/friends/123/newName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.name", is("newName")));

            verify(friendService).updateName(123L, "newName");
        }

        @Test
        void friendNotFound() throws Exception {
            when(friendService.updateName(123, "newName"))
                .thenThrow(new FriendNotFoundException());

            mockMvc.perform(patch("/friends/123/newName"))
                .andExpect(status().isNotFound());
        }
    }
}