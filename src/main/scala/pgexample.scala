import com.twitter.finagle.postgres._;

object PGExample {
  def main(args: Array[String]) = {
    val client = Client("localhost:26257", "root", null, "")
    val f = client.select("select * from system.lease") { row => row }
    println("Responded " + f.get)
  }
}
