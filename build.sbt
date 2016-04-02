lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= Seq(
    "com.github.finagle" %% "finagle-postgres" % "0.1.0"
    )
  )
