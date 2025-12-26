package com.aqtilink.user_service.service;

import com.aqtilink.user_service.dto.FriendDTO;
import com.aqtilink.user_service.model.User;
import com.aqtilink.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Optional;



import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final FriendRequestService requestrepo;

    public UserService(UserRepository repo, FriendRequestService requestrepo) {
        this.repo = repo;
        this.requestrepo = requestrepo;
    }

    public User create(User user) {
        return repo.save(user);
    }

    public User getByClerkId(String clerkId) {
        return repo.findByClerkId(clerkId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
    public List<User> getAll() {
        return repo.findAll();
    }
    public User updateByClerkId(String clerkId, User updated){
        User exists = getByClerkId(clerkId);
        if(updated.getFirstName() != null){
            exists.setFirstName(updated.getFirstName());
        }
        if(updated.getLastName() != null){
            exists.setLastName(updated.getLastName());
        }
        if(updated.getEmail() != null){
            exists.setEmail(updated.getEmail());
        }
        if(updated.getAge() != null){
            exists.setAge(updated.getAge());
        }
        if(updated.getCity() != null){
            exists.setCity(updated.getCity());
        }
        return repo.save(exists);
    }
    public void deleteByClerkId(String clerkId){
        if(!repo.existsByClerkId(clerkId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        repo.deleteByClerkId(clerkId);
    }

    public String getEmailByClerkId(String clerkId){
        return getByClerkId(clerkId).getEmail();
    }
    public List<FriendDTO> getFriendsByClerkId(String clerkId){
        repo.findByClerkId(clerkId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return requestrepo.findFriendsOfUserByClerkId(clerkId).stream().map(u -> new FriendDTO(u.getClerkId(), u.getFirstName(), u.getLastName(), u.getEmail())).toList();
    }

    public List<FriendDTO> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        return repo.searchByName(query.trim()).stream()
                .map(u -> new FriendDTO(u.getClerkId(), u.getFirstName(), u.getLastName(), u.getEmail()))
                .toList();
    }

    public User getOrCreateByClerkId(String clerkId, String email, String firstName, String lastName) {
        Optional<User> existing = repo.findByClerkId(clerkId);
        if (existing.isPresent()) {
            return existing.get();
        }
        User newUser = new User();
        newUser.setClerkId(clerkId);
        newUser.setEmail(email != null ? email : clerkId + "@unknown.local");
        newUser.setFirstName(firstName != null ? firstName : "");
        newUser.setLastName(lastName != null ? lastName : "");
        newUser.setAge(0); // default age when provisioning from token
        newUser.setCity(""); // default city when provisioning from token
        return repo.save(newUser);
    }


}

