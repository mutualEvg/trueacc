package client

import com.twitter.finagle.http.Request
import com.twitter.finagle.{Http, Service, http}

object Clients{

  val paymentClient: Service[http.Request, http.Response] = Http.newService("my-json-server.typicode.com:80")
  val paymentRequest: Request = http.Request(http.Method.Get, "/druska/trueaccord-mock-payments-api/payments")
  paymentRequest.host = "my-json-server.typicode.com"


  val paymentPlanClient: Service[http.Request, http.Response] = Http.newService("my-json-server.typicode.com:80")
  val paymentPlanRequest: Request = http.Request(http.Method.Get, "/druska/trueaccord-mock-payments-api/payment_plans")
  paymentPlanRequest.host = "my-json-server.typicode.com"


  val debtClient: Service[http.Request, http.Response] = Http.newService("my-json-server.typicode.com:80")
  val debtRequest = http.Request(http.Method.Get, "/druska/trueaccord-mock-payments-api/debts")
  debtRequest.host = "my-json-server.typicode.com"

}

