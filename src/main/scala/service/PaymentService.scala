package service

import com.twitter.finagle.http.Request
import com.twitter.finagle.{Service, http}
import com.twitter.util.Future
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import model.{Debt, Payment, PaymentPlan, ResultDebt}

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.collection.mutable

class PaymentService(
                      paymentRequest: Request,
                      paymentClient: Service[http.Request, http.Response],
                      paymentPlanRequest: Request,
                      paymentPlanClient: Service[http.Request, http.Response],
                      debtRequest: Request,
                      debtClient: Service[http.Request, http.Response]
                    ) {
  val paymentMap = new mutable.HashMap[Int, Double]()
  val paymentResponse: Future[http.Response] = paymentClient(paymentRequest)
  val planResponse: Future[http.Response] = paymentPlanClient(paymentPlanRequest)
  val debtResponse: Future[http.Response] = debtClient(debtRequest)

  val payments: Future[List[Payment]] = paymentResponse
    .map(paymentJson => {
      val payment: List[Payment] = decode[List[Payment]](paymentJson.getContentString()) match {
        case Left(_) => List[Payment]()
        case Right(value) => value
      }
      payment

    })

  val debts: Future[List[Debt]] = debtResponse.map(debtJson => {
    val plan: List[Debt] = decode[List[Debt]](debtJson.getContentString()) match {
      case Left(_) => List[Debt]()
      case Right(value) => value
    }
    plan
  })

  val plans: Future[List[PaymentPlan]] = planResponse.map(planJson => {
    val plan: List[PaymentPlan] = decode[List[PaymentPlan]](planJson.getContentString()) match {
      case Left(_) => List[PaymentPlan]()
      case Right(value) => value
    }
    plan
  })

  val result = payments.flatMap(paymentsList => {
    plans.flatMap((plansList: List[PaymentPlan]) => {
      Future {
        paymentsList.map((payment: Payment) => {
          val remainingAmount: Double = getRemainingAmount(payment, plansList)
          ResultDebt(
            payment.amount,
            payment.payment_plan_id,
            isInPaymentPlan(payment, plansList),
            remainingAmount,
            getNextPaymentDate(payment, plansList, remainingAmount)
          ).asJson.noSpaces
        })
      }
    })
  })

  def getNextPaymentDate(payment: Payment, list: List[PaymentPlan], remainingAmount: Double): Option[String] = {
    val paymentPlan = list.find(el => el.id == payment.payment_plan_id).get
    val startDate = LocalDate.parse(paymentPlan.start_date, DateTimeFormatter.ISO_LOCAL_DATE)
    if (remainingAmount != 0.0) {
      val paymentDate = LocalDate.parse(payment.date, DateTimeFormatter.ISO_LOCAL_DATE)
      if (paymentDate.isAfter(startDate)
      //&& startDate.plusDays(getDaysNumber(paymentPlan.installment_frequency)).isAfter(paymentDate)
      ) {
        Some(startDate.plusDays(getDaysNumber(paymentPlan.installment_frequency)).toString)
      } else None
    } else None
  }

  def getDaysNumber(frequency: String): Int = {
    if (frequency.equals("WEEKLY")) 7
    else if (frequency.equals("BI_WEEKLY")) 14
    else 7
  }

  def isInPaymentPlan(payment: Payment, list: List[PaymentPlan]): Boolean =
    list.map(plan => plan.id).contains(payment.payment_plan_id)

  def getRemainingAmount(payment: Payment, planList: List[PaymentPlan]) = {
    //    println(s"paymentMap $paymentMap payment ${payment.payment_plan_id} planList ${planList.map(el => el.id)}")
    if (!paymentMap.contains(payment.payment_plan_id) && planList.map(el => el.id).contains(payment.payment_plan_id)) {
      val remainingAmount: Double = planList.find(p => p.id == payment.payment_plan_id).get.amount_to_pay - payment.amount
      paymentMap += (payment.payment_plan_id -> remainingAmount)
      //      println(s"paymentMap1 $paymentMap remaining $remainingAmount")
      remainingAmount
    } else if (paymentMap.contains(payment.payment_plan_id) && planList.map(el => el.id).contains(payment.payment_plan_id)) {
      val remainingAmount: Double = paymentMap(payment.payment_plan_id) - payment.amount
      paymentMap += (payment.payment_plan_id -> remainingAmount)
      remainingAmount
    } else {
      0.0
    }
  }
}
