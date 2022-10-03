package com.elos.vueauthspring.controllers;

import com.elos.vueauthspring.models.User;
import com.elos.vueauthspring.payload.response.MessageResponse;
import com.elos.vueauthspring.repository.UserRepository;
import com.elos.vueauthspring.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/user/")
public class LikeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("like/{id}")
    public ResponseEntity<?> like(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl currentUser) {
        User user;
        if (userRepository.existsById(id)) {
            user = userRepository.findById(id).orElseThrow();
        } else {
            user = new User();
            user.setId(id);
            user.setEnabled(false);
            userRepository.save(user);
        }
        if (Objects.equals(user.getId(), currentUser.getId())) {
            return ResponseEntity.ok(new MessageResponse("You can like yourself!"));
        }
        user.getLikes().add(userRepository.findById(currentUser.getId()).orElseThrow());
        System.out.println(user.getLikes().size());
        userRepository.save(user);
        Map<Object, Object> map = new HashMap<>();
        map.put("successLike", true);
        map.put("likes", user.getLikes());
        return ResponseEntity.ok(map);
    }

    @GetMapping("unlike/{user}")
    public ResponseEntity<?> unlike(@PathVariable User user, @AuthenticationPrincipal UserDetailsImpl currentUser) {
        user.getLikes().remove(userRepository.findById(currentUser.getId()).orElseThrow());
        userRepository.save(user);
        Map<Object, Object> map = new HashMap<>();
        map.put("successUnLike", true);
        map.put("likes", user.getLikes());
        return ResponseEntity.ok(map);
    }

    @GetMapping("likes/{id}")
    public Set likes(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).orElseThrow();
            System.out.println("error this is");
            return user.getLikes();

        } else {
            User user = new User();
            user.setId(id);
            user.setEnabled(false);
            userRepository.save(user);
            return user.getLikes();
        }
    }
}
