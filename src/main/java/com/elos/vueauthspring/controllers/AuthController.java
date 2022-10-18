package com.elos.vueauthspring.controllers;

import com.elos.vueauthspring.models.ERole;
import com.elos.vueauthspring.models.Role;
import com.elos.vueauthspring.models.User;
import com.elos.vueauthspring.payload.request.LoginRequest;
import com.elos.vueauthspring.payload.response.UserResponse;
import com.elos.vueauthspring.payload.response.MessageResponse;
import com.elos.vueauthspring.repository.RoleRepository;
import com.elos.vueauthspring.repository.UserRepository;
import com.elos.vueauthspring.security.services.UserDetailsImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws IOException {
        String token = loginRequest.getToken().replace("https://api.vime.world/web/token/", "");
        JSONObject jsonObject = readJsonFromUrl("https://api.vimeworld.com/misc/token/" + token);
        System.out.println(loginRequest.getToken());
        if (!jsonObject.getBoolean("valid")) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Token is bad!"));
        }

        JSONObject owner = jsonObject.getJSONObject("owner");
        Long id = owner.getLong("id");
        String username = owner.getString("username");
        if (!userRepository.existsByUsername(username)) {
            User user;
            if (userRepository.existsById(id)) {
                user = userRepository.findById(id).orElseThrow();
                user.setUsername(username);
            } else {
                user = new User(username, id);
            }
            Set<String> strRoles = loginRequest.getRole();
            Set<Role> roles = new HashSet<>();
            if (strRoles == null) {
                System.out.println("YES");
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);
                            break;
                        case "mod":
                            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(modRole);
                            break;
                        default:
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                    }
                });
            }
            user.setEnabled(true);
            user.setRoles(roles);
            userRepository.save(user);
        }
        User user = userRepository.findById(id).orElseThrow();
        UserDetailsImpl userDetails1 = UserDetailsImpl.build(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails1, null, userDetails1.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println(userDetails1.getAuthorities());
        System.out.println(userDetails.getAuthorities());
        List<String> rolesUser = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new UserResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                rolesUser));
    }

//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws IOException {
//        // Create new user's account
//
//        JSONObject jsonObject = readJsonFromUrl("https://api.vimeworld.com/misc/token/" + signUpRequest.getToken());
//        System.out.println(signUpRequest.getToken());
//        if (!jsonObject.getBoolean("valid")) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Token is bad!"));
//        }
//
//        JSONObject owner = jsonObject.getJSONObject("owner");
//        Long id = owner.getLong("id");
//        String username = owner.getString("username");
//
//        User user = new User(username, id);
//
//        Set<String> strRoles = signUpRequest.getRole();
//        Set<Role> roles = new HashSet<>();
//        if (strRoles == null) {
//            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//                        break;
//                    case "mod":
//                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(modRole);
//                        break;
//                    default:
//                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(userRole);
//                }
//            });
//        }
//        user.setRoles(roles);
//        userRepository.save(user);
//        return ResponseEntity.ok(user);
//
//    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
