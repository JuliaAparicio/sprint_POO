package br.com.motiva.exception;

/** Operação que conflita com o estado atual dos dados (ex.: excluir algo vinculado). Vira HTTP 409. */
public class ConflitoException extends RuntimeException {

    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}
