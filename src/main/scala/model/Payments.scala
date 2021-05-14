package model


case class PaymentPlan(
                        amount_to_pay: Double,
                        debt_id: Int,
                        id: Int,
                        installment_amount: Double,
                        installment_frequency: String,
                        start_date: String
                      )

case class Payment(amount: Double, date: String, payment_plan_id: Int)

case class Debt(amount: Double, id: Int)

case class ResultDebt(
                       amount: Double,
                       id: Int,
                       is_in_payment_plan: Boolean,
                       remaining_amount: Double,
                       next_payment_due_date: Option[String]
                     )
