package br.com.motiva.exception;

/** Violação de regra de negócio. Vira HTTP 400 no GlobalExceptionHandler. */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
