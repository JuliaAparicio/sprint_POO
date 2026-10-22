package br.com.motiva.repository;

import br.com.motiva.model.EquipeManutencao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Substitui o EquipeManutencaoDAO (375 linhas) da Sprint 3. */
public interface EquipeManutencaoRepository extends JpaRepository<EquipeManutencao, Long> {

    List<EquipeManutencao> findByNomeEquipeContainingIgnoreCase(String nomeEquipe);

    List<EquipeManutencao> findByResponsavelContainingIgnoreCase(String responsavel);
}
