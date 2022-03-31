package br.com.zup.edu.fornecedormanager.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String produto;

    @Column(nullable = false)
    private String empresa;

    public LocalDateTime criadoEm=LocalDateTime.now();

    public Fornecedor(String nome, String produto, String empresa) {
        this.nome = nome;
        this.produto = produto;
        this.empresa = empresa;
    }

    /**
     * @deprecated  construtor para uso exclusivo do Hibernate
     */
    @Deprecated
    public Fornecedor(){

    }

    public Long getId() {
        return id;
    }
}
