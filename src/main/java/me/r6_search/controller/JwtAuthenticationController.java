package me.r6_search.controller;

import com.sun.mail.iap.Response;
import lombok.RequiredArgsConstructor;
import me.r6_search.config.UserProfileAnnotation;
import me.r6_search.dto.JwtRequestDto;
import me.r6_search.dto.JwtResponseDto;
import me.r6_search.dto.PasswordChangeRequestDto;
import me.r6_search.dto.SignUpRequestDto;
import me.r6_search.exception.user.UserAuthenticationException;
import me.r6_search.model.userprofile.UserProfile;
import me.r6_search.security.JwtTokenProvider;
import me.r6_search.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.Files;

@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/c")
@RestController
public class JwtAuthenticationController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserProfileService userProfileService;

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody JwtRequestDto authenticationRequest) {
        try {
            String username = authenticationRequest.getUsername();
            String password = authenticationRequest.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            String token = jwtTokenProvider.generateToken(username);
            return ResponseEntity.ok(new JwtResponseDto(token));
        } catch (AuthenticationException e) {
            throw new UserAuthenticationException("Invalid id or pw");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        userProfileService.saveUser(signUpRequestDto);
        return new ResponseEntity<>("{\"status\": \"good\"}", HttpStatus.OK);
    }

    @GetMapping("/authenticate")
    public ResponseEntity authenticateEmail(@RequestParam String username,
                                            @RequestParam String code) {
        boolean isAuthenticated = userProfileService.authenticateEmail(username, code);
        if(isAuthenticated) {
            return new ResponseEntity("{\"message\": \"Email Authentication Success\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity("{\"message\": \"Email Authentication fail, Check email again\"}", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/change-pw")
    public ResponseEntity changePassword(@RequestBody PasswordChangeRequestDto requestDto,
                                         @UserProfileAnnotation UserProfile userProfile) {
        if(!requestDto.getPassword().equals(requestDto.getPassword1())) {
            return new ResponseEntity("{\"message\": \"Password is not same\"}", HttpStatus.BAD_REQUEST);
        }

        userProfileService.changePassword(requestDto.getPassword(), userProfile);
        return new ResponseEntity("{\"message\": \"Password changed\"}", HttpStatus.OK);
    }
}

