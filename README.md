* setup sbt: http://www.scala-sbt.org/0.13/docs/Setup.html
* run the example:
```
05:32 $ sbt run
[info] Set current project to root (in build file:/Users/tschottdorf/scala/pgtest/)
[info] Compiling 1 Scala source to /Users/tschottdorf/scala/pgtest/target/scala-2.10/classes...
[warn] there were 1 deprecation warning(s); re-run with -deprecation for details
[warn] one warning found
[info] Running PGExample
Apr 01, 2016 5:33:00 AM com.twitter.finagle.Init$$anonfun$1 apply$mcV$sp
INFO: Finagle version 6.29.0 (rev=5f4f8b8b9420082468fb99c95e90fd5f924e09e5) built at 20150928-113102
[error] (run-main-0) java.lang.IllegalArgumentException: Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]
java.lang.IllegalArgumentException: Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]
	at java.sql.Timestamp.valueOf(Timestamp.java:251)
	at com.twitter.finagle.postgres.values.StringValueParser$.parseTimestamp(Values.scala:140)
	at com.twitter.finagle.postgres.values.ValueParser$$anonfun$14.apply(Values.scala:205)
	at com.twitter.finagle.postgres.values.ValueParser$$anonfun$14.apply(Values.scala:205)
	at com.twitter.finagle.postgres.Client$$anonfun$com$twitter$finagle$postgres$Client$$extractRows$1$$anonfun$apply$5.apply(Client.scala:173)
	at com.twitter.finagle.postgres.Client$$anonfun$com$twitter$finagle$postgres$Client$$extractRows$1$$anonfun$apply$5.apply(Client.scala:172)
	at scala.collection.TraversableLike$$anonfun$map$1.apply(TraversableLike.scala:244)
	at scala.collection.TraversableLike$$anonfun$map$1.apply(TraversableLike.scala:244)
```
