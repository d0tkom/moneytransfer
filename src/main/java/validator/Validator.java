package validator;

import exception.InvalidAmountException;
import exception.InvalidTransferException;

import java.math.BigDecimal;

public class Validator {

    public void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidAmountException("Amount: " + amount + " is not valid");
    }


    public void validateStartingBalance(BigDecimal balance) {
        if (balance != null && balance.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidAmountException("Starting balance: " + balance + " is not valid");
    }

    public void validateAccountsDiffer(String acc1, String acc2) {
        if (acc1.equals(acc2))
            throw new InvalidTransferException("Money cannot be sent to same account");
    }
}
