package com.hsbc.roboadvisor.payload;

import java.util.List;
import javax.validation.constraints.NotNull;

import com.hsbc.roboadvisor.model.Transaction;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Recommendation transactions Update Request")
public class TransactionsRequest
{
    @ApiModelProperty(required = true, value = "List of Transactions.")
    @NotNull(message = "List of Transactions may not be empty")
    private List<Transaction> transactionList;

    public TransactionsRequest() {
        //empty constructor
    }

    public TransactionsRequest(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
