package br.com.motiva.repository;

import br.com.motiva.model.IntervencaoOperacional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IntervencaoOperacionalRepository extends JpaRepository<IntervencaoOperacional, Long> {

    // SELECT ... WHERE TIPO_INTERVENCAO = ?
    List<IntervencaoOperacional> findByTipo(String tipo);

    // Consultas de alocação (FK ID_EQUIPE / ID_TRECHO)
    List<IntervencaoOperacional> findByEquipeId(Long equipeId);

    List<IntervencaoOperacional> findByTrechoId(Long trechoId);

    List<IntervencaoOperacional> findByDataExecucaoBetween(LocalDateTime inicio, LocalDateTime fim);

    // Usados para impedir exclusões que quebrariam a FK
    boolean existsByEquipeId(Long equipeId);

    boolean existsByTrechoId(Long trechoId);
}
