package com.aqtilink.user_service.service;

import com.aqtilink.user_service.dto.FriendDTO;
import com.aqtilink.user_service.model.User;
import com.aqtilink.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


import java.util.UUID;
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

    public User get(UUID id) {
        User user = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user;
    }
    public List<User> getAll() {
        return repo.findAll();
    }
    public User update(UUID id, User updated){
        User exists = get(id);
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
    public void delete(UUID id){
        if(!repo.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        repo.deleteById(id);
    }

    public String getEmail(UUID id){
        return get(id).getEmail();
    }
    public List<FriendDTO> getFriends(UUID id){
        repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return requestrepo.findFriendsOfUser(id).stream().map(u -> new FriendDTO(u.getId(), u.getFirstName(), u.getLastName())).toList();
    }

}

