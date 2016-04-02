import scala.util.{Try, Failure}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

import com.twitter.finagle.postgres._;
import com.twitter.finagle.postgres.codec.ServerError

package object cockroachExample {
  def transferMoney(client: Client, from: Int, to: Int, amount: Int) {
    val attempt: () => Boolean = () => {
      val commit = for {
      _ <- client.prepareAndExecute("BEGIN")
      // Not supported by finagle-postgres, but should be here for optimal performance.
      // _ <- client.prepareAndExecute("SAVEPOINT cockroach_restart")

      // Do actual transactional stuff. The below is of course very unsafe and
      // real code would do it in one query for performance.
      _ <- client.prepareAndExecute("UPDATE accounts SET balance=balance+$2 WHERE id=$1",
              to, amount)
      _ <- client.prepareAndExecute("UPDATE accounts SET balance=balance+$2 WHERE id=$1",
              from, -amount)

      // Not supported by finagle-postgres, but should be here for optimal performance.
      // _ <- client.prepareAndExecute("RELEASE SAVEPOINT cockroach_restart")
      } yield client.prepareAndExecute("COMMIT")
      Try(commit.get()) match {
        case Failure(t) => {
          // Did not see the error code exposed in a better way.
          if(t.getMessage().drop(6).take(4) != "C000") {
            client.prepareAndExecute("ROLLBACK")
            throw t
          }
          println("restarting transaction")
          false
        }
        case _ => true
      }
    }
    while (!attempt() ) {};
  }
}

object CockroachExample{
  def main(args: Array[String]) = {
    //val client = Client("192.168.99.100:5432", "postgres", null, "bank")
    val client = Client("localhost:26257", "root", null, "bank")
    //client.prepareAndExecute("CREATE TABLE IF NOT EXISTS accounts (id INT PRIMARY KEY, balance INT)").get()
    //client.prepareAndExecute("DELETE FROM accounts").get() // Truncate crashes the client
    //client.prepareAndExecute("INSERT INTO accounts VALUES (1, 1000), (2, 1000)").get()
    cockroachExample.transferMoney(client, 1, 2, 10)
    val f = client.select("select id, balance from accounts;") { row => println(row.get("id").toString + ": " + row.get("balance").toString) }
  }
}
