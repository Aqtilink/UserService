package com.aqtilink.user_service.repository;

import com.aqtilink.user_service.model.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.aqtilink.user_service.model.User;

import java.util.List;
import java.util.UUID;


@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID> {

    @Query("SELECT CASE WHEN COUNT(fr) > 0 THEN true ELSE false END " +
           "FROM FriendRequest fr " +
           "WHERE fr.sender.id = :senderId AND fr.receiver.id = :receiverId")
    boolean existsBySenderIdAndReceiverId(@Param("senderId") UUID senderId, @Param("receiverId") UUID receiverId);

    @Query("SELECT fr.receiver FROM FriendRequest fr " +
        "WHERE fr.sender.id = :userId AND fr.status = 'ACCEPTED' " +
        "UNION " +
        "SELECT fr.sender FROM FriendRequest fr " +
        "WHERE fr.receiver.id = :userId AND fr.status = 'ACCEPTED'")
    List<User> findFriendsOfUser(@Param("userId") UUID userId);

    @Query("SELECT fr.id FROM FriendRequest fr")
    List<UUID> findAllRequests();

    void deleteById(UUID id);
}

