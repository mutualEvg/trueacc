## How to run
### Dependencies
#### Download this before you start
1. Java 8 - specifically jdk1.8.0_anyNumbersHere (downloads for any OS - https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html)
2. Scala 2.12 or 2.13 (downloads for any OS - https://www.scala-lang.org/download/)
3. SBT tool (downloads for any OS - https://www.scala-sbt.org/download.html)

### Command line
1. Do `git clone <repo>` and `cd <repo>`
2. `sbt run`


## What I would do if I had more time
- Add more comments
- More thorough testing for additional cases, invalid inputs/cases, run scenarios, etc
- Add javadocs, i.e. documentation
- Check endpoint API that I'm consuming - make sure all preconditions are true
  i.e. check PaymentPlan really is One-to-one with debt.
- Optimize some of my data structures
  i.e. things like, tweak lists etc
- Optimize code
- Rename some things, like DebtInfo, and/or find better readable names

## Assumptions/Problems
### Assumptions
1. endpoint APIs return back ids and startdates in ascending order (but ok)
2. BI_WEEKLY = twice a month (not twice a week)

### Problems
Actually it can be many particular cases that I need to discuss first before the logic implementation

## Endpoints (snapshots)
https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans
```json
[
  {
    "amount": 123.46,
    "id": 0
  },
  {
    "amount": 100,
    "id": 1
  },
  {
    "amount": 4920.34,
    "id": 2
  },
  {
    "amount": 12938,
    "id": 3
  },
  {
    "amount": 9238.02,
    "id": 4
  }
]
```
https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans
```json
[
  {
    "amount_to_pay": 102.5,
    "debt_id": 0,
    "id": 0,
    "installment_amount": 51.25,
    "installment_frequency": "WEEKLY",
    "start_date": "2020-09-28"
  },
  {
    "amount_to_pay": 100,
    "debt_id": 1,
    "id": 1,
    "installment_amount": 25,
    "installment_frequency": "WEEKLY",
    "start_date": "2020-08-01"
  },
  {
    "amount_to_pay": 4920.34,
    "debt_id": 2,
    "id": 2,
    "installment_amount": 1230.085,
    "installment_frequency": "BI_WEEKLY",
    "start_date": "2020-01-01"
  },
  {
    "amount_to_pay": 4312.67,
    "debt_id": 3,
    "id": 3,
    "installment_amount": 1230.085,
    "installment_frequency": "WEEKLY",
    "start_date": "2020-08-01"
  }
]
```
https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments
```json
[
  {
    "amount": 51.25,
    "date": "2020-09-29",
    "payment_plan_id": 0
  },
  {
    "amount": 51.25,
    "date": "2020-10-29",
    "payment_plan_id": 0
  },
  {
    "amount": 25,
    "date": "2020-08-08",
    "payment_plan_id": 1
  },
  {
    "amount": 25,
    "date": "2020-08-08",
    "payment_plan_id": 1
  },
  {
    "amount": 4312.67,
    "date": "2020-08-08",
    "payment_plan_id": 2
  },
  {
    "amount": 1230.085,
    "date": "2020-08-01",
    "payment_plan_id": 3
  },
  {
    "amount": 1230.085,
    "date": "2020-08-08",
    "payment_plan_id": 3
  },
  {
    "amount": 1230.085,
    "date": "2020-08-15",
    "payment_plan_id": 3
  }
]
```