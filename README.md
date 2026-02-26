# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

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
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

### Link to Server Sequence Diagram for Each Endpoint
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5afCCak7A2Onwr6zpdjACBCeKGLWcJBJEmApJvoYu40vuDJMlOylcjeHl3kuwowGKEpujKcplu8SrbigFTDvy9SHnIKDPvE56XteCXqBUy4BmuAaZVurnaY8cL1IJDn-gggFlRh4k1C8yy+Xs3wUVR9aLEsmnVPVlSibUDS4U43RZs1RHzF1sGXh1yFdTAdFNp43h+P4XgoOgMRxIk62bZVvhYKJgqgfUDTSBG-ERu0EbdD0cmqApwztYh6DoWZDywrp5EzS9yELZgpkVK59T2QddlCQdjlqM5sXxbeiVGCg3DHpeGXwb9c4BfyuXBdISNMoYaURfKz1IbD5SmRVEOnhkqgAQDdVQEDvVgf9WnJsgqaNMNo2jP99GMStAQouu-jYOKGr8WiMAAOJKhoR0NZJMtXbd9hKk9P1k+zlSUzAIzjfp8z-YD5TA1ZaJyzmGJSzkVtqFDJLk9lo4W3b8sYpjvIjjj9Sheu9uvhZFR6-btu1Z9TNmyzTVjOrOYFsMDTjPHKAAJLSAWACM4TBIEazDGMmzxLqKBupyrUF8hySgGq5eQVNMCF6nABySpdRcMCdD1EnvQN3N4Z0Y1x-LCxJynSoZ9nuf503yHF6X9efI3hc1yAdcTZXc8j3MbdzB3XeLQxy3Mf4HAAOxuE4KBODEEbBHAXEAGzwBOhj2zARSc2U5m96drQdDVhraYWt0BZlbkqHuiZdaM1ji1EEEDiLQkZszTsSU372wxIHbQLlg7lBdugo8KB7bDGnEbNYiC-Iu19vlAOSog6dhDrA+2DNI6oMavrZYqcp71BznnNmLNBT9yGoPOeBsd7p0zrwme-MlpMVWpYJG1lNhbSQAkMAii+wQBUQAKQgOKWWFZ-BrzVF-EoYlf4cOaMyGSPRU6a3RkhLM2AEDAEUVAOAEBrJQAoZPaQUC+ofS-HAzehlXHuM8d4743DM4mxQdHNBMAABW+i0CYOwfIXBjD8Hw3pDARkYBMExK9guVQND-aGLmMTfJfjna5Ndn4LQ6Iil+JWMMXybS0AuLcZQSJ0ASn7hocybATT370OKng0OMVTYnU4RInhMA+GBAEb3IR38wCnR5mIrhfjp78KPoLU+Xg3GqPUcc+UiBgywGANgFxhA8gFE-orSxklzqXWurdYwb1SqRyasgthCSqBIhgCAbgeBPZ1Kxnk0FVyIXUPKHlPGyMxlVO0MMGAGLMVYoxeFNFaBsUEsKi+CZ2S9YwrwLTemMyY6cICX3dZmzRFjQOUAA
