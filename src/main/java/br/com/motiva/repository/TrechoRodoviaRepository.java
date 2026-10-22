package br.com.motiva.repository;

import br.com.motiva.model.TrechoRodovia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Substitui o TrechoRodoviaDAO da Sprint 3. Nenhum SQL escrito à mão:
 * o Spring Data gera a implementação e as consultas a partir do nome dos métodos.
 */
public interface TrechoRodoviaRepository extends JpaRepository<TrechoRodovia, Long> {

    // SELECT ... WHERE NIVEL_VEGETACAO >= ?
    List<TrechoRodovia> findByNivelVegetacaoGreaterThanEqual(double minimo);

    // SELECT ... WHERE TIPO_TERRENO = ?
    List<TrechoRodovia> findByTipoTerreno(String tipoTerreno);

    // SELECT ... WHERE UPPER(NOME_RODOVIA) LIKE UPPER('%?%')
    List<TrechoRodovia> findByNomeRodoviaContainingIgnoreCase(String nomeRodovia);

    // SELECT ... ORDER BY NIVEL_VEGETACAO DESC  (usado pelo relatório)
    List<TrechoRodovia> findAllByOrderByNivelVegetacaoDesc();
}
