# Codex Naturalis
<div align="center">
    <a href="https://www.craniocreations.it/prodotto/codex-naturalis"><img src="https://cdn.discordapp.com/attachments/1235868074163310634/1255514954052927498/codex-background.jpg?ex=667d68eb&is=667c176b&hm=4af60febb2514f499799c73f9a9343b3f87032539831ccba0f4a1656211c0950&" alt="Codex Naturalis" width="900"></a>
</div>

## Team
[Cosimo Giovanni Negri](https://github.com/cosimonegri)<br />
[Alexandru Cezar Mierlita](https://github.com/cezarmierlita)<br />
[Davide Orizio](https://github.com/DolbyTheSheep)<br />
[Emanuele Lovino](https://github.com/EmanueleLovino)<br />

## What we have implemented

| Feature        |                    Implemented                    |
|:---------------|:-------------------------------------------------:|
| Basic rules    |                :heavy_check_mark:                 |
| Complete rules |                :heavy_check_mark:                 |
| Socket         |                :heavy_check_mark:                 |
| RMI            |                :heavy_check_mark:                 |
| TUI            |                :heavy_check_mark:                 |
| GUI            |                :heavy_check_mark:                 |
| Multiple games |                :heavy_check_mark:                 |
| Disconnections |                        :x:                        |
| Persistence    |                        :x:                        |
| Chat           |                        :x:                        |

## How to use
- In the [deliverables](deliverables) folder there are two jar files, one for the server and one for the client.

- The server can be run with the following command:
    ```shell
    java -jar CodexNaturalisServer.jar
    ```
  The RMI port is 1099 and the socket port is 6666.

- The client can be run with the following command:
    ```shell
    java -jar CodexNaturalisClient.jar
    ```
    By default this command runs the GUI and establishes a connection with the server on localhost using socket.
  
    You can change its behaviour by adding a combination of the following cli arguments:
  - **-i**: followed by _gui_ (default) or by _tui_
  - **-n**: followed by _socket_ (default) or by _rmi_
  - **-h**: followed by the ip of the server

## Disclaimer
[_Codex Naturalis_](https://www.craniocreations.it/prodotto/codex-naturalis) is property of [_Cranio Creations_](https://www.craniocreations.it) and all of the copyrighted graphical assets used in this project were supplied by [_Politecnico di Milano_](https://www.polimi.it) in collaboration with their rights' holders.
