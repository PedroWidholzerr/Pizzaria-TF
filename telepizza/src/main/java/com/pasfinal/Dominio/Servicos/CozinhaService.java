package com.pasfinal.Dominio.Servicos;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.pasfinal.Dominio.Dados.PedidoRepository;
import com.pasfinal.Dominio.Entidades.Pedido;

@Service
public class CozinhaService {
    private Queue<Pedido> filaEntrada;
    private Pedido emPreparacao;
    private Queue<Pedido> filaSaida;

    private ScheduledExecutorService scheduler;
    private PedidoRepository pedidoRepository;
    private EntregaService entregaService;

    public CozinhaService(PedidoRepository pedidoRepository, EntregaService entregaService) {
        filaEntrada = new LinkedBlockingQueue<Pedido>();
        emPreparacao = null;
        filaSaida = new LinkedBlockingQueue<Pedido>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        this.pedidoRepository = pedidoRepository;
        this.entregaService = entregaService;
    }

    private synchronized void colocaEmPreparacao(Pedido pedido){
        pedido.setStatus(Pedido.Status.PREPARACAO);
        pedidoRepository.salva(pedido);
        emPreparacao = pedido;
        // Agenda pedidoPronto para ser chamado em 5 segundos
        scheduler.schedule(() -> pedidoPronto(), 5, TimeUnit.SECONDS);
    }

    public synchronized void chegadaDePedido(Pedido p) {
        p.setStatus(Pedido.Status.AGUARDANDO);
        pedidoRepository.salva(p);
        
        filaEntrada.add(p);
        if (emPreparacao == null) {
            colocaEmPreparacao(filaEntrada.poll());
        }
    }

    public synchronized void pedidoPronto() {
        emPreparacao.setStatus(Pedido.Status.PRONTO);
        pedidoRepository.salva(emPreparacao);
        filaSaida.add(emPreparacao);
        
        // Envia o pedido para o setor de entregas
        entregaService.receberPedidoPronto(emPreparacao);
        
        emPreparacao = null;
        // Se tem pedidos na fila, programa a preparação para daqui a 1 segundo
        if (!filaEntrada.isEmpty()){
            Pedido prox = filaEntrada.poll();
            scheduler.schedule(() -> colocaEmPreparacao(prox), 1, TimeUnit.SECONDS);
        }
    }
}
