package com.gvendas.gestaovendas.excecao;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GestaoVendasTratamentoExcecao extends ResponseEntityExceptionHandler {

    public static final String CONSTANT_VALIDATION_NOT_NOT_BLANK = "NotBlank";
    public static final String CONSTANT_VALIDATION_NOT_NOT_NULL = "NotNull";
    public static final String CONSTANT_VALIDATION_LENGTH = "Length";

    public static final String CONSTANT_VALIDATION_PARTTERN = "Pattern";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Erro> erros = gerarListaDeErrors(ex.getBindingResult());

        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        String msgUsuario = "Recurso não encontrado.";
        String msgDesenvolvedor = ex.toString();
        List<Erro> erros = Arrays.asList(new Erro(msgUsuario, msgDesenvolvedor));
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String msgUsuario = "Recurso não encontrado.";
        String msgDesenvolvedor = ex.toString();
        List<Erro> erros = Arrays.asList(new Erro(msgUsuario, msgDesenvolvedor));
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Object> handleRegraNegocioException(RegraNegocioException ex, WebRequest request) {
        String msgUsuario = ex.getMessage();
        String msgDesenvolvedor = ex.getMessage();
        List<Erro> erros = Arrays.asList(new Erro(msgUsuario, msgDesenvolvedor));
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private List<Erro> gerarListaDeErrors(BindingResult bindingResult) {
        List<Erro> erros = new ArrayList<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            String msgUsuario = tratarMensagemDeErroParaUsuario(fieldError);
            String msgDesenvolvedor = fieldError.toString();
            erros.add(new Erro(msgUsuario, msgDesenvolvedor));
        });
        return erros;
    }

    private String tratarMensagemDeErroParaUsuario(FieldError fieldError) {
        if(fieldError.getCode().equals(CONSTANT_VALIDATION_NOT_NOT_BLANK)) {
            return fieldError.getDefaultMessage().concat(" é obrigatório");
        }

        if(fieldError.getCode().equals(CONSTANT_VALIDATION_NOT_NOT_NULL)) {
            return fieldError.getDefaultMessage().concat(" o campo não pode ser nulo");
        }

        if(fieldError.getCode().equals(CONSTANT_VALIDATION_LENGTH)) {
            return fieldError.getDefaultMessage().concat(String.format(" deve ter entre %s e %s caracteres",
                    fieldError.getArguments()[2],
                        fieldError.getArguments()[1]));
        }

        if(fieldError.getCode().equals(CONSTANT_VALIDATION_PARTTERN)) {
            return fieldError.getDefaultMessage().concat(" formato inválido");
        }
        return fieldError.toString();
    }
}
