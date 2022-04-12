package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.GrupoDeFornecedores;
import br.com.zup.edu.fornecedormanager.repository.GrupoDeFornecedoresRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@RestController
public class RemoverGrupoDeFornecedoresController {

    private final GrupoDeFornecedoresRepository repository;

    public RemoverGrupoDeFornecedoresController(GrupoDeFornecedoresRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @DeleteMapping("/grupo-de-fornecedores/{id}")
    public ResponseEntity<?> remover(@PathVariable("id") Long id) {
        GrupoDeFornecedores grupoDeFornecedores = repository.findById(id).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo não encontrado");
        });

        repository.delete(grupoDeFornecedores);

        return ResponseEntity.noContent().build();
    }
}
