package org.http4s.client.jdkhttpclient

import java.time.Instant

import cats.effect._
import fs2.Stream
import org.http4s._
import org.http4s.headers.Authorization
import org.http4s.implicits._

import scala.concurrent.duration._

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    for {
      client <- JdkHttpClient.simple[IO]
      req = Request[IO](
        uri = uri"https://discordapp.com/api/gateway/bot",
        headers = Headers.of(Authorization(Credentials.Token("Bot".ci, token)))
      )
      _ <- Stream
        .awakeEvery[IO](5.seconds)
        .evalTap(_ => client.fetch(req)(res => IO(println(res.status))))
        .evalTap(time => IO(println(s"[${Instant.now()}] ${time.toSeconds} seconds")))
        .compile
        .drain
    } yield ExitCode.Success

  val token = "blabliblub"

}
