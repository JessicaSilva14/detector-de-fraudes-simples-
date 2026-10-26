package br.com.deteccaofraudes.regra;

import br.com.deteccaofraudes.modelo.Transacao;
import br.com.deteccaofraudes.util.Moeda;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Marca transações de valor relevante feitas de madrugada.
 * O intervalo precisa estar dentro do mesmo dia (ex.: 00:00 até 05:00).
 */
public class HorarioIncomumRegra implements RegraFraude {
    private final LocalTime inicio;
    private final LocalTime fim;
    private final BigDecimal valorMinimo;

    public HorarioIncomumRegra(LocalTime inicio, LocalTime fim, BigDecimal valorMinimo) {
        this.inicio = inicio;
        this.fim = fim;
        this.valorMinimo = valorMinimo;
    }

    @Override
    public String nome() {
        return "Horário incomum";
    }

    @Override
    public Optional<String> avaliar(Transacao atual, List<Transacao> historico) {
        LocalTime hora = atual.dataHora().toLocalTime();
        boolean madrugada = !hora.isBefore(inicio) && hora.isBefore(fim);

        if (madrugada && atual.valor().compareTo(valorMinimo) >= 0) {
            return Optional.of(String.format("Transação de %s às %s (entre %s e %s)",
                    Moeda.formatar(atual.valor()), hora, inicio, fim));
        }
        return Optional.empty();
    }
}
