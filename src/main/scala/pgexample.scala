import com.twitter.finagle.postgres._;
import com.twitter.finagle.postgres.codec.ServerError

object PGExample {
  def main(args: Array[String]) = {
    val client = Client("localhost:26257", "root", null, "bank")
    println("running")
    val brokenUpdate = "UPDATE stuffthaterrorsout SET nofield = 123"
    // Works (i.e returns with an exception):
    // val fut = client.executeUpdate(brokenUpdate)
    // Does not work (gets stuck forever):
    val fut = client.prepareAndExecute(brokenUpdate)

    val res = fut.get()
    println("never getting here")
  }
}
