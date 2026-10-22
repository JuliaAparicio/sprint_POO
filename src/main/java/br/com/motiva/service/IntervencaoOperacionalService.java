package br.com.motiva.service;

import br.com.motiva.dto.IntervencaoAtualizacaoRequest;
import br.com.motiva.dto.IntervencaoRequest;
import br.com.motiva.dto.IntervencaoResponse;
import br.com.motiva.exception.RecursoNaoEncontradoException;
import br.com.motiva.exception.RegraNegocioException;
import br.com.motiva.model.EquipeManutencao;
import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.model.Pulverizacao;
import br.com.motiva.model.RocadaMecanizada;
import br.com.motiva.model.SemIntervencao;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.IntervencaoOperacionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class IntervencaoOperacionalService {

    private static final Set<String> TIPOS_VALIDOS =
            Set.of(SemIntervencao.CODIGO, Pulverizacao.CODIGO, RocadaMecanizada.CODIGO);

    private final IntervencaoOperacionalRepository repository;
    private final TrechoRodoviaService trechoService;
    private final EquipeManutencaoService equipeService;
    private final IntervencaoService motorDeRegras;

    public IntervencaoOperacionalService(IntervencaoOperacionalRepository repository,
                                         TrechoRodoviaService trechoService,
                                         EquipeManutencaoService equipeService,
                                         IntervencaoService motorDeRegras) {
        this.repository = repository;
        this.trechoService = trechoService;
        this.equipeService = equipeService;
        this.motorDeRegras = motorDeRegras;
    }

    @Transactional(readOnly = true)
    public List<IntervencaoResponse> listar(String tipo) {
        List<IntervencaoOperacional> lista;
        if (tipo != null && !tipo.isBlank()) {
            String codigo = tipo.trim().toUpperCase();
            if (!TIPOS_VALIDOS.contains(codigo)) {
                throw new RegraNegocioException("Tipo de intervenção inválido: '" + tipo
                        + "'. Use SEM_INTERVENCAO, PULVERIZACAO ou ROCADA_MECANIZADA.");
            }
            lista = repository.findByTipo(codigo);
        } else {
            lista = repository.findAll();
        }
        return lista.stream().map(IntervencaoResponse::from).toList();
    }

    /** Consulta de alocação: o que uma equipe tem/teve para executar. */
    @Transactional(readOnly = true)
    public List<IntervencaoResponse> listarPorEquipe(Long equipeId) {
        equipeService.buscarEntidade(equipeId); // 404 se a equipe não existir
        return repository.findByEquipeId(equipeId).stream().map(IntervencaoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<IntervencaoResponse> listarPorTrecho(Long trechoId) {
        trechoService.buscarEntidade(trechoId); // 404 se o trecho não existir
        return repository.findByTrechoId(trechoId).stream().map(IntervencaoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public IntervencaoResponse buscarPorId(Long id) {
        return IntervencaoResponse.from(buscarEntidade(id));
    }

    /** O motor de regras decide QUAL intervenção (subclasse) será registrada. */
    @Transactional
    public IntervencaoResponse criar(IntervencaoRequest req) {
        TrechoRodovia trecho = trechoService.buscarEntidade(req.trechoId());
        EquipeManutencao equipe = equipeService.buscarEntidade(req.equipeId());

        IntervencaoOperacional intervencao = motorDeRegras.decidirIntervencao(trecho);
        LocalDateTime data = req.dataExecucao() != null ? req.dataExecucao() : LocalDateTime.now();
        intervencao.registrar(trecho, equipe, data, limpar(req.observacao()));

        return IntervencaoResponse.from(repository.save(intervencao));
    }

    @Transactional
    public IntervencaoResponse atualizar(Long id, IntervencaoAtualizacaoRequest req) {
        IntervencaoOperacional intervencao = buscarEntidade(id);
        EquipeManutencao equipe = equipeService.buscarEntidade(req.equipeId());
        intervencao.atualizarExecucao(equipe, req.dataExecucao(), limpar(req.observacao()));
        return IntervencaoResponse.from(repository.save(intervencao));
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscarEntidade(id));
    }

    private IntervencaoOperacional buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Intervenção", id));
    }

    private String limpar(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim();
    }
}
