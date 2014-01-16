package test.simplecalculations;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

public class TransactionWorker implements Runnable {
    private final String command;
    private final Transaction transaction;
    private final ConcurrentMap<Integer, Integer> rules;
    private final ExecutorService rulesExecutor;
    private final TransactionRulesProcessor processor;

    public TransactionWorker(String command, Transaction transaction, ConcurrentMap<Integer, Integer> rules,
                             ExecutorService rulesExecutor) {
        this.command = command;
        this.transaction = transaction;
        this.rules = rules;
        this.rulesExecutor = rulesExecutor;
        this.processor = new TransactionRulesProcessor(this.rulesExecutor);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start. Command = " + this.command);
        processCommand();
        System.out.println(Thread.currentThread().getName() + " End.");
    }

    private void processCommand() {
        Integer total = this.processor.calculate(this.transaction, this.rules);
        System.out.println("Thread " + Thread.currentThread().getName() + " total " + total);
    }

    @Override
    public String toString(){
        return this.command;
    }
}