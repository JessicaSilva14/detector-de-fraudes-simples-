package br.com.deteccaofraudes.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transacao(String id, String clienteId, BigDecimal valor, LocalDateTime dataHora) {

    public Transacao {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id é obrigatório");
        }
        if (clienteId == null || clienteId.isBlank()) {
            throw new IllegalArgumentException("clienteId é obrigatório");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("valor deve ser maior que zero");
        }
        if (dataHora == null) {
            throw new IllegalArgumentException("dataHora é obrigatória");
        }
    }
}
