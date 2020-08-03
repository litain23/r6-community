package me.r6_search.service.validate;

import lombok.RequiredArgsConstructor;
import me.r6_search.dto.SignUpRequestDto;
import me.r6_search.exception.user.UserSignUpValidateException;
import me.r6_search.model.userprofile.UserProfileRepository;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@RequiredArgsConstructor
@Component
public class SignUpValidator implements ConstraintValidator<SignUpValid, Object> {
    private final UserProfileRepository userProfileRepository;

    @Override
    public void initialize(SignUpValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        SignUpRequestDto dto = (SignUpRequestDto)value;
//        if(dto.getUsername().matches('/')) {
//
//        } else if(dto.getPassword().matches('/')) {
//
//        } else
        if(userProfileRepository.findByUsername(dto.getUsername()).isPresent()) {
            context.buildConstraintViolationWithTemplate("이미등록된아이디입니다.")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        } else if(userProfileRepository.findByEmail(dto.getEmail()).isPresent()) {
            return false;
        }
        return true;
    }

}
