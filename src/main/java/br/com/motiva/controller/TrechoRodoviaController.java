package br.com.motiva.controller;

import br.com.motiva.dto.IntervencaoResponse;
import br.com.motiva.dto.TrechoRequest;
import br.com.motiva.dto.TrechoResponse;
import br.com.motiva.service.IntervencaoOperacionalService;
import br.com.motiva.service.TrechoRodoviaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/** Camada HTTP apenas: recebe, delega ao Service e devolve o status correto. */
@RestController
@RequestMapping("/api/trechos")
public class TrechoRodoviaController {

    private final TrechoRodoviaService service;
    private final IntervencaoOperacionalService intervencaoService;

    public TrechoRodoviaController(TrechoRodoviaService service,
                                   IntervencaoOperacionalService intervencaoService) {
        this.service = service;
        this.intervencaoService = intervencaoService;
    }

    @GetMapping
    public ResponseEntity<List<TrechoResponse>> listar(
            @RequestParam(name = "nivelMinimo", required = false) Double nivelMinimo,
            @RequestParam(name = "tipoTerreno", required = false) String tipoTerreno,
            @RequestParam(name = "rodovia", required = false) String rodovia) {
        return ResponseEntity.ok(service.listar(nivelMinimo, tipoTerreno, rodovia));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrechoResponse> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TrechoResponse> criar(@Valid @RequestBody TrechoRequest request) {
        TrechoResponse criado = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);   // 201 + header Location
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrechoResponse> atualizar(@PathVariable("id") Long id,
                                                    @Valid @RequestBody TrechoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable("id") Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();              // 204
    }

    /** Simula a chegada de uma leitura do sensor (só para trechos IoT). */
    @PostMapping("/{id}/leitura-sensor")
    public ResponseEntity<TrechoResponse> registrarLeituraSensor(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.registrarLeituraSensor(id));
    }

    @GetMapping("/{id}/intervencoes")
    public ResponseEntity<List<IntervencaoResponse>> listarIntervencoes(@PathVariable("id") Long id) {
        return ResponseEntity.ok(intervencaoService.listarPorTrecho(id));
    }
}
