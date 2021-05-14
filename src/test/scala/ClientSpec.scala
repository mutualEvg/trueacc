import ServiceResponses.{debts, paymentPlans, payments}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Service, http}
import com.twitter.util.{Await, Duration, Future}
import org.mockito.Mockito.when
import org.scalatest.FlatSpec
import org.scalatest.Matchers.convertToAnyShouldWrapper
import org.scalatest.mockito.MockitoSugar.mock
import service.PaymentService

case class Foo(someText: String, someInt: Int)

class ClientSpec extends FlatSpec {


  def mockPayments = {
    val paymentService = mock[Service[http.Request, http.Response]]
    val paymentRequest: Request = http.Request(http.Method.Get, "/druska/trueaccord-mock-payments-api/payments")
    val paymentResponse = mock[Response]
    when(paymentResponse.getContentString()).thenReturn(payments)
    when(paymentService.apply(paymentRequest)).thenReturn(Future(paymentResponse))
    (paymentService, paymentRequest, paymentResponse)
  }

  def mockPaymentPlans = {
    val paymentPLanService = mock[Service[http.Request, http.Response]]
    val paymentPLanRequest: Request = http.Request(http.Method.Get, "/druska/trueaccord-mock-payments-api/payment_plans")
    val paymentPlanResponse = mock[Response]
    when(paymentPlanResponse.getContentString()).thenReturn(paymentPlans)
    when(paymentPLanService.apply(paymentPLanRequest)).thenReturn(Future(paymentPlanResponse))
    (paymentPLanService, paymentPLanRequest, paymentPlanResponse)
  }

  def mockDebt = {
    val debtService = mock[Service[http.Request, http.Response]]
    val debtRequest: Request = http.Request(http.Method.Get, "/druska/trueaccord-mock-payments-api/debts")
    val debtResponse = mock[Response]
    when(debtResponse.getContentString()).thenReturn(debts)
    when(debtService.apply(debtRequest)).thenReturn(Future(debtResponse))
    (debtService, debtRequest, debtResponse)
  }

  "payment service" should "return payments" in {

    val payments = mockPayments
    val result: Future[Response] = payments._1.apply(payments._2)
    Await.result(result, Duration.fromMinutes(1))

    result.onSuccess {
      rep: http.Response => {
        println("GET success: " + rep)
        val paymentsJson = rep.getContentString()

        println("GET success: " + paymentsJson)
        Thread.sleep(5000)

      }
    }
  }
  "plans service" should "return payment plans" in {

    val plans = mockPaymentPlans
    val paymentPlanResult: Future[Response] = plans._1.apply(plans._2)
    Await.result(paymentPlanResult, Duration.fromMinutes(1))

    paymentPlanResult.onSuccess {
      rep: http.Response => {
        println("GET success: " + rep)
        val paymentsJson = rep.getContentString()

        println("GET success: " + paymentsJson)
        Thread.sleep(5000)

      }
    }
  }

  "debts service" should "return payment debts" in {

    val debts = mockDebt
    val debtResult: Future[Response] = debts._1.apply(debts._2)
    Await.result(debtResult, Duration.fromMinutes(1))

    debtResult.onSuccess {
      rep: http.Response => {
        println("GET success: " + rep)
        val paymentsJson = rep.getContentString()

        println("GET success: " + paymentsJson)
        Thread.sleep(5000)

      }
    }

  }

  "Payment handler service" should "return payment debt objects" in {

    val resultList = List(
      "{\"amount\":51.25,\"id\":0,\"is_in_payment_plan\":true,\"remaining_amount\":51.25,\"next_payment_due_date\":\"2020-10-05\"}",
      "{\"amount\":51.25,\"id\":0,\"is_in_payment_plan\":true,\"remaining_amount\":0.0,\"next_payment_due_date\":null}",
      "{\"amount\":25.0,\"id\":1,\"is_in_payment_plan\":true,\"remaining_amount\":75.0,\"next_payment_due_date\":\"2020-08-08\"}",
      "{\"amount\":25.0,\"id\":1,\"is_in_payment_plan\":true,\"remaining_amount\":50.0,\"next_payment_due_date\":\"2020-08-08\"}",
      "{\"amount\":4312.67,\"id\":2,\"is_in_payment_plan\":true,\"remaining_amount\":607.6700000000001,\"next_payment_due_date\":\"2020-01-15\"}",
      "{\"amount\":1230.085,\"id\":3,\"is_in_payment_plan\":true,\"remaining_amount\":3082.585,\"next_payment_due_date\":null}",
      "{\"amount\":1230.085,\"id\":3,\"is_in_payment_plan\":true,\"remaining_amount\":1852.5,\"next_payment_due_date\":\"2020-08-08\"}",
      "{\"amount\":1230.085,\"id\":3,\"is_in_payment_plan\":true,\"remaining_amount\":622.415,\"next_payment_due_date\":\"2020-08-08\"}"
    )
    val payments = mockPayments
    val plans = mockPaymentPlans
    val debts = mockDebt
    val service = new PaymentService(
      payments._2,
      payments._1,
      plans._2,
      plans._1,
      debts._2,
      debts._1
    )

    Await.ready(service.result, Duration.fromMicroseconds(3000000))

    service.result.onSuccess {
      rep => {
        rep shouldBe resultList
      }
    }
  }

} 