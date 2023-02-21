package com.project.login.controllers;

import com.project.login.controllers.model.Role;
import com.project.login.controllers.model.User;
import com.project.login.controllers.request.LoginRequest;
import com.project.login.controllers.request.RegisterRequest;
import com.project.login.controllers.response.LoginResponse;
import com.project.login.repository.RoleRepository;
import com.project.login.repository.UserRepository;
import com.project.login.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @GetMapping("/message")
    public String test(){
        return "Spring boot";
    }

    @PostMapping(value="/user/login",produces = "application/json",consumes = "application/json")
    public LoginResponse loginv1(@RequestBody LoginRequest request, HttpServletResponse httpServletResponse) throws IOException {
      log.info("login api");
      LoginResponse loginResponse=new LoginResponse();
      try{
          Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName()
                  ,request.getPassword()));
          if(authentication.isAuthenticated()){
              SecurityContextHolder.getContext().setAuthentication(authentication);
              loginResponse.setResponseCode("SUCCESS");
              loginResponse.setMessage(request.getUserName()+" Authenticate Successfully");
              System.out.println("login api:"+ request);
          }
      }
      catch (AuthenticationException exception){
          loginResponse.setResponseCode("FAILURE");
          loginResponse.setMessage(request.getUserName()+" Authentication Failed");
          httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
      }
      return loginResponse;
    }
@PostMapping(value = "/user/register",produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> registerResponse(@RequestBody RegisterRequest registerRequest){
        log.info("Register api");
    // add check if username exists in Database
    if(userRepository.existsByusername(registerRequest.getUsername())){
        return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
    }

    // create object of the user
    User user = new User();
    
    user.setUsername(registerRequest.getUsername());
    user.setFirstname(registerRequest.getFirstName());
    user.setLastname(registerRequest.getLastName());
    user.setAge(registerRequest.getAge());
    user.setGender(registerRequest.getGender());
    user.setCountry(registerRequest.getCountry());
    user.setEmail(registerRequest.getEmail());
    user.setPhoneNumber(registerRequest.getPhoneNumber());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    System.out.println("username : "+registerRequest.getUsername()+" Password: "+registerRequest.getPassword());
    Role roles = roleRepository.findByRoleName("ROLE_ADMIN").get();
    user.setRoles(Collections.singleton(roles));

    userRepository.save(user);

    return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
    //end points for forgotpassword
    @PostMapping(value = "/user/forgotpassword",produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> handleForgotPassword(@RequestParam("userName") String userName,@RequestParam("newPassword") String newpassword) {
        
            if (userRepository.existsByusername(userName)) {
            return ResponseEntity.badRequest().body("User not found");
        }
       // User user = userRepository.findByUsername(username);
            
       userService.updateUserPassword(userName,newpassword);

        return ResponseEntity.ok("Password reset done successfully");
}
}
