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
        String idRegex= "^[a-zA-Z0-9][a-zA-Z0-9._-]{3,}$";
        String pwRegex= "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$) .{8,}$";
        if(!dto.getUsername().matches(idRegex)) {
            context.buildConstraintViolationWithTemplate("아이디는 4글자이상, 영문, 숫자, '.', '_', '-' 만 가능합니다.")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        } else if(!dto.getPassword().matches(pwRegex)) {
            context.buildConstraintViolationWithTemplate("비밀번호는 숫자와 영문자를 합쳐서 8글자 이상이어야 됩니다.")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        } else if(userProfileRepository.findByUsername(dto.getUsername()).isPresent()) {
            context.buildConstraintViolationWithTemplate("이미 등록된 아이디입니다.")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        } else if(userProfileRepository.findByEmail(dto.getEmail()).isPresent()) {
            context.buildConstraintViolationWithTemplate("이미 등록된 이메일입니다.")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        }
        return true;
    }

}
