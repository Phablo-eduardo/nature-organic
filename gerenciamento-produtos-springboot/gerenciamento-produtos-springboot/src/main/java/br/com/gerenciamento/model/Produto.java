package br.com.gerenciamento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe o nome do produto.")
    @Column(nullable = false, length = 150)
    private String nome;

    @NotBlank(message = "Informe o código de barras.")
    @Column(nullable = false, unique = true, length = 80)
    private String codigoBarras;

    @NotNull(message = "Informe o preço.")
    @DecimalMin(value = "0.00", inclusive = true, message = "O preço não pode ser negativo.")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @NotNull(message = "Informe a quantidade em estoque.")
    @Min(value = 0, message = "A quantidade não pode ser negativa.")
    @Column(nullable = false)
    private Integer quantidadeEstoque;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imagem;

    @Column(length = 100)
    private String imagemContentType;

    public Produto() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
    public byte[] getImagem() { return imagem; }
    public void setImagem(byte[] imagem) { this.imagem = imagem; }
    public String getImagemContentType() { return imagemContentType; }
    public void setImagemContentType(String imagemContentType) { this.imagemContentType = imagemContentType; }
}
