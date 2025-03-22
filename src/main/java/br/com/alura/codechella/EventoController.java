package br.com.alura.codechella;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequestMapping("/eventos")
public class EventoController {

//    @Autowired
//    private EventoService servico;


    private final EventoService servico;
    private final Sinks.Many<EventoDTO> eventoSink;


    public EventoController(EventoService servico) {
        this.servico = servico;
        this.eventoSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping//(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDTO> obterTodos() {
        return servico.obterTodos();
    }

    @GetMapping(value = "/categoria/{tipo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDTO> obterPorTipo(@PathVariable String tipo) {
        return Flux.merge(servico.obterPorTipo(tipo), eventoSink.asFlux())
                .delayElements(Duration.ofSeconds(4));
    }

    @GetMapping("/{id}")
    public Mono<EventoDTO> obterPorId(@PathVariable Long id) {
        return servico.obterPorId(id);
    }

    @PostMapping
    public Mono<EventoDTO> cadastrar (@RequestBody EventoDTO dto) {
        return servico.cadastrar(dto)
                .doOnSuccess(eventoSink::tryEmitNext);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> excluir (@PathVariable Long id) {
        return servico.excluir(id);
    }





}
