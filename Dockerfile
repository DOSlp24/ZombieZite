FROM zgwmike/akka-sbt
WORKDIR /main
EXPOSE 8081
ADD . /main
CMD sbt "runMain de.htwg.se.zombiezite.MicroServices.SimpleCounterMain"