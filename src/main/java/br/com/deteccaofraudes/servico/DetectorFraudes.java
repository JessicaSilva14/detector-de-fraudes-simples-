package br.com.deteccaofraudes.servico;

import br.com.deteccaofraudes.modelo.Alerta;
import br.com.deteccaofraudes.modelo.Transacao;
import br.com.deteccaofraudes.regra.HorarioIncomumRegra;
import br.com.deteccaofraudes.regra.MuitasTransacoesRapidasRegra;
import br.com.deteccaofraudes.regra.RegraFraude;
import br.com.deteccaofraudes.regra.ValorAcimaDaMediaRegra;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetectorFraudes {
    private final List<RegraFraude> regras;

    public DetectorFraudes(List<RegraFraude> regras) {
        this.regras = List.copyOf(regras);
    }

    public static DetectorFraudes comRegrasPadrao() {
        return new DetectorFraudes(List.of(
                new ValorAcimaDaMediaRegra(new BigDecimal("5"), 3),
                new MuitasTransacoesRapidasRegra(3, Duration.ofMinutes(5)),
                new HorarioIncomumRegra(LocalTime.MIDNIGHT, LocalTime.of(5, 0), new BigDecimal("500"))
        ));
    }

    public List<Alerta> analisar(List<Transacao> transacoes) {
        // Ordena por data para que o "histórico" seja sempre o passado de cada transação.
        List<Transacao> ordenadas = new ArrayList<>(transacoes);
        ordenadas.sort(Comparator.comparing(Transacao::dataHora));

        Map<String, List<Transacao>> historicoPorCliente = new HashMap<>();
        List<Alerta> alertas = new ArrayList<>();

        for (Transacao transacao : ordenadas) {
            List<Transacao> historico =
                    historicoPorCliente.computeIfAbsent(transacao.clienteId(), c -> new ArrayList<>());
            List<Transacao> somenteLeitura = Collections.unmodifiableList(historico);

            for (RegraFraude regra : regras) {
                regra.avaliar(transacao, somenteLeitura)
                        .ifPresent(motivo -> alertas.add(new Alerta(transacao, regra.nome(), motivo)));
            }
            historico.add(transacao);
        }
        return alertas;
    }
}
