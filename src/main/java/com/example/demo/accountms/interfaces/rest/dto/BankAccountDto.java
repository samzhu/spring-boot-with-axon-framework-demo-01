package com.example.demo.accountms.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "帳號資訊")
public class BankAccountDto {
    @Schema(required = true, description = "帳號", example = "123456")
    private String accountNumber;
    @Schema(required = true, description = "餘額", example = "1000")
    private Integer balance;
    @Schema(required = true, description = "客戶姓名", example = "sam")
    private String customer;
}
