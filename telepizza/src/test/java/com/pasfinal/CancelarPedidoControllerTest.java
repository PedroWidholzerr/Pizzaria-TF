package com.pasfinal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CancelarPedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void deveCancelarPedidoAprovado() throws Exception {
        mockMvc.perform(post("/pedidos/1/cancelamento"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELADO"));
    }

    @Test
    @Order(2)
    void naoCancelaPedidoJaCancelado() throws Exception {
        mockMvc.perform(post("/pedidos/1/cancelamento"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value("CANCELADO"));
    }

    @Test
    @Order(3)
    void naoCancelaPedidoInexistente() throws Exception {
        mockMvc.perform(post("/pedidos/999/cancelamento"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("NAO_ENCONTRADO"));
    }

    @Test
    @Order(4)
    void consultaStatusAposCancelamento() throws Exception {
        mockMvc.perform(get("/pedidos/1/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELADO"));
    }
}
