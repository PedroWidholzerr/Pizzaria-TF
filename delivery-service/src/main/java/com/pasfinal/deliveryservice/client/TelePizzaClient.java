package com.pasfinal.deliveryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "telepizza")
public interface TelePizzaClient {

    @PutMapping("/pedidos/{id}/status")
    void atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizarStatusRequest request);
}
