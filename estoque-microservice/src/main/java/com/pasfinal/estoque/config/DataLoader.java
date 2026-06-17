package com.pasfinal.estoque.config;

import com.pasfinal.estoque.entity.Ingrediente;
import com.pasfinal.estoque.entity.ItemEstoque;
import com.pasfinal.estoque.repository.IngredienteRepository;
import com.pasfinal.estoque.repository.ItemEstoqueRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(IngredienteRepository ingredienteRepository, 
                                   ItemEstoqueRepository itemEstoqueRepository) {
        return args -> {
            // Limpar dados existentes
            itemEstoqueRepository.deleteAll();
            ingredienteRepository.deleteAll();
            
            // Criar ingredientes com IDs fixos (mesmos IDs do telepizza)
            Ingrediente disco = ingredienteRepository.save(new Ingrediente(1L, "Disco de pizza"));
            Ingrediente tomate = ingredienteRepository.save(new Ingrediente(2L, "Porcao de tomate"));
            Ingrediente mussarela = ingredienteRepository.save(new Ingrediente(3L, "Porcao de mussarela"));
            Ingrediente presunto = ingredienteRepository.save(new Ingrediente(4L, "Porcao de presunto"));
            Ingrediente calabresa = ingredienteRepository.save(new Ingrediente(5L, "Porcao de calabresa"));
            Ingrediente molho = ingredienteRepository.save(new Ingrediente(6L, "Molho de tomate (200ml)"));
            Ingrediente azeitona = ingredienteRepository.save(new Ingrediente(7L, "Porcao de azeitona"));
            Ingrediente oregano = ingredienteRepository.save(new Ingrediente(8L, "Porcao de oregano"));
            Ingrediente cebola = ingredienteRepository.save(new Ingrediente(9L, "Porcao de cebola"));

            // Criar itens de estoque com IDs fixos
            itemEstoqueRepository.save(new ItemEstoque(1L, 30, disco));
            itemEstoqueRepository.save(new ItemEstoque(2L, 30, tomate));
            itemEstoqueRepository.save(new ItemEstoque(3L, 30, mussarela));
            itemEstoqueRepository.save(new ItemEstoque(4L, 30, presunto));
            itemEstoqueRepository.save(new ItemEstoque(5L, 30, calabresa));
            itemEstoqueRepository.save(new ItemEstoque(6L, 30, molho));
            itemEstoqueRepository.save(new ItemEstoque(7L, 30, azeitona));
            itemEstoqueRepository.save(new ItemEstoque(8L, 30, oregano));
            itemEstoqueRepository.save(new ItemEstoque(9L, 30, cebola));

            System.out.println("✅ Dados de estoque inicializados com sucesso!");
        };
    }
}
