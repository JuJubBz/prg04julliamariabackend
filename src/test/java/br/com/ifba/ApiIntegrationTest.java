package br.com.ifba;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc
class ApiIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    String email, token;
    long itemId;

    @BeforeEach void preparar() throws Exception {
        email="aluno-"+UUID.randomUUID()+"@exemplo.com";
        mvc.perform(post("/api/auth/cadastro").contentType(MediaType.APPLICATION_JSON).content("""
                {"nome":"Pessoa Teste","email":"%s","senha":"senha-segura-123"}
                """.formatted(email))).andExpect(status().isCreated()).andExpect(jsonPath("$.senha").doesNotExist());
        String tokenResponse=mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {"email":"%s","senha":"senha-segura-123"}
                """.formatted(email))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        token=json.readTree(tokenResponse).get("token").asText();
        String itemResponse=mvc.perform(post("/api/itens").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("""
                {"tipo":"ALBUM","nome":"Disco de Teste","artista":"Artista"}
                """)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        itemId=json.readTree(itemResponse).get("id").asLong();
    }

    @Test void autenticaCadastroLoginERejeitaCredencialInvalida() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {"email":"%s","senha":"errada"}
                """.formatted(email))).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/auth/cadastro").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test void pesquisaCatalogoPorCategoriaETexto() throws Exception {
        mvc.perform(get("/api/itens").param("tipo","ALBUM").param("q","Disco"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].nome").value("Disco de Teste"));
        mvc.perform(get("/api/bandas")).andExpect(status().isOk());
        mvc.perform(get("/api/artistas")).andExpect(status().isOk());
        mvc.perform(get("/api/albuns")).andExpect(status().isOk());
        mvc.perform(get("/api/musicas")).andExpect(status().isOk());
    }

    @Test void endpointsCrudDeAlbum() throws Exception {
        String album="{\"nome\":\"Album REST\",\"artista\":\"Artista REST\",\"descricao\":\"Criado no teste\"}";
        String criado=mvc.perform(post("/albums/save").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content(album))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.nome").value("Album REST")).andReturn().getResponse().getContentAsString();
        long id=json.readTree(criado).get("id").asLong();
        mvc.perform(get("/albums/findall")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists());
        mvc.perform(get("/albums/findbyid/"+id)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
        mvc.perform(get("/albums/findbyname/REST")).andExpect(status().isOk()).andExpect(jsonPath("$[0].nome").value("Album REST"));
        mvc.perform(put("/albums/update/"+id).header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Album atualizado\",\"artista\":\"Artista REST\",\"descricao\":\"Atualizado\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Album atualizado"));
        mvc.perform(delete("/albums/delete/"+id).header("Authorization","Bearer "+token)).andExpect(status().isNoContent());
        mvc.perform(get("/albums/findbyid/"+id)).andExpect(status().isNotFound());
    }

    @Test void cicloDeAvaliacaoEVotoComControleDeDuplicidade() throws Exception {
        String body="{\"texto\":\"Excelente album\",\"itemId\":"+itemId+"}";
        String created=mvc.perform(post("/api/avaliacoes").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.texto").value("Excelente album")).andReturn().getResponse().getContentAsString();
        long id=json.readTree(created).get("id").asLong();
        mvc.perform(get("/api/avaliacoes")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(id));
        mvc.perform(get("/api/avaliacoes").param("itemId",Long.toString(itemId))).andExpect(status().isOk());
        mvc.perform(get("/api/avaliacoes/"+id)).andExpect(status().isOk());
        mvc.perform(put("/api/avaliacoes/"+id).header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"texto\":\"Atualizada\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.texto").value("Atualizada"));
        mvc.perform(post("/api/avaliacoes/"+id+"/votos").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/avaliacoes/"+id+"/votos").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"util\":true}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/avaliacoes/"+id+"/votos").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"util\":false}"))
                .andExpect(status().isConflict());
        mvc.perform(get("/api/avaliacoes/"+id)).andExpect(jsonPath("$.votosUteis").value(1));
        mvc.perform(delete("/api/avaliacoes/"+id+"/votos").header("Authorization","Bearer "+token)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/avaliacoes/"+id).header("Authorization","Bearer "+token)).andExpect(status().isNoContent());
        mvc.perform(get("/api/avaliacoes/"+id)).andExpect(status().isNotFound());
    }

    @Test void protegeRotasEValidaAutoria() throws Exception {
        mvc.perform(post("/api/avaliacoes").contentType(MediaType.APPLICATION_JSON).content("{\"texto\":\"sem login\",\"itemId\":1}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/api/avaliacoes").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        String other="outro-"+UUID.randomUUID()+"@exemplo.com";
        mvc.perform(post("/api/auth/cadastro").contentType(MediaType.APPLICATION_JSON).content("""
                {"nome":"Outra Pessoa","email":"%s","senha":"senha-segura-123"}
                """.formatted(other))).andExpect(status().isCreated());
        String login=mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {"email":"%s","senha":"senha-segura-123"}
                """.formatted(other))).andReturn().getResponse().getContentAsString();
        String otherToken=json.readTree(login).get("token").asText();
        String created=mvc.perform(post("/api/avaliacoes").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"texto\":\"Minha avaliacao\",\"itemId\":"+itemId+"}"))
                .andReturn().getResponse().getContentAsString();
        long id=json.readTree(created).get("id").asLong();
        mvc.perform(delete("/api/avaliacoes/"+id).header("Authorization","Bearer "+otherToken)).andExpect(status().isForbidden());
        mvc.perform(put("/api/avaliacoes/999999").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"texto\":\"x\"}"))
                .andExpect(status().isNotFound());
    }
}
