package br.com.zup.edu.fornecedormanager.controller;

import br.com.zup.edu.fornecedormanager.model.Fornecedor;
import br.com.zup.edu.fornecedormanager.model.Telefone;
import br.com.zup.edu.fornecedormanager.repository.FornecedorRepository;
import br.com.zup.edu.fornecedormanager.repository.TelefoneRepository;
import br.com.zup.edu.fornecedormanager.util.MensagemDeErro;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
class CadastrarTelefoneAoFornecedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    private Fornecedor fornecedor;

    @BeforeEach
    void setUp() {
        this.telefoneRepository.deleteAll();
        this.fornecedorRepository.deleteAll();
        this.fornecedor = new Fornecedor("Lasanha da Mai", "Lasanha", "Mai empresas");
        this.fornecedorRepository.save(this.fornecedor);
    }

    @Test
    @DisplayName("Deve cadastrar um telefone para o fornecedor")
    void deveCadastrarUmTelefoneParaOFornecedor() throws Exception {

        // Cenário
        TelefoneRequest telefoneRequest = new TelefoneRequest(
                "27",
                "12345678"
        );

        String payload = mapper.writeValueAsString(telefoneRequest);

        MockHttpServletRequestBuilder request = post("/fornecedores/{id}/telefones", this.fornecedor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        // Ação e Corretude
        mockMvc.perform(request)
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        redirectedUrlPattern("http://localhost/fornecedores/*/telefones/*")
                );

        // Asserts
        List<Telefone> telefones = telefoneRepository.findAll();
        assertEquals(1,telefones.size());

    }

    @Test
    @DisplayName("Não deve cadastrar um telefone para um fornecedor inexistente")
    void naoDeveCadastrarUmTelefoneParaUmFornecedorInexistente() throws Exception {

        // Cenário
        TelefoneRequest telefoneRequest = new TelefoneRequest(
                "27",
                "12345678"
        );

        String payload = mapper.writeValueAsString(telefoneRequest);

        MockHttpServletRequestBuilder request = post("/fornecedores/{id}/telefones", Integer.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        // Ação e Corretude
        mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                );

    }

    @Test
    @DisplayName("Não deve cadastrar um telefone com dados nulos")
    void naoDeveCadastrarUmTelefoneComDadosNulos() throws Exception {

        // Cenário
        TelefoneRequest telefoneRequest = new TelefoneRequest(
                "",
                null
        );

        String payload = mapper.writeValueAsString(telefoneRequest);

        MockHttpServletRequestBuilder request = post("/fornecedores/{id}/telefones", this.fornecedor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br")
                .content(payload);

        // Ação e Corretude
        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        MensagemDeErro mensagemDeErro = mapper.readValue(payloadResponse, MensagemDeErro.class);

        // Asserts
        assertEquals(2,mensagemDeErro.getMensagens().size());
        assertThat(mensagemDeErro.getMensagens(), containsInAnyOrder(
                "O campo numero não deve estar em branco",
                "O campo ddd não deve estar em branco"
        ));

    }
}