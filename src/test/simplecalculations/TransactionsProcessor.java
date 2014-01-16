package test.simplecalculations;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionsProcessor {
    private static ConcurrentMap<Integer, Integer> getRules(int rulesNum, int maxValue) {
        ConcurrentMap<Integer, Integer> rules = new ConcurrentHashMap<Integer, Integer>(rulesNum);
        for (int i = 0; i < rulesNum; i++)
            rules.put(i, (int)(Math.random() * maxValue));
        return rules;
    }


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ConcurrentMap<Integer, Integer> rules = getRules(10000, 40);
        ExecutorService transactionsExecutor = Executors.newFixedThreadPool(10);
        ExecutorService rulesExecutor = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 100; i++) {
            Transaction transaction = new Transaction(i);
            Runnable worker = new TransactionWorker("Transaction worker " + i, transaction, rules, rulesExecutor);
            transactionsExecutor.execute(worker);
        }
        transactionsExecutor.shutdown();
        while (!transactionsExecutor.isTerminated()) {}
        System.out.println("Finished all transactions threads");

        rulesExecutor.shutdown();
        while (!rulesExecutor.isTerminated()) {}
        System.out.println("Finished all rules threads");

        long stopTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (stopTime - startTime));
    }
}