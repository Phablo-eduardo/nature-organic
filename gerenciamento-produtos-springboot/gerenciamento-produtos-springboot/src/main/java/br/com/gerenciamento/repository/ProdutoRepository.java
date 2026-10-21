package br.com.gerenciamento.repository;

import br.com.gerenciamento.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);
    List<Produto> findByQuantidadeEstoqueGreaterThanOrderByNomeAsc(Integer quantidade);
    List<Produto> findByQuantidadeEstoqueEqualsOrderByNomeAsc(Integer quantidade);
    boolean existsByCodigoBarrasIgnoreCaseAndIdNot(String codigoBarras, Long id);
}
