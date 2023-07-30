package com.ChemiChat.repository;

import com.ChemiChat.model.Room;
import com.ChemiChat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByMembers(User user);

    Room findByName(String name);
}
