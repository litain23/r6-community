package me.r6_search.config;

import me.r6_search.dto.ErrorResponseDto;
import me.r6_search.exception.board.*;
import me.r6_search.exception.user.UserAuthenticationException;
import me.r6_search.exception.user.UserSignUpValidateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorResponseDto> handleBadCredentialsException(BadCredentialsException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatus(401);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected  ResponseEntity<ErrorResponseDto> handleSignupException(IllegalArgumentException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(e.getMessage());
        errorResponseDto.setStatus(400);

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserSignUpValidateException.class)
    protected ResponseEntity<ErrorResponseDto> handleUserValidateException(UserSignUpValidateException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage(), 400);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAuthenticationException.class)
    protected ResponseEntity<ErrorResponseDto> handlerUserAuthenticationException(UserAuthenticationException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage(), 400);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handlerCommentNotFoundException(CommentNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage(), 400);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentIllegalModifyException.class)
    protected ResponseEntity<ErrorResponseDto> handlerCommentIllegalModifyException(CommentIllegalModifyException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage(), 400);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostNotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handlerPostNotFoundException(PostNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage(), 400);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostIllegalModifyException.class)
    protected ResponseEntity<ErrorResponseDto> handlerPostIllegalModifyException(PostIllegalModifyException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage(), 400);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostIllegalFileExtensionException.class)
    protected ResponseEntity<ErrorResponseDto> handlerPostIllegalFileExtensionException(PostIllegalFileExtensionException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage(), 400);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BoardException.class)
    protected ResponseEntity<ErrorResponseDto> handlerBoardException(BoardException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage(), 400);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
}

