package br.com.motiva.exception;

import br.com.motiva.dto.ErroResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tratamento global de erros (@ControllerAdvice).
 *
 * Resolve a pendência da Sprint 3 ("SQLException não tratada, usuário não recebe
 * mensagem orientativa"): toda falha vira um JSON padronizado, com status HTTP
 * correto e uma mensagem que diz ao cliente o que fazer.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontrado(RecursoNaoEncontradoException ex, HttpServletRequest req) {
        return montar(HttpStatus.NOT_FOUND, ex.getMessage(), req, Map.of());
    }

    @ExceptionHandler({RegraNegocioException.class, IllegalArgumentException.class})
    public ResponseEntity<ErroResponse> regraNegocio(RuntimeException ex, HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, ex.getMessage(), req, Map.of());
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResponse> conflito(ConflitoException ex, HttpServletRequest req) {
        return montar(HttpStatus.CONFLICT, ex.getMessage(), req, Map.of());
    }

    /** Falhas do Bean Validation (@Valid): devolve qual campo falhou e por quê. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            campos.merge(erro.getField(), erro.getDefaultMessage(), (a, b) -> a + " " + b);
        }
        return montar(HttpStatus.BAD_REQUEST, "Dados inválidos. Corrija os campos indicados.", req, campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> jsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST,
                "Corpo da requisição ausente ou mal formatado. Verifique o JSON e os tipos dos campos "
                        + "(datas no formato 2026-10-21T14:30:00).", req, Map.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tipoInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String msg = "Valor inválido '" + ex.getValue() + "' para o parâmetro '" + ex.getName()
                + "'. Para datas use o formato AAAA-MM-DD.";
        return montar(HttpStatus.BAD_REQUEST, msg, req, Map.of());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> parametroAusente(MissingServletRequestParameterException ex,
                                                         HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST,
                "O parâmetro obrigatório '" + ex.getParameterName() + "' não foi informado.", req, Map.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponse> rotaInexistente(NoResourceFoundException ex, HttpServletRequest req) {
        return montar(HttpStatus.NOT_FOUND, "Rota não encontrada. Confira o endereço no README.", req, Map.of());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponse> metodoNaoSuportado(HttpRequestMethodNotSupportedException ex,
                                                           HttpServletRequest req) {
        return montar(HttpStatus.METHOD_NOT_ALLOWED,
                "Método " + ex.getMethod() + " não é suportado nesta rota.", req, Map.of());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErroResponse> mediaNaoSuportada(HttpMediaTypeNotSupportedException ex,
                                                          HttpServletRequest req) {
        return montar(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Envie o corpo com o cabeçalho Content-Type: application/json.", req, Map.of());
    }

    /** Restrição do banco violada (FK, NOT NULL, CHECK...). */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> integridade(DataIntegrityViolationException ex, HttpServletRequest req) {
        log.warn("Violação de integridade em {}: {}", req.getRequestURI(), ex.getMostSpecificCause().getMessage());
        return montar(HttpStatus.CONFLICT,
                "A operação viola uma restrição do banco de dados (ex.: registro vinculado a outro).",
                req, Map.of());
    }

    /** Equivalente à SQLException da Sprint 3: banco fora do ar, credencial errada etc. */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErroResponse> bancoIndisponivel(DataAccessException ex, HttpServletRequest req) {
        log.error("Erro de acesso ao banco em {}", req.getRequestURI(), ex);
        return montar(HttpStatus.SERVICE_UNAVAILABLE,
                "Não foi possível acessar o banco de dados no momento. Tente novamente em instantes.",
                req, Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> inesperado(Exception ex, HttpServletRequest req) {
        log.error("Erro inesperado em {}", req.getRequestURI(), ex);
        return montar(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno inesperado. Se persistir, contate o suporte.", req, Map.of());
    }

    private ResponseEntity<ErroResponse> montar(HttpStatus status, String mensagem,
                                                HttpServletRequest req, Map<String, String> campos) {
        ErroResponse corpo = new ErroResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                req.getRequestURI(),
                campos
        );
        return ResponseEntity.status(status).body(corpo);
    }
}
