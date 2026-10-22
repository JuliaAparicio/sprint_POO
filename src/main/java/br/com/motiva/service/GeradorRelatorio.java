package br.com.motiva.service;

import br.com.motiva.dto.ItemRelatorioResponse;
import br.com.motiva.dto.RelatorioGeradoResponse;
import br.com.motiva.dto.RelatorioResponse;
import br.com.motiva.exception.RecursoNaoEncontradoException;
import br.com.motiva.exception.RegraNegocioException;
import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.model.NivelPrioridade;
import br.com.motiva.model.RelatorioPrioridade;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.RelatorioPrioridadeRepository;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Evolução do GeradorRelatorio da Sprint 3: continua recebendo as dependências
 * pelo construtor, mas agora o Spring injeta os Repositories e o resultado
 * volta como JSON em vez de System.out.println.
 */
@Service
public class GeradorRelatorio {

    private final RelatorioPrioridadeRepository relatorioRepository;
    private final TrechoRodoviaRepository trechoRepository;
    private final IntervencaoService intervencaoService;

    public GeradorRelatorio(RelatorioPrioridadeRepository relatorioRepository,
                            TrechoRodoviaRepository trechoRepository,
                            IntervencaoService intervencaoService) {
        this.relatorioRepository = relatorioRepository;
        this.trechoRepository = trechoRepository;
        this.intervencaoService = intervencaoService;
    }

    @Transactional
    public RelatorioGeradoResponse gerarRelatorio() {
        List<TrechoRodovia> trechos = trechoRepository.findAllByOrderByNivelVegetacaoDesc();
        if (trechos.isEmpty()) {
            throw new RegraNegocioException("Não há trechos cadastrados. Cadastre ao menos um trecho "
                    + "(POST /api/trechos) antes de gerar o relatório.");
        }

        Map<NivelPrioridade, Integer> contagem = new EnumMap<>(NivelPrioridade.class);
        List<ItemRelatorioResponse> itens = new ArrayList<>();

        for (TrechoRodovia trecho : trechos) {
            NivelPrioridade nivel = intervencaoService.classificar(trecho);
            IntervencaoOperacional recomendada = intervencaoService.decidirIntervencao(trecho);

            contagem.merge(nivel, 1, Integer::sum);
            itens.add(new ItemRelatorioResponse(
                    trecho.getId(),
                    trecho.getNomeRodovia(),
                    trecho.getQuilometroInicial(),
                    trecho.getQuilometroFinal(),
                    trecho.getNivelVegetacao(),
                    nivel,
                    recomendada.getDescricao()
            ));
        }

        int qtUrgente = contagem.getOrDefault(NivelPrioridade.URGENTE, 0);
        int qtCritico = contagem.getOrDefault(NivelPrioridade.CRITICO, 0);
        int qtAtencao = contagem.getOrDefault(NivelPrioridade.ATENCAO, 0);
        int qtNormal = contagem.getOrDefault(NivelPrioridade.NORMAL, 0);

        String resumo = String.format("Urgente: %d | Crítico: %d | Atenção: %d | Normal: %d",
                qtUrgente, qtCritico, qtAtencao, qtNormal);

        RelatorioPrioridade salvo = relatorioRepository.save(
                new RelatorioPrioridade(qtUrgente, qtCritico, qtAtencao, qtNormal, resumo));

        return new RelatorioGeradoResponse(RelatorioResponse.from(salvo), itens);
    }

    @Transactional(readOnly = true)
    public List<RelatorioResponse> listarHistorico() {
        return relatorioRepository.findAllByOrderByDataGeracaoDesc().stream()
                .map(RelatorioResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public RelatorioResponse buscarPorId(Long id) {
        return relatorioRepository.findById(id)
                .map(RelatorioResponse::from)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório", id));
    }

    /** Período inclusivo: de 00:00 do dia inicial até 23:59:59 do dia final. */
    @Transactional(readOnly = true)
    public List<RelatorioResponse> consultarPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio.isAfter(fim)) {
            throw new RegraNegocioException("A data de início (" + inicio
                    + ") não pode ser posterior à data de fim (" + fim + ").");
        }
        return relatorioRepository
                .findByDataGeracaoBetweenOrderByDataGeracaoDesc(inicio.atStartOfDay(), fim.atTime(LocalTime.MAX))
                .stream().map(RelatorioResponse::from).toList();
    }
}
