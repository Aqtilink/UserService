package com.aqtilink.user_service.service;

import com.aqtilink.user_service.dto.FriendDTO;
import com.aqtilink.user_service.model.User;
import com.aqtilink.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Optional;



import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final FriendRequestService requestrepo;
    private final RestTemplate restTemplate;
    private final String activityServiceUrl;
    private final String serviceApiKey;
    private final String clerkSecretKey;
    private final String clerkApiUrl;

    public UserService(UserRepository repo, FriendRequestService requestrepo, 
                      RestTemplate restTemplate, 
                      @Value("${activity-service.url:http://localhost:8081}") String activityServiceUrl,
                      @Value("${service.api-key}") String serviceApiKey,
                      @Value("${clerk.secret-key:}") String clerkSecretKey,
                      @Value("${clerk.api-url:https://api.clerk.com/v1}") String clerkApiUrl) {
        this.repo = repo;
        this.requestrepo = requestrepo;
        this.restTemplate = restTemplate;
        this.activityServiceUrl = activityServiceUrl;
        this.serviceApiKey = serviceApiKey;
        this.clerkSecretKey = clerkSecretKey;
        this.clerkApiUrl = clerkApiUrl;
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
        
        // Get the user first to clear relationships
        User user = repo.findByClerkId(clerkId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Clear bidirectional friendships
        user.getFriends().clear();
        repo.save(user);
        
        // Delete all friend requests involving this user
        requestrepo.deleteAllByUserClerkId(clerkId);
        
        // Delete user's activities from activity-service
        try {
            String url = activityServiceUrl + "/api/v1/activities/user/" + clerkId;
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("X-Service-API-Key", serviceApiKey);
            org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);
            restTemplate.exchange(url, org.springframework.http.HttpMethod.DELETE, entity, Void.class);
        } catch (Exception e) {
            // Log but continue - activities service might be down
            System.err.println("Failed to delete activities for user " + clerkId + ": " + e.getMessage());
        }
        
        // Finally delete the user
        repo.deleteByClerkId(clerkId);
        
        // Delete user from Clerk if secret key is configured
        if (clerkSecretKey != null && !clerkSecretKey.isEmpty()) {
            try {
                String url = clerkApiUrl + "/users/" + clerkId;
                org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
                headers.set("Authorization", "Bearer " + clerkSecretKey);
                org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);
                restTemplate.exchange(url, org.springframework.http.HttpMethod.DELETE, entity, Void.class);
            } catch (Exception e) {
                // Log but don't fail - user already deleted from our DB
                System.err.println("Failed to delete user from Clerk " + clerkId + ": " + e.getMessage());
            }
        }
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

