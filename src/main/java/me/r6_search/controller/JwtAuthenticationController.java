package me.r6_search.controller;

import com.sun.mail.iap.Response;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import me.r6_search.config.UserProfileAnnotation;
import me.r6_search.dto.*;
import me.r6_search.exception.user.UserAuthenticationException;
import me.r6_search.exception.user.UserSignUpValidateException;
import me.r6_search.model.userprofile.UserProfile;
import me.r6_search.security.JwtTokenProvider;
import me.r6_search.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.file.Files;

@RequiredArgsConstructor
@RequestMapping("/api/c")
@RestController
public class JwtAuthenticationController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserProfileService userProfileService;

    @PostMapping("/refresh")
    public ResponseEntity refreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = "wrong_token";
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                String name = cookie.getName();
                if(name.equals("refresh-token")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        try {
            jwtTokenProvider.validateRefreshToken(refreshToken);
            String username = jwtTokenProvider.getUsernameUsingRefreshToken(refreshToken);
            String token = jwtTokenProvider.generateToken(username);
            return ResponseEntity.ok(new JwtResponseDto(token));
        } catch (ExpiredJwtException e) {
            return new ResponseEntity("{\"message\": \"리프레쉬토큰이 만료되었습니다.\"}", HttpStatus.FORBIDDEN);
        } catch (JwtException e) {
            return new ResponseEntity("{\"message\": \"인증에 실패하였습니다.\"}", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody JwtRequestDto authenticationRequest,
                                 HttpServletResponse response) {
        try {
            String username = authenticationRequest.getUsername();
            String password = authenticationRequest.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            String token = jwtTokenProvider.generateToken(username);
            String refreshToken = jwtTokenProvider.generateRefreshToken(username);

            Cookie cookie = new Cookie("refresh-token", refreshToken);
            cookie.setHttpOnly(true);
//            cookie.setSecure(true);
            response.addCookie(cookie);
            return ResponseEntity.ok(new JwtResponseDto(token));
        } catch (AuthenticationException e) {
            throw new UserAuthenticationException("아이디 또는 비밀번호가 일치하지 않습니다");
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

