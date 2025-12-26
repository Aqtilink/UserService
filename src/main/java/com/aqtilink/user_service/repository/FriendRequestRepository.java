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
           "WHERE fr.sender.clerkId = :senderClerkId AND fr.receiver.clerkId = :receiverClerkId")
    boolean existsBySenderClerkIdAndReceiverClerkId(@Param("senderClerkId") String senderClerkId,
                                                    @Param("receiverClerkId") String receiverClerkId);

    @Query("SELECT fr.receiver FROM FriendRequest fr " +
        "WHERE fr.sender.clerkId = :clerkId AND fr.status = 'ACCEPTED' " +
        "UNION " +
        "SELECT fr.sender FROM FriendRequest fr " +
        "WHERE fr.receiver.clerkId = :clerkId AND fr.status = 'ACCEPTED'")
    List<User> findFriendsOfUserByClerkId(@Param("clerkId") String clerkId);

    @Query("SELECT fr.id FROM FriendRequest fr")
    List<UUID> findAllRequests();

    void deleteById(UUID id);
}

