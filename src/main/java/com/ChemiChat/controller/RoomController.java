package com.ChemiChat.controller;

import com.ChemiChat.dto.RoomDto;
import com.ChemiChat.model.Message;
import com.ChemiChat.model.Room;
import com.ChemiChat.model.User;
import com.ChemiChat.service.RoomService;
import com.ChemiChat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @PostMapping("/rooms/create")
    public void createNewRoom(@RequestBody RoomDto roomDto){
        Room room = new Room();
        room.setName(roomDto.getName());
        room.setMembers(new HashSet<>());
        var r = roomService.createRoom(room);
        System.out.println(r.getName());
    }

    @PostMapping("/room/add")
    public void addMemberToRoom(@RequestParam(value = "username") String username,
                                @RequestParam(value = "roomname") String roomname){
        User member = userService.findUserByUserName(username);
        Room room = roomService.findByName(roomname);
        roomService.addMemberToRoom(member, room);
    }

    @GetMapping("/rooms/all")
    public List<Room> getAllRoomsForUser(@RequestParam(value = "username") String username){
        return roomService.getRoomsForMember(userService.findUserByUserName(username));
    }

    @GetMapping("/room")
    public Room getRoom(@RequestParam("roomId") Long roomId){
        return roomService.findById(roomId);
    }

    @GetMapping("/room/history")
    public List<Message> getRoomMessageHistory(@RequestParam("roomId") Long roomId){
        return roomService.getHistory(roomId);
    }
}
