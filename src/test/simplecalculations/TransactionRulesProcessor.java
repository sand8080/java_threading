package test.simplecalculations;

import java.util.concurrent.*;

public class TransactionRulesProcessor {
    private final ExecutorService executor;

    TransactionRulesProcessor(ExecutorService executor) {
        this.executor = executor;
    }

    Integer calculate(final Transaction transaction, final ConcurrentMap<Integer, Integer> rules) {
        CompletionService<CalculationResult> completionService =
                new ExecutorCompletionService<CalculationResult>(executor);
        for (final ConcurrentMap.Entry<Integer, Integer> entry : rules.entrySet())
            completionService.submit(new Callable<CalculationResult>() {
                public CalculationResult call() {
                    Integer result = entry.getValue();
                    return new CalculationResult(entry.getKey(), result, transaction);
                }
            });

        Integer total = 0;
        System.out.println("Transaction " + transaction.getId() + " rules processing started");
        try {
            for (int i = 0, n = rules.size(); i < n; i++) {
                Future<CalculationResult> f = completionService.take();
                total += f.get().getResult();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        }
        System.out.println("Transaction " + transaction.getId() +
                " calculation total result: " + total);
        return total;
    }
}
