# Transactions Lab

This module is a live-coding playground for Spring transaction and database concurrency scenarios.

## Scenario 01 — Concurrent Withdrawal

We start with a deliberately naive implementation:

1. Load an account.
2. Check whether its balance is sufficient.
3. Subtract the withdrawal amount.
4. Commit the transaction.

This works for sequential requests, but it does not yet define what should happen when multiple requests modify the same account concurrently.

## Baseline API

Create an account:

```bash
curl -X POST http://localhost:8080/accounts \
  -H 'Content-Type: application/json' \
  -d '{"initialBalance":1000}'
```

Read an account:

```bash
curl http://localhost:8080/accounts/1
```

Withdraw:

```bash
curl -X POST http://localhost:8080/accounts/1/withdraw \
  -H 'Content-Type: application/json' \
  -d '{"amount":700}'
```

## First Live-Coding Exercise

Write a concurrency test that starts an account with a balance of `1000`, then submits two withdrawals of `700` at almost the same time.

Questions to answer before fixing anything:

- Can both callers observe enough balance to withdraw?
- What final balance is persisted?
- Is the result deterministic?
- What anomaly are we observing?
- Does `@Transactional` alone protect this business invariant?

Do not add locking yet. First make the failure observable and explain why it happens.

## Later Iterations

After reproducing the problem, implement and compare these approaches:

- optimistic locking with `@Version`
- pessimistic locking
- atomic database update
- transaction isolation changes
- retry strategy and conflict handling

For every solution, compare correctness, throughput, contention, failure behavior, and operational complexity.
