package test.simplecalculations;

public class CalculationResult {
    private final Integer ruleId;
    private final Integer result;
    private final Transaction transaction;

    public CalculationResult(Integer ruleId, Integer result, Transaction transaction) {
        this.ruleId = ruleId;
        this.result = result;
        this.transaction = transaction;
    }

    public Integer getResult() { return this.result; }
}
