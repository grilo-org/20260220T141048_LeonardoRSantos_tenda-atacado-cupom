package br.com.tenda.atacado.cupom.application.controller;

import br.com.tenda.atacado.cupom.application.input.CouponInput;
import br.com.tenda.atacado.cupom.application.mapper.AppCouponMapper;
import br.com.tenda.atacado.cupom.application.output.CouponOutput;
import br.com.tenda.atacado.cupom.application.output.FieldMessageOutput;
import br.com.tenda.atacado.cupom.core.domain.Coupon;
import br.com.tenda.atacado.cupom.core.usecase.coupon.DeleteCouponUseCase;
import br.com.tenda.atacado.cupom.core.usecase.coupon.GetCouponByIdUseCase;
import br.com.tenda.atacado.cupom.core.usecase.coupon.SaveCouponUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final AppCouponMapper appCouponMapper;
    private final SaveCouponUseCase saveCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;
    private final GetCouponByIdUseCase getCouponByIdUseCase;

    @PostMapping
    @Operation(summary = "Criar cupom", description = "Cria um novo cupom caso o cupom seja válido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponOutput.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados fornecidos")
    })
    public ResponseEntity<CouponOutput> create(@Valid @RequestBody CouponInput input) {
        Coupon saved = saveCouponUseCase.save(appCouponMapper.toDomain(input));
        return ResponseEntity.status(HttpStatus.CREATED).body(appCouponMapper.toOutput(saved));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cupom", description = "Busca um cupom pelo ID (UUID)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupom encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponOutput.class))),
            @ApiResponse(responseCode = "400", description = "Cupom não encontrado")
    })
    public ResponseEntity<CouponOutput> getById(@PathVariable UUID id) {
        Coupon coupon = getCouponByIdUseCase.execute(id);
        return ResponseEntity.ok(appCouponMapper.toOutput(coupon));
    }

    @DeleteMapping("/{code}")
    @Operation(summary = "Deletar cupom", description = "Realiza soft delete do cupom, impedindo múltiplas exclusões")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupom deletado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FieldMessageOutput.class))),
            @ApiResponse(responseCode = "400", description = "Regra de negócio violada"),
            @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    })
    public ResponseEntity<FieldMessageOutput> delete(@PathVariable String code) {
        deleteCouponUseCase.execute(code);
        return ResponseEntity.ok(new FieldMessageOutput("message", "Cupom deletado com sucesso"));
    }
}
