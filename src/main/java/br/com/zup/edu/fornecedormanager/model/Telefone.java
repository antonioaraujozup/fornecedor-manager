package br.com.zup.edu.fornecedormanager.model;

import javax.persistence.*;

@Entity
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2)
    private String ddd;

    @Column(nullable = false, length = 9)
    private String numero;

    @ManyToOne(optional = false)
    private Fornecedor fornecedor;

    public Telefone(String ddd, String numero, Fornecedor fornecedor) {
        this.ddd = ddd;
        this.numero = numero;
        this.fornecedor = fornecedor;
    }

    /**
     * @deprecated Construtor vazio para uso exclusivo do Hibernate.
     */
    @Deprecated
    public Telefone() {
    }

    public Long getId() {
        return id;
    }
}
