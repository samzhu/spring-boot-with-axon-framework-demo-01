package com.example.demo.accountms.interfaces.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "帳號建立申請資料")
public class BankAccountCreationDTO {
    @NotNull(message = "開戶金額 為必填")
    @Range(min = 1000, message = "開戶必須儲存 {min} 元")
    @Schema(required = true, description = "初始金額", example = "1000", exclusiveMaximum = true)
    private Integer initialBalance;
    @NotBlank(message = "用戶名 為必填")
    @Schema(required = true, description = "用戶名", example = "sam")
    private String customer;
}
