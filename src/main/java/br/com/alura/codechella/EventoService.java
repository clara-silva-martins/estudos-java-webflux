package br.com.alura.codechella;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EventoService {

    @Autowired
    private EventoRespository repositorio;

    public Flux<EventoDTO> obterTodos(){
        return repositorio.findAll()
        .map(EventoDTO::toDTO);

    }

    public Mono<EventoDTO> obterPorId(Long id) {
        return repositorio.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(EventoDTO::toDTO);
    }

    public Mono<EventoDTO> cadastrar(EventoDTO dto) {
        return repositorio.save(dto.toEntity())
                .map(EventoDTO::toDTO);
    }

    public Mono<Void> excluir(Long id) {
        return repositorio.findById(id)
        .flatMap(repositorio::delete);
    }

    public Flux<EventoDTO> obterPorTipo(String tipo) {
        TipoEvento tipoEvento = TipoEvento.valueOf(tipo.toUpperCase());
        return repositorio.findByTipo(tipoEvento)
                .map(EventoDTO::toDTO);
    }
}
