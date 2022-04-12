package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.GrupoDeFornecedores;
import br.com.zup.edu.fornecedormanager.repository.GrupoDeFornecedoresRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class CadastrarNovoGrupoFornecedorController {

    private final GrupoDeFornecedoresRepository repository;

    public CadastrarNovoGrupoFornecedorController(GrupoDeFornecedoresRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/grupo-de-fornecedores")
    public ResponseEntity<?> cadastrar(@RequestBody @Valid GrupoDeFornecedoresRequest request, UriComponentsBuilder uriComponentsBuilder) {
        GrupoDeFornecedores novoGrupoDeFornecedores = request.paraGrupoDeFornecedores();

        repository.save(novoGrupoDeFornecedores);

        URI location = uriComponentsBuilder.path("/grupo-de-fornecedores/{id}")
                .buildAndExpand(novoGrupoDeFornecedores.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
