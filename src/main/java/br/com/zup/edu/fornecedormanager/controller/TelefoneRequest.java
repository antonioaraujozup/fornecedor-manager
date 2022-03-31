package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.model.Telefone;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class TelefoneRequest {

    @NotBlank
    @Length(min = 2, max = 2)
    private String ddd;

    @NotBlank
    @Length(min = 9, max = 9)
    private String numero;

    public TelefoneRequest(String ddd, String numero) {
        this.ddd = ddd;
        this.numero = numero;
    }

    public TelefoneRequest() {
    }

    public Telefone paraTelefone(Fornecedor fornecedor) {
        return new Telefone(ddd,numero,fornecedor);
    }

    public String getDdd() {
        return ddd;
    }

    public String getNumero() {
        return numero;
    }
}
