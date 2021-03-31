package com.example.demo.accountms.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "帳號建立結果資訊")
public class BankAccountCreatedDTO {
    @Schema(required = true, description = "帳號", example = "123456")
    private String accountNumber;
}
