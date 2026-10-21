package br.com.gerenciamento.repository;

import br.com.gerenciamento.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    @Query("select v.produto, sum(v.quantidadeVendida) from Venda v group by v.produto order by sum(v.quantidadeVendida) desc")
    List<Object[]> findProdutosMaisVendidos();

    @Query("select coalesce(avg(v.valorTotal), 0) from Venda v")
    BigDecimal findMediaValorVendas();

    @Query("select coalesce(sum(v.valorTotal), 0) from Venda v where v.dataVenda >= :inicio")
    BigDecimal findTotalVendasDesde(LocalDateTime inicio);
}
