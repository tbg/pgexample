import com.twitter.conversions.time._
import com.twitter.util.Awaitable._
import com.twitter.finagle.postgres._;
import com.twitter.finagle.postgres.codec.ServerError

package object pgexample {
  def cockroachTxn(client: Client, from: Int, to: Int, amount: Int) {
    val attempt = () => {
      println("b")
      val q = for {
        _ <- client.prepareAndExecute("begin")
        // Works unless this one is activated (regardless of whether the RELEASE
        // below is).
        _ <- client.prepareAndExecute("SAVEPOINT cockroach_restart")
        _ <- client.prepareAndExecute("UPDATE accounts SET balance=balance+$2 WHERE id=$1",
              to, amount)
        _ <- client.prepareAndExecute("UPDATE accounts SET balance=balance+$2 WHERE id=$1",
              from, -amount)
        _ <- client.prepareAndExecute("RELEASE SAVEPOINT cockroach_restart")
      } yield client.prepareAndExecute("commit")

      q.get()
    }
    attempt()
  }
}

object PGExample {
  def main(args: Array[String]) = {
    val client = Client("localhost:26257", "root", null, "bank")
    client.prepareAndExecute("CREATE TABLE IF NOT EXISTS accounts (id INT PRIMARY KEY, balance INT)").get()
    pgexample.cockroachTxn(client, 1, 2, 13)
    val f = client.select("select id, balance from accounts;") { row => println(row.get("id").toString + ": " + row.get("balance").toString) }
  }
}
