package br.com.gerenciamento.controller;

import br.com.gerenciamento.repository.ProdutoRepository;
import br.com.gerenciamento.repository.VendaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class DashboardController {

    private final ProdutoRepository produtoRepository;
    private final VendaRepository vendaRepository;

    public DashboardController(ProdutoRepository produtoRepository, VendaRepository vendaRepository) {
        this.produtoRepository = produtoRepository;
        this.vendaRepository = vendaRepository;
    }

    @GetMapping({"/", "/catalogo"})
    public String vitrine(Model model) {
        model.addAttribute("produtos", produtoRepository.findByQuantidadeEstoqueGreaterThanOrderByNomeAsc(0));
        return "home/vitrine";
    }

    @GetMapping("/sobre-nos")
    public String sobreNos() {
        return "home/sobre-nos";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("totalProdutos", produtoRepository.count());
        model.addAttribute("produtosAtivos", produtoRepository.findByQuantidadeEstoqueGreaterThanOrderByNomeAsc(0).size());
        model.addAttribute("produtosInativos", produtoRepository.findByQuantidadeEstoqueEqualsOrderByNomeAsc(0).size());
        model.addAttribute("totalEstoque", produtoRepository.findAll().stream()
                .mapToInt(produto -> produto.getQuantidadeEstoque() == null
                        ? 0
                        : produto.getQuantidadeEstoque())
                .sum());
        model.addAttribute("mediaVendas", vendaRepository.findMediaValorVendas());
        LocalDateTime agora = LocalDateTime.now();
        model.addAttribute("vendasMensais", vendaRepository.findTotalVendasDesde(agora.minusMonths(1)));
        model.addAttribute("vendasAnuais", vendaRepository.findTotalVendasDesde(agora.minusYears(1)));
        model.addAttribute("produtosMaisVendidos", vendaRepository.findProdutosMaisVendidos());
        return "home/index";
    }
}
