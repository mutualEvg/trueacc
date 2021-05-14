import client.Clients._
import com.twitter.util.{Await, Duration}
import service.PaymentService

object App extends App {

  val service = new PaymentService(
    paymentRequest,
    paymentClient,
    paymentPlanRequest,
    paymentPlanClient,
    debtRequest,
    debtClient
  )

  Await.ready(service.result, Duration.fromMicroseconds(3000000))

  service.result.onSuccess {
    rep => {
      rep.foreach(r => println(r))
    }
  }
}
