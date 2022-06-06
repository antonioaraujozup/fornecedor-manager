package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.model.Telefone;
import br.com.zup.edu.fornecedormanager.repository.FornecedorRepository;
import br.com.zup.edu.fornecedormanager.repository.TelefoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
class RemoverTelefoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    private Fornecedor fornecedorA;

    private Telefone telefone;

    private void cadastraFornecedorA() {
        this.fornecedorA = new Fornecedor("Antonio", "Queijo", "AE Laticínios");
        this.fornecedorRepository.save(fornecedorA);
    }

    private void cadastraTelefone(Fornecedor fornecedor) {
        this.telefone = new Telefone(fornecedor, "27", "1234-5678");
        this.telefoneRepository.save(telefone);
    }

    @BeforeEach
    void setUp() {
        this.telefoneRepository.deleteAll();
        this.fornecedorRepository.deleteAll();
    }

    @Test
    @DisplayName("Não deve remover telefone pois o fornecedor não está cadastrado")
    void naoDeveRemoverTelefonePoisOFornecedorNaoEstaCadastrado() throws Exception {

        // Cenário
        MockHttpServletRequestBuilder request = delete("/fornecedores/{idFornecedor}/telefones/{idTelefone}", Integer.MAX_VALUE, Integer.MAX_VALUE);

        // Ação e Corretude
        Exception resolvedException = mockMvc.perform(request).andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();

        // Asserts
        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Fornecedor nao cadastrado", ((ResponseStatusException) resolvedException).getReason());

    }

    @Test
    @DisplayName("Não deve remover telefone não cadastrado")
    void naoDeveRemoverTelefoneNaoCadastrado() throws Exception {

        // Cenário
        cadastraFornecedorA();

        MockHttpServletRequestBuilder request = delete("/fornecedores/{idFornecedor}/telefones/{idTelefone}", this.fornecedorA.getId(), Integer.MAX_VALUE);

        // Ação e Corretude
        Exception resolvedException = mockMvc.perform(request).andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();

        // Asserts
        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Telefone nao cadastrado", ((ResponseStatusException) resolvedException).getReason());

    }

    @Test
    @DisplayName("Não deve remover telefone que não pertence ao fornecedor")
    void naoDeveRemoverTelefoneQueNaoPertenceAoFornecedor() throws Exception {

        // Cenário
        cadastraFornecedorA();
        Fornecedor fornecedorB = new Fornecedor("Maiana", "Lasanha", "Lasanha da Mai");
        this.fornecedorRepository.save(fornecedorB);

        cadastraTelefone(fornecedorB);

        MockHttpServletRequestBuilder request = delete("/fornecedores/{idFornecedor}/telefones/{idTelefone}", this.fornecedorA.getId(), this.telefone.getId());

        // Ação e Corretude
        Exception resolvedException = mockMvc.perform(request).andExpect(
                        status().isUnprocessableEntity()
                )
                .andReturn()
                .getResolvedException();

        // Asserts
        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Este telefone nao pertence ao fornecedor", ((ResponseStatusException) resolvedException).getReason());

    }

    @Test
    @DisplayName("Deve remover telefone do fornecedor")
    void deveRemoverTelefoneDoFornecedor() throws Exception {

        // Cenário
        cadastraFornecedorA();

        cadastraTelefone(this.fornecedorA);

        MockHttpServletRequestBuilder request = delete("/fornecedores/{idFornecedor}/telefones/{idTelefone}", this.fornecedorA.getId(), this.telefone.getId());

        // Ação e Corretude
        mockMvc.perform(request).andExpect(
                        status().isNoContent()
                );

        // Asserts
        assertFalse(this.telefoneRepository.existsById(this.telefone.getId()), "Não deve existir telefone com esse id");

    }

}