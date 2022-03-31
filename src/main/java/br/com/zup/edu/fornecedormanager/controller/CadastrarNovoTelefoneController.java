package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.model.Telefone;
import br.com.zup.edu.fornecedormanager.repository.FornecedorRepository;
import br.com.zup.edu.fornecedormanager.repository.TelefoneRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/fornecedores/{idFornecedor}/telefones")
public class CadastrarNovoTelefoneController {

    private final FornecedorRepository fornecedorRepository;
    private final TelefoneRepository telefoneRepository;

    public CadastrarNovoTelefoneController(FornecedorRepository fornecedorRepository, TelefoneRepository telefoneRepository) {
        this.fornecedorRepository = fornecedorRepository;
        this.telefoneRepository = telefoneRepository;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@PathVariable Long idFornecedor, @RequestBody @Valid TelefoneRequest request, UriComponentsBuilder uriComponentsBuilder) {

        Fornecedor fornecedor = fornecedorRepository.findById(idFornecedor)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Não existe fornecedor cadastrado para o id informado."));

        Telefone novoTelefone = request.paraTelefone(fornecedor);

        telefoneRepository.save(novoTelefone);

        URI location = uriComponentsBuilder.path("/fornecedores/{idFornecedor}/telefones/{idTelefone}")
                .buildAndExpand(fornecedor.getId(), novoTelefone.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }

}
