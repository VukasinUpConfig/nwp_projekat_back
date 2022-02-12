package com.example.RafCloud.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.example.RafCloud.user.User;
import com.example.RafCloud.user.UserService;
import com.example.RafCloud.utils.JwtUtils;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private JwtUtils jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception   e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
        
        User user = userService.findUserByUsername(loginRequest.getUsername());
        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(user.getId(), user.getUsername())));
    }

}
