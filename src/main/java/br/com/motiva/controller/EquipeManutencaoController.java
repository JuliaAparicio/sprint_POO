package br.com.motiva.controller;

import br.com.motiva.dto.EquipeRequest;
import br.com.motiva.dto.EquipeResponse;
import br.com.motiva.dto.IntervencaoResponse;
import br.com.motiva.service.EquipeManutencaoService;
import br.com.motiva.service.IntervencaoOperacionalService;
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

@RestController
@RequestMapping("/api/equipes")
public class EquipeManutencaoController {

    private final EquipeManutencaoService service;
    private final IntervencaoOperacionalService intervencaoService;

    public EquipeManutencaoController(EquipeManutencaoService service,
                                      IntervencaoOperacionalService intervencaoService) {
        this.service = service;
        this.intervencaoService = intervencaoService;
    }

    @GetMapping
    public ResponseEntity<List<EquipeResponse>> listar(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "responsavel", required = false) String responsavel) {
        return ResponseEntity.ok(service.listar(nome, responsavel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipeResponse> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EquipeResponse> criar(@Valid @RequestBody EquipeRequest request) {
        EquipeResponse criada = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipeResponse> atualizar(@PathVariable("id") Long id,
                                                    @Valid @RequestBody EquipeRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable("id") Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    /** Consulta de alocação: intervenções atribuídas à equipe. */
    @GetMapping("/{id}/intervencoes")
    public ResponseEntity<List<IntervencaoResponse>> listarIntervencoes(@PathVariable("id") Long id) {
        return ResponseEntity.ok(intervencaoService.listarPorEquipe(id));
    }
}
