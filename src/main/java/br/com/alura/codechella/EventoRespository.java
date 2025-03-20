package br.com.alura.codechella;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EventoRespository extends ReactiveCrudRepository<Evento, Long> {
}
