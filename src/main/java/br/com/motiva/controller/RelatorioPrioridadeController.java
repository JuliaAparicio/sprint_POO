package br.com.motiva.controller;

import br.com.motiva.dto.RelatorioGeradoResponse;
import br.com.motiva.dto.RelatorioResponse;
import br.com.motiva.service.GeradorRelatorio;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioPrioridadeController {

    private final GeradorRelatorio geradorRelatorio;

    public RelatorioPrioridadeController(GeradorRelatorio geradorRelatorio) {
        this.geradorRelatorio = geradorRelatorio;
    }

    /** Gera o relatório a partir dos trechos atuais e salva no histórico. */
    @PostMapping
    public ResponseEntity<RelatorioGeradoResponse> gerar() {
        RelatorioGeradoResponse gerado = geradorRelatorio.gerarRelatorio();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(gerado.relatorio().id()).toUri();
        return ResponseEntity.created(location).body(gerado);
    }

    @GetMapping
    public ResponseEntity<List<RelatorioResponse>> listarHistorico() {
        return ResponseEntity.ok(geradorRelatorio.listarHistorico());
    }

    /** Ex.: /api/relatorios/periodo?inicio=2026-10-01&fim=2026-10-31 */
    @GetMapping("/periodo")
    public ResponseEntity<List<RelatorioResponse>> consultarPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(geradorRelatorio.consultarPorPeriodo(inicio, fim));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RelatorioResponse> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(geradorRelatorio.buscarPorId(id));
    }
}
