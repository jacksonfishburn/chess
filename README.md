# ♕ BYU CS 240 Chess

[chess.jacksonfishburn.dev](https://chess.jacksonfishburn.dev/)

## What it is and What I Learned

A Chess Server which manages users, auth and game state, and a CLI Client for connecting to the server and playing chess games. 

Making this project in the spring semester of my second year at BYU was one of my biggest and most important learning experiences in programming yet. I went from knowing how to code, to understanding and being able to leverage that skill to build useful and scalable systems that can be hosted on the web. 

What I Learned:
- Programming in Java: OOP, Interfaces and Abstract Classes, Exceptions, Basic Design Principles
- Serializing JSON to take data sent over the web and use it as objects in a Java program
- Communicating over HTTP through a REST API
- Writing and using Unit Tests to verify service logic
- Setting up and using a Relational database with SQL
- Hashing passwords for security
- Authenticating user requests with tokens
- Debugging with logs and IDE tools
- Setting up and managing connections to WebSockets
- Applying basic software design principles such as single responsibility, low coupling, avoidance of code duplication and information/data hiding to create modular code
## Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](https://github.com/jacksonfishburn/chess/raw/main/10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

I made a few more [sequence diagrams](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5afCCak7A2Onwr6zpdjACBCeKGLWcJBJEmApJvoYu40vuDJMlOylcjeHl3kuwowGKEpujKcplu8SrbigFTDvy9SHnIKDPvE56XteCXqBUy4BmuAaZVurnaY8cL1IJDn-gggFlRh4k1C8yy+Xs3wUVR9aLEsmnVPVlSibUDS4U43RZs1RHzF1sGXh1yFdTAdFNp43h+P4XgoOgMRxIk62bZVvhYKJgqgfUDTSBG-ERu0EbdD0cmqApwztYh6DoWZDywrp5EzS9yELZgpkVK59T2QddlCQdjlqM5sXxbeiVGCg3DHpeGXwb9c4BfyuXBdISNMoYaURfKz1IbD5SmRVEOnhkqgAQDdVQEDvVgf9WnJsgqaNMNo2jP99GMStAQouu-jYOKGr8WiMAAOJKhoR0NZJMtXbd9hKk9P1k+zlSUzAIzjfp8z-YD5TA1ZaJyzmGJSzkVtqFDJLk9lo4W3b8sYpjvIjjj9Sheu9uvhZFR6-btu1Z9TNmyzTVjOrOYFsMDTjPHKAAJLSAWACM4TBIEazDGMmzxLqKBupyrUF8hySgGq5eQVNMCF6nABySpdRcMCdD1EnvQN3N4Z0Y1x-LCxJynSoZ9nuf503yHF6X9efI3hc1yAdcTZXc8j3MbdzB3XeLQxy3Mf4HAAOxuE4KBODEEbBHAXEAGzwBOhj2zARSc2U5m96drQdDVhraYWt0BZlbkqHuiZdaM1ji1EEEDiLQkZszTsSU372wxIHbQLlg7lBdugo8KB7bDGnEbNYiC-Iu19vlAOSog6dhDrA+2DNI6oMavrZYqcp71BznnNmLNBT9yGoPOeBsd7p0zrwme-MlpMVWpYJG1lNhbSQAkMAii+wQBUQAKQgOKWWFZ-BrzVF-EoYlf4cOaMyGSPRU6a3RkhLM2AEDAEUVAOAEBrJQAoZPaQUC+ofS-HAzehlXHuM8d4743DM4mxQdHNBMAABW+i0CYOwfIXBjD8Hw3pDARkYBMExK9guVQND-aGLmMTfJfjna5Ndn4LQ6Iil+JWMMXybS0AuLcZQSJ0ASn7hocybATT370OKng0OMVTYnU4RInhMA+GBAEb3IR38wCnR5mIrhfjp78KPoLU+Xg3GqPUcc+UiBgywGANgFxhA8gFE-orSxklzqXWurdYwb1SqRyasgthCSqBIhgCAbgeBPZ1Kxnk0FVyIXUPKHlPGyMxlVO0MMGAGLMVYoxeFNFaBsUEsKi+CZ2S9YwrwLTemMyY6cICX3dZmzRFjQOUAA) that outline the flow of specific endpoints.
## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```shell
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Deployment

[chess.jacksonfishburn.dev](https://chess.jacksonfishburn.dev/)

A small Node (`bridge/server.js`) process serves an xterm.js page and opens a WebSocket at `/terminal`. On each browser connection it spawns your Java CLI (`client.ClientMain`), piping stdout/stderr into the terminal and keystrokes into the process’s stdin—so the same CLI runs in the browser without rewriting it as a web app.

## Tech stack

- Java 21 — main language
- Maven — multi-module build (`client`, `server`, `shared`)
- Javalin — HTTP server
- WebSockets (Jakarta / Tyrus) — real-time game updates
- MySQL 8 — persistence
- Gson — JSON
- jBCrypt — password hashing
- JUnit 5 — tests
- Docker / Docker Compose — app + DB packaging
- Node.js (`bridge/`) — Express + `ws` bridge for browser ↔ Java client
- IntelliJ — primary IDE support
