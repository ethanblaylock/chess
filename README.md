# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Design
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFJjr5CxYKQAi-YAEEQIFFy6YAJgoBGwLihiad49lAgBXbDADEaYFQCerDt178kg2wHcAFkjAxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAegsDKAAdNABvfMp7AFsUABoYXHVvaE06lErgJAQAX0xhGJgIl04ePgEhaNF4qFceSgAKcqgq2vr9LiaoFpg2joQASkw2YfcxvtEByPkwJRU1Lnj2FDAAVQKFguWDq5vVdQujHTxMgAUQAMsC4OkYItljAAGbmSrQgqYb7KX5cAaDI5uUaecYiVTxNAWBAIQ4zE74s6qf5o27qeIgab8FCveYw4DVOoNdbNL4KdF3f7GeIASQAciCWFDOdzVo1mq12p0YJL0ilkbQcSMPIIaYZBvSMUyWYFFBYwL53hUuSgBdchX9BqK1VLgTKtUs7XVgJbfOkIABrdBujUwP1W1GChmY0LYyl4-UTIkR-2BkNoCnHJMEqjneORPqUeKRgPB9C9aKULGRYLoMDxBIAJgADK3MsUSmWM+geuhNOIdBAAB4wCD9HVU5OElDxUEQTh50SYMyWaw2aAyR4wBdLnz+QKYethIvVuKJVIZbI5AzqfFduUrXkbTQ9YtQCJT3MG+IIRdIGgNrevKL78tmuJ6sutJGjGJowNw7JQHujyaGKQFPg6PzCi6gLkGCEKyh8drwoiaZRsaOEJjmUG-vCwAIAYEG6qcKaGpccF3H+AFoEhwHLDyayvlhTpxpErpwAAEhCADSCp8psMDeAEvherCgFeholHqIW360WxsS8BYYh6axs50pxjIwMyKCshaVr8XaImxiKeGStKRG2vKPYVmgYaamW0aOi5hZDJBZn5qmPmZsx07QYagwfqW6a+VW1A1qFJ6NokbYdl20V9pgA5DqO46Tom+mzvOi6WGABqruYVi2GYKChnutUHgEQTIA2-xJYksgEekwI3neXAPvYKUxR+X4VRFkwwP+7C1XMBVoAcpnUmxETafcMAxEgcKOPZ5aZqtU3oM5GKuUCBGQuRp2hgiEBIoFu21mFLFbVVMBGWIsU-mxFy7dxy2WudVq9utQXYc64l4YN4LDWpPoPVDMAAGIsCkACyD0w6JH2bTOkVzjAJJkgDlWkx9-Vpf0mU9RgTa5ZkRVoIOxWYMOY4TjWxPxfObgAOJ2lw9Vrk1NjTJou5uDAovVJiNh+F1x5M8wiXnk2wvAlkuTsHaxRrfTGWRALdHIDwhtKxDj3QxbQM7ZZ9wHUdJ1Q3bUNXTh4SuiC4L3WtpEvfj72hY7P1-VT80weEIMwI8YCKGSitqHMPs6X7gIB4RidiyHSI22Iu26XN32k7ExcaJHpOFkl1em5+ERZbELbtp2pTV-2HMlbz5U0bHZNwGaKBpxLjUbtgFhQNgCCGCPtmBArJEq4e3UhJrZ7pReyRpPrOTF8bF1oF2xcSna77nv8tcLTZrLF17vl1OfTkxxXBbxy7sRu8dJ9P5mTOYl-Z3ShMHZ6r1-QExCtRcKH9UzR1vnHBOSc05zFftUIBERRS53uhgwwED87VGgRiMug94FzgpuSd+JNRDO2Ciae+gQ05ik0Ogu0F9MEkLuNgwE7kPRQnwS-O0YpZAIQwBAIhJcXZkLgbQokxdRE0PivXbWijZBN0LK3dueUu4iNkD3Qc3NSp81gEgsmAApCAgFx5sQauuWwjgUBkggN4GAViNJp1sDoBAoAgzq03n1bWl5ngHyPpNSGvkuxZTgBAf8UBhHVFEVfHeN9y7yLJgAK2sWgR+a1EkoFEXUGJcToAbXSfFYG39f4e18gAy63C4YgMDmAk+hcw4uyJhUuiiDulOy-gwriicnhoPwVg7OsRcGCI4SRQhxdGlcFkV9DJhkoDGXEOY+hsN7hJ3SLZSosT4nsKSbIYpGtDllIWbwyZoCpFFPgOc0psA5l2iubNchKyqHKINFs0SsQDBgD2VyC5UBjmFNOQ8zeILxmin4Z6dRZyoVPPEZAKRbzwjmNiN87aEQkqaJbhrNurN2ZGJ5mVfmfSfqIFslARQ2A56EGpiuSWG4zDAGcNSvAEZ6XIBAFBTqR4spBJ3k2RGesRq5GMJojFlLK4gHnngOlDK+VjAzti2cvzYyxE0M4p4bIChcDVaXCZYrkYMQQO0xY6q64DO2dq3V5p-SGphYCU1wIIxknaYFTZtq-k6vnswsWRqZEmqGu6817T5ml3eXIwW1q6G4u1vi8I2jiUkqAA


## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
