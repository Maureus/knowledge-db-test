package com.example.knowledgedbcore.services.userservice;

import com.example.knowledgedbcore.dao.authdao.AuthTokenDao;
import com.example.knowledgedbcore.models.AuthToken;
import com.example.knowledgedbcore.models.Role;
import com.example.knowledgedbcore.models.User;
import com.example.knowledgedbcore.models.UserRole;
import com.example.knowledgedbcore.payload.request.LoginRequest;
import com.example.knowledgedbcore.payload.request.SignupRequest;
import com.example.knowledgedbcore.payload.response.JwtResponse;
import com.example.knowledgedbcore.payload.response.MessageResponse;
import com.example.knowledgedbcore.repo.RoleRepo;
import com.example.knowledgedbcore.repo.UserRepo;
import com.example.knowledgedbcore.security.jwt.JwtUtils;
import com.example.knowledgedbcore.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthTokenDao authTokenDao;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager,
                           UserRepo userRepo,
                           RoleRepo roleRepo,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils,
                           AuthTokenDao authTokenDao) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authTokenDao = authTokenDao;
    }

    @Override
    public User createUser(SignupRequest signUpRequest) throws RuntimeException {

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepo.findByName(UserRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepo.findByName(UserRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepo.findByName(UserRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        User userFromDb = userRepo.save(user);
        return userFromDb;
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        AuthToken authToken = new AuthToken();
        authToken.setJwtToken(jwt);
        AuthToken authTokenDB = authTokenDao.save(authToken);

        return new JwtResponse(
                authTokenDB.getId(),
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    @Override
    public ResponseEntity<?> logoutUser(String authToken) throws IllegalArgumentException {
        if (isAToken(authToken)) {
            String jwtToken = authToken.substring(7);
            boolean isValidated = jwtUtils.validateJwtToken(jwtToken);
            if (isValidated) {
                try {
                    AuthToken authTokenDb = authTokenDao.findByJwtToken(jwtToken)
                            .orElseThrow(() -> new IllegalArgumentException("Token not found"));
                    authTokenDao.delete(authTokenDb);
                    return ResponseEntity.status(HttpStatus.OK).build();
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Token is not Valid"));
                }
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Token is not Valid"));
    }

    private boolean isAToken(String authToken) {
        return StringUtils.hasText(authToken) && authToken.startsWith("Bearer ");
    }
}
