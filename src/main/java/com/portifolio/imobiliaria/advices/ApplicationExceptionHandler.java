//package com.portifolio.imobiliaria.advices;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.dao.DataAccessException;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import com.portifolio.imobiliaria.exception.ResourceNotFoundExeception;
//
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.validation.ConstraintViolationException;
//
//@RestControllerAdvice
//public class ApplicationExceptionHandler {
//
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(ResourceNotFoundExeception.class)
//    public ErroHandle handleResourceNotFound(ResourceNotFoundExeception exception) {
//        Map<String, String> errorMap = new HashMap<>();
//        errorMap.put("message", exception.getMessage());
//
//        return new ErroHandle(errorMap);
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ErroHandle handleInvalidArguments(MethodArgumentNotValidException exception) {
//        Map<String, String> errorMap = new HashMap<>();
//        exception.getBindingResult().getFieldErrors().forEach(
//                fieldError -> {
//                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
//                });
//
//        return new ErroHandle(errorMap);
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ErroHandle handleErrorConstraintViolationException(ConstraintViolationException exception) {
//        Map<String, String> errorMap = new HashMap<>();
//        errorMap.put("message", exception.getMessage());
//
//        return new ErroHandle(errorMap);
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ErroHandle handleErrorDataIntegrityViolationException(DataIntegrityViolationException exception) {
//        Map<String, String> erroMap = new HashMap<>();
//        erroMap.put("message", exception.getMessage());
//
//        return new ErroHandle(erroMap);
//    }
//
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ErroHandle handleErrorEntityNotFoundException(EntityNotFoundException exception) {
//        Map<String, String> erroMap = new HashMap<>();
//        erroMap.put("message", exception.getMessage());
//
//        return new ErroHandle(erroMap);
//    }
//
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(DataAccessException.class)
//    public ErroHandle handleErrorDataAccessException(DataAccessException exception) {
//        Map<String, String> erroMap = new HashMap<>();
//        erroMap.put("message", exception.getMessage());
//
//        return new ErroHandle(erroMap);
//    }
//
//}
