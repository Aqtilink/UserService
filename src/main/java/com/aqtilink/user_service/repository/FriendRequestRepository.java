package com.aqtilink.user_service.repository;

import com.aqtilink.user_service.model.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.aqtilink.user_service.model.User;

import java.util.List;
import java.util.UUID;


@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID> {

    @Query("SELECT CASE WHEN COUNT(fr) > 0 THEN true ELSE false END " +
           "FROM FriendRequest fr " +
           "WHERE (fr.sender.clerkId = :senderClerkId AND fr.receiver.clerkId = :receiverClerkId) " +
           "OR (fr.sender.clerkId = :receiverClerkId AND fr.receiver.clerkId = :senderClerkId)")
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

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.receiver.clerkId = :clerkId AND fr.status = 'PENDING'")
    List<FriendRequest> findPendingRequestsForUser(@Param("clerkId") String clerkId);

    void deleteById(UUID id);

    @Transactional
    @Modifying
    @Query("DELETE FROM FriendRequest fr WHERE fr.sender.clerkId = :clerkId OR fr.receiver.clerkId = :clerkId")
    void deleteAllByUserClerkId(@Param("clerkId") String clerkId);
}