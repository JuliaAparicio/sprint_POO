package br.com.motiva.service;

import br.com.motiva.dto.EquipeRequest;
import br.com.motiva.dto.EquipeResponse;
import br.com.motiva.exception.ConflitoException;
import br.com.motiva.exception.RecursoNaoEncontradoException;
import br.com.motiva.model.EquipeManutencao;
import br.com.motiva.repository.EquipeManutencaoRepository;
import br.com.motiva.repository.IntervencaoOperacionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipeManutencaoService {

    private final EquipeManutencaoRepository repository;
    private final IntervencaoOperacionalRepository intervencaoRepository;

    public EquipeManutencaoService(EquipeManutencaoRepository repository,
                                   IntervencaoOperacionalRepository intervencaoRepository) {
        this.repository = repository;
        this.intervencaoRepository = intervencaoRepository;
    }

    @Transactional(readOnly = true)
    public List<EquipeResponse> listar(String nome, String responsavel) {
        List<EquipeManutencao> equipes;
        if (nome != null && !nome.isBlank()) {
            equipes = repository.findByNomeEquipeContainingIgnoreCase(nome.trim());
        } else if (responsavel != null && !responsavel.isBlank()) {
            equipes = repository.findByResponsavelContainingIgnoreCase(responsavel.trim());
        } else {
            equipes = repository.findAll();
        }
        return equipes.stream().map(EquipeResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public EquipeResponse buscarPorId(Long id) {
        return EquipeResponse.from(buscarEntidade(id));
    }

    @Transactional
    public EquipeResponse criar(EquipeRequest req) {
        EquipeManutencao equipe = new EquipeManutencao(req.nomeEquipe().trim(), req.responsavel().trim());
        return EquipeResponse.from(repository.save(equipe));
    }

    @Transactional
    public EquipeResponse atualizar(Long id, EquipeRequest req) {
        EquipeManutencao equipe = buscarEntidade(id);
        equipe.atualizarDados(req.nomeEquipe().trim(), req.responsavel().trim());
        return EquipeResponse.from(repository.save(equipe));
    }

    @Transactional
    public void remover(Long id) {
        EquipeManutencao equipe = buscarEntidade(id);
        if (intervencaoRepository.existsByEquipeId(id)) {
            throw new ConflitoException("A equipe " + id + " está alocada em intervenções. "
                    + "Realoque ou remova essas intervenções antes de excluir a equipe.");
        }
        repository.delete(equipe);
    }

    @Transactional(readOnly = true)
    public EquipeManutencao buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipe de manutenção", id));
    }
}
