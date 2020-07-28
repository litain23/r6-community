package me.r6_search.service;

import lombok.RequiredArgsConstructor;
import me.r6_search.model.userrole.UserRole;
import me.r6_search.dto.SignUpRequestDto;
import me.r6_search.exception.user.UserProfileNotFound;
import me.r6_search.model.userprofile.UserProfile;
import me.r6_search.model.userprofile.UserProfileRepository;
import me.r6_search.utils.RandomStringGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final MailService mailService;

    @Transactional
    public Long saveUser(SignUpRequestDto requestDto) throws IllegalArgumentException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator();

        String emailCode = randomStringGenerator.generateRandomString(18);
        UserProfile newUser = UserProfile.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .emailCode(emailCode)
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        UserRole userRole = new UserRole();
        userRole.setRoleName("USER");
        newUser.setRoles(Arrays.asList(userRole));

        mailService.sendEmailAuthenticationCode(requestDto.getUsername(), requestDto.getEmail(), emailCode);

        return userProfileRepository.save(newUser).getId();
    }

    @Transactional
    public boolean authenticateEmail(String username, String code) {
        UserProfile userProfile = userProfileRepository.findByUsernameAndEmailAuthenticateCode(username, code);
        if(userProfile != null) {
            userProfile.setEmailAuthenticated(true);
            userProfile.getRoles().add(new UserRole("AUTHENTICATED_USER"));
            return true;
        }
        return false;
    }

    @Transactional
    public void changePassword(String password, UserProfile userProfile) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userProfile.changePassword(passwordEncoder.encode(password));
    }

    public UserProfile getUserProfile(String username) {
        return userProfileRepository.findByUsername(username).orElseThrow(() -> new UserProfileNotFound("유저를 찾을 수 없습니다."));
    }


}
