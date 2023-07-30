package com.ChemiChat.service;

import com.ChemiChat.model.Message;
import com.ChemiChat.model.Room;
import com.ChemiChat.model.User;
import com.ChemiChat.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public Room createRoom(Room room){
        return roomRepository.save(room);
    }

    public void addMemberToRoom(User member, Room room){
        var roomPersist = roomRepository.findById(room.getId())
                .orElseThrow(() -> {
                    throw new RuntimeException("Room with id = {" + room.getId()+"} not found!");
                });
        roomPersist.getMembers().add(member);
        roomRepository.save(roomPersist);
    }

    public List<Room> getRoomsForMember(User user){
        return roomRepository.findAllByMembers(user);
    }

    public Room findByName(String name){
        return roomRepository.findByName(name);
    }

    public Room findById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> {
            throw new RuntimeException("Room with id = {" + roomId+"} not found!");
        });
    }

    public List<Message> getHistory(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> {
            throw new RuntimeException("Room with id = {" + roomId+"} not found!");
        }).getHistory();
    }
}
