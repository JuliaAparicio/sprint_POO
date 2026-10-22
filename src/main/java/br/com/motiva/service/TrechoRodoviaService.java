package br.com.motiva.service;

import br.com.motiva.dto.TrechoRequest;
import br.com.motiva.dto.TrechoResponse;
import br.com.motiva.exception.ConflitoException;
import br.com.motiva.exception.RecursoNaoEncontradoException;
import br.com.motiva.exception.RegraNegocioException;
import br.com.motiva.model.Constantes;
import br.com.motiva.model.MonitoravelViaIoT;
import br.com.motiva.model.TrechoMonitoradoIoT;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.IntervencaoOperacionalRepository;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrechoRodoviaService {

    private final TrechoRodoviaRepository repository;
    private final IntervencaoOperacionalRepository intervencaoRepository;
    private final IntervencaoService intervencaoService;

    // Injeção de dependência por construtor (o Spring fornece as instâncias)
    public TrechoRodoviaService(TrechoRodoviaRepository repository,
                                IntervencaoOperacionalRepository intervencaoRepository,
                                IntervencaoService intervencaoService) {
        this.repository = repository;
        this.intervencaoRepository = intervencaoRepository;
        this.intervencaoService = intervencaoService;
    }

    @Transactional(readOnly = true)
    public List<TrechoResponse> listar(Double nivelMinimo, String tipoTerreno, String rodovia) {
        List<TrechoRodovia> trechos;
        if (nivelMinimo != null) {
            validarNivelVegetacao(nivelMinimo);
            trechos = repository.findByNivelVegetacaoGreaterThanEqual(nivelMinimo);
        } else if (tipoTerreno != null && !tipoTerreno.isBlank()) {
            trechos = repository.findByTipoTerreno(normalizarTerreno(tipoTerreno));
        } else if (rodovia != null && !rodovia.isBlank()) {
            trechos = repository.findByNomeRodoviaContainingIgnoreCase(rodovia.trim());
        } else {
            trechos = repository.findAll();
        }
        return trechos.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TrechoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public TrechoResponse criar(TrechoRequest req) {
        String terreno = validar(req);
        TrechoRodovia trecho = Boolean.TRUE.equals(req.monitoradoIot())
                ? new TrechoMonitoradoIoT(req.nomeRodovia().trim(), req.quilometroInicial(),
                        req.quilometroFinal(), req.nivelVegetacao(), terreno)
                : new TrechoRodovia(req.nomeRodovia().trim(), req.quilometroInicial(),
                        req.quilometroFinal(), req.nivelVegetacao(), terreno);
        return toResponse(repository.save(trecho));
    }

    @Transactional
    public TrechoResponse atualizar(Long id, TrechoRequest req) {
        TrechoRodovia trecho = buscarEntidade(id);
        String terreno = validar(req);

        if (req.monitoradoIot() != null && req.monitoradoIot() != trecho.isMonitoradoIot()) {
            throw new RegraNegocioException("Não é possível alterar se um trecho é monitorado via IoT. "
                    + "Exclua o trecho e cadastre-o novamente com o tipo correto.");
        }

        trecho.atualizarDados(req.nomeRodovia().trim(), req.quilometroInicial(),
                req.quilometroFinal(), req.nivelVegetacao(), terreno);
        return toResponse(repository.save(trecho));
    }

    @Transactional
    public void remover(Long id) {
        TrechoRodovia trecho = buscarEntidade(id);
        if (intervencaoRepository.existsByTrechoId(id)) {
            throw new ConflitoException("O trecho " + id + " possui intervenções registradas. "
                    + "Remova as intervenções antes de excluir o trecho.");
        }
        repository.delete(trecho);
    }

    /** Usa a interface MonitoravelViaIoT: só trechos IoT aceitam leitura de sensor. */
    @Transactional
    public TrechoResponse registrarLeituraSensor(Long id) {
        TrechoRodovia trecho = buscarEntidade(id);
        if (!(trecho instanceof MonitoravelViaIoT sensor)) {
            throw new RegraNegocioException("O trecho " + id + " não é monitorado via IoT "
                    + "e não pode receber leituras de sensor.");
        }
        sensor.transmitirDadosSensor();
        return toResponse(repository.save(trecho));
    }

    /** Usado por outros services para obter a entidade (com 404 padronizado). */
    @Transactional(readOnly = true)
    public TrechoRodovia buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Trecho de rodovia", id));
    }

    // ------------------------------------------------------------------
    // Regras de negócio da Sprint 1. Ficam no Service (e não no Controller)
    // para valerem qualquer que seja a porta de entrada do sistema.
    // ------------------------------------------------------------------
    private String validar(TrechoRequest req) {
        validarNivelVegetacao(req.nivelVegetacao());
        if (req.quilometroInicial() < 0) {
            throw new RegraNegocioException("O km inicial não pode ser negativo.");
        }
        if (req.quilometroFinal() <= req.quilometroInicial()) {
            throw new RegraNegocioException("O km final (" + req.quilometroFinal()
                    + ") deve ser maior que o km inicial (" + req.quilometroInicial() + ").");
        }
        return normalizarTerreno(req.tipoTerreno());
    }

    private void validarNivelVegetacao(double nivel) {
        if (nivel < 0) {
            throw new RegraNegocioException("O nível de vegetação não pode ser negativo.");
        }
    }

    private String normalizarTerreno(String tipoTerreno) {
        String terreno = tipoTerreno.trim().toUpperCase();
        if (!terreno.equals(Constantes.TERRENO_SECO) && !terreno.equals(Constantes.TERRENO_UMIDO)) {
            throw new RegraNegocioException("Tipo de terreno inválido: '" + tipoTerreno
                    + "'. Use SECO ou UMIDO.");
        }
        return terreno;
    }

    private TrechoResponse toResponse(TrechoRodovia trecho) {
        return TrechoResponse.from(trecho, intervencaoService.classificar(trecho));
    }
}
