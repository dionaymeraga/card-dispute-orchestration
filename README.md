# Card dispute orchestration

A card dispute process built on Camunda 8. It checks the transaction, applies the bank's
rules, sends the hard cases to a person, and files a chargeback with the card scheme.

## Why

When someone disputes a card payment, their bank sends a statement to whoever handles its
disputes. A person there pulls up the transaction, checks it against what the cardholder
said, and decides whether a chargeback can be raised. If it can, they file it with
Mastercard or Visa. If it can't, they tell the bank why.

Most of that is the same checking over and over. The hard part is the judgement in the
middle. This project automates the first part and keeps the judgement where it belongs.

## What it does

A dispute comes in and gets triaged: Claude reads the cardholder's statement alongside the
transaction and the card's recent activity, and says what kind of dispute it is and how
confident it is. A DMN table then decides what happens next.

Small, clearly duplicated charges go straight through. Everything else goes to a person,
who either initiates the chargeback or decides it can't be raised and has the bank notified.

The rules live in a decision table rather than in code, so someone who isn't a developer
can read them and change the limits:

| Transaction matches | Category | Confidence | Amount | Route |
|---|---|---|---|---|
| false | any | any | any | Manual review |
| true | Duplicate charge | High | <= 50 | Auto-approve |
| any | any | any | any | Manual review |

These rules are my own examples, not card scheme rules.

## The pieces

Camunda runs the process in the cloud. Three Spring Boot apps run locally:

- **dispute-workers** (8081) does the work: triage, filing, notifying the bank
- **mock-card-system** (8080) stands in for the bank's card system
- **mock-scheme** (8082) stands in for Mastercard or Visa

The two mocks are separate apps on purpose. They belong to different organisations in
real life, and keeping them apart means I can stop one and see what the process does when
a system is down. It turns out it stops with an incident, and a retry picks up where it
left off.

## Things I decided on purpose

**The rules decide, not the AI.** Claude reports the category and how sure it is. For a
duplicate charge it can see both transactions, so it says HIGH. For a fraud claim it says
MEDIUM, because you can't confirm fraud from transaction data. The decision table takes
those as inputs and decides the route. Nothing gets filed because a model felt confident.
Its reasoning is saved as a process variable, so the person reviewing can see why.

**No card numbers in the process.** The process carries a transaction reference. When the
worker needs the PAN to file, it fetches it, uses it for that one call, and lets it go. It
never becomes a process variable, so it never lands in Operate, in the history or in an
export.

**You can't file the same chargeback twice.** The scheme mock rejects a repeat filing for
the same reference, and the worker treats that as "already done" rather than failing. Job
retries can't cause a double filing.

## Running it

You'll need JDK 21, a Camunda 8 cluster and an Anthropic API key.

Create a Camunda API client with the Orchestration Cluster scope, then put your credentials
in `dispute-workers/src/main/resources/secrets.properties`:

```
camunda.client.auth.client-id=...
camunda.client.auth.client-secret=...
camunda.client.cloud.cluster-id=...
camunda.client.cloud.region=...
anthropic.api-key=...
```

Start the three apps. The workers app deploys the process, the decision table and the form
on startup, so there's nothing to deploy by hand. Then start an instance with:

```json
{
  "transactionId": "tx-1002",
  "cardholderStatement": "I was charged twice for one coffee."
}
```

That one goes through automatically. Try `tx-2001` with "I did not make this purchase" to
see it land in Tasklist instead.

## What's not here

This covers the process up to a filed chargeback. What happens after that, if the other
side responds and the case goes further, isn't modelled yet.

All the data is made up. No real card numbers, transactions or references.

## Next

- The "we need more information" loop back to the bank, with a deadline
- Waiting for the scheme's response after filing
- A custom connector for the filing call
- Tests, and a set of sample disputes to check the agent's accuracy against
- Letting the agent choose what to look up, instead of handing it everything up front

