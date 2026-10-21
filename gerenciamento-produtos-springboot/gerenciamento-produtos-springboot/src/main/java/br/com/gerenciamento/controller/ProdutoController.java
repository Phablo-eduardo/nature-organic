package br.com.gerenciamento.controller;

import br.com.gerenciamento.model.Produto;
import br.com.gerenciamento.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String nome, Model model) {
        model.addAttribute("produtos", (nome == null || nome.isBlank())
                ? produtoRepository.findAll()
                : produtoRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim()));
        model.addAttribute("nomeBusca", nome == null ? "" : nome);
        return "Produto/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        return "Produto/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("produto") Produto produto,
                         BindingResult result,
                         @RequestParam(value = "imagemArquivo", required = false) MultipartFile imagemArquivo,
                         RedirectAttributes redirectAttributes) {
        if (produto.getId() != null && produtoRepository.existsByCodigoBarrasIgnoreCaseAndIdNot(produto.getCodigoBarras(), produto.getId())) {
            result.rejectValue("codigoBarras", "duplicado", "Já existe um produto com este código de barras.");
        }
        if (result.hasErrors()) {
            return "Produto/formulario";
        }
        if (imagemArquivo != null && !imagemArquivo.isEmpty()) {
            if (imagemArquivo.getContentType() == null || !imagemArquivo.getContentType().startsWith("image/")) {
                result.reject("imagemArquivo", "Selecione um arquivo de imagem válido.");
                return "Produto/formulario";
            }
            if (imagemArquivo.getSize() > 5 * 1024 * 1024) {
                result.reject("imagemArquivo", "A imagem deve ter no máximo 5 MB.");
                return "Produto/formulario";
            }
            try {
                produto.setImagem(imagemArquivo.getBytes());
                produto.setImagemContentType(imagemArquivo.getContentType());
            } catch (java.io.IOException exception) {
                result.reject("imagemArquivo", "Não foi possível ler a imagem enviada.");
                return "Produto/formulario";
            }
        } else if (produto.getId() != null) {
            produtoRepository.findById(produto.getId()).ifPresent(existing -> {
                produto.setImagem(existing.getImagem());
                produto.setImagemContentType(existing.getImagemContentType());
            });
        }
        try {
            produtoRepository.save(produto);
            redirectAttributes.addFlashAttribute("sucesso", "Produto salvo com sucesso.");
            return "redirect:/produtos";
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("codigoBarras", "duplicado", "Já existe um produto com este código de barras.");
            return "Produto/formulario";
        }
    }

    @GetMapping("/{id}/imagem")
    @ResponseBody
    public ResponseEntity<byte[]> imagem(@PathVariable @NonNull Long id) {
        return produtoRepository.findById(id)
                .filter(produto -> produto.getImagem() != null && produto.getImagemContentType() != null)
                .map(produto -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, produto.getImagemContentType())
                        .body(produto.getImagem()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable @NonNull Long id, Model model, RedirectAttributes redirectAttributes) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    model.addAttribute("produto", produto);
                    return "Produto/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("erro", "Produto não encontrado.");
                    return "redirect:/produtos";
                });
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable @NonNull Long id, RedirectAttributes redirectAttributes) {
        produtoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("sucesso", "Produto excluído com sucesso.");
        return "redirect:/produtos";
    }

    @GetMapping("/ativos")
    public String ativos(Model model) {
        model.addAttribute("produtos", produtoRepository.findByQuantidadeEstoqueGreaterThanOrderByNomeAsc(0));
        model.addAttribute("titulo", "Produtos ativos");
        return "Produto/status";
    }

    @GetMapping("/inativos")
    public String inativos(Model model) {
        model.addAttribute("produtos", produtoRepository.findByQuantidadeEstoqueEqualsOrderByNomeAsc(0));
        model.addAttribute("titulo", "Produtos inativos");
        return "Produto/status";
    }
}
