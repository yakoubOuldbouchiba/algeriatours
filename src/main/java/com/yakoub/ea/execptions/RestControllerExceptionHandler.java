package com.yakoub.ea.execptions;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public RestError businessException(BusinessException ex){
        return RestError.builder()
                .type(HttpStatus.BAD_REQUEST.toString())
                .message(ex.getMessage())
                .originMessage(ex.getMessage())
                .reason(Optional
                        .ofNullable(ex.getCause())
                        .map(Throwable::toString)
                        .orElse(null))
                .advice("Veuillez consulter l'administrateur")
                .build();
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public RestError noSuchElementException(NoSuchElementException ex){
        return RestError.builder()
                .type(HttpStatus.NOT_FOUND.toString())
                .message("Données  introuvable")
                .originMessage(ex.getMessage())
                .reason(Optional
                        .ofNullable(ex.getCause())
                        .map(Throwable::toString)
                        .orElse(null))
                .advice("Veuillez consulter l'administrateur")
                .build();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public RestError dataIntegrityViolationException(DataIntegrityViolationException ex){
        return RestError.builder()
                .type(HttpStatus.BAD_REQUEST.toString())
                .message("vous avez violé une contrainte d'integrité")
                .originMessage(ex.getMessage())
                .reason(Optional
                        .ofNullable(ex.getCause())
                        .map(Throwable::toString)
                        .orElse(null))
                .advice("Veuillez corriger les données")
                .build();
    }
    
    
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public RestError constraintViolationException(ConstraintViolationException ex){
        return RestError.builder()
                .type(HttpStatus.BAD_REQUEST.toString())
                .message("vous avez violé une contrainte de validation")
                .originMessage(ex.getMessage())
                .reason(Optional
                        .of(ex)
                        .map(Throwable::toString)
                        .orElse(null))
                .advice("Veuillez corriger  les données")
                .build();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestError methodArgumentNotValidException(MethodArgumentNotValidException ex){
        return RestError.builder()
                .type(HttpStatus.BAD_REQUEST.toString())
                .message("Les données sont invalides")
                .originMessage(ex.getMessage())
                .reason(Optional
                        .ofNullable(ex.getCause())
                        .map(Throwable::toString)
                        .orElse(null))
                .advice("Veuillez corriger  les données")
                .build();
    }
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(AuthenticationException.class)
//    public RestError userJwtAuthenticationException(AuthenticationException ex){
//        return RestError.builder()
//                .type(HttpStatus.UNAUTHORIZED.toString())
//                .message("Vous n'êtes pas authentifié, Veuillez s'authentifier")
//                .originMessage(ex.getMessage())
//                .reason(Optional
//                        .ofNullable(ex.getCause())
//                        .map(Throwable::toString)
//                        .orElse(null))
//                .advice("Veuillez s'authentifier")
//                .build();
//    }
//
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ExceptionHandler(AccessDeniedException.class)
//    public RestError userJwtAccessDenied(AccessDeniedException ex){
//        return RestError.builder()
//                .type(HttpStatus.FORBIDDEN.toString())
//                .message("Vous n'avez pas les droits d'accès à cette fonctionnalité")
//                .originMessage(ex.getMessage())
//                .reason(Optional
//                        .ofNullable(ex.getCause())
//                        .map(Throwable::toString)
//                        .orElse(null))
//                .advice("Veuillez consulter l'administrateur")
//                .build();
//    }
//
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public RestError globalException(Exception ex){
        ex.printStackTrace();
        return RestError.builder()
                .type(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message("Une erreur s'est produite, veuillez consulter l'administrateur")
                .originMessage(ex.getMessage())
                .reason(Optional
                        .ofNullable(ex.getCause())
                        .map(Throwable::toString)
                        .orElse(null))
                .advice("Veuillez consulter l'administrateur")
                .build();
    }
}
