package io.ismaildrissi.app.digitalbanking.dtos;

import lombok.Data;

@Data
public class TransferDTO {
    private String senderAccountId;
    private String receiverAccountId;
    private int balance;
    private String desciption;
}
