package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.GrupoDeFornecedores;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

public class GrupoDeFornecedoresRequest {

    @NotBlank
    private String produto;

    @NotEmpty
    private List<FornecedorRequest> fornecedores;

    public GrupoDeFornecedoresRequest() {
    }

    public GrupoDeFornecedores paraGrupoDeFornecedores() {
        GrupoDeFornecedores grupoDeFornecedores = new GrupoDeFornecedores(produto);

        this.fornecedores.stream()
                .map(fr -> fr.paraFornecedor())
                .collect(Collectors.toList())
                .forEach(f -> grupoDeFornecedores.adicionaFornecedor(f));

        return grupoDeFornecedores;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public void setFornecedores(List<FornecedorRequest> fornecedores) {
        this.fornecedores = fornecedores;
    }
}
