package io.bootify.spring_batch_project.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BankTransactions {

    @NotNull
    @Size(max = 5)
    private String transactionID;

    @NotNull
    private Integer customerID;

    @NotNull
    @Size(max = 255)
    private String customerDOB;

    @NotNull
    @Size(max = 255)
    private String custGender;

    @NotNull
    @Size(max = 255)
    private String custLocation;

    @NotNull
    private Double custAccountBalance;

    @NotNull
    @Size(max = 255)
    private String transactionDate;

    @NotNull
    private Double transactionTime;

    @NotNull
    private Integer transactionAmount;

}
