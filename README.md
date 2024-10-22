# Codex Naturalis

Software implementation of the board game ***Codex Naturalis***. Rules available in italian on the
[official website](https://www.craniocreations.it/storage/media/product_downloads/126/1516/CODEX_ITA_Rules_compressed.pdf).

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

## How to run
- In the [deliverables/jar](deliverables/jar) folder there are two jar files, one for the server and one for the client.

- The server can be run with the following command:
    ```shell
    java -jar CodexNaturalisServer.jar
    ```
  The RMI port is 1099 and the socket port is 6666.

- The client can be run with the following command:
    ```shell 
    java -jar CodexNaturalisClient.jar
    ```
    
  Options:
    - -i   &nbsp;&emsp;   the interface [gui/tui]
    - -n   &emsp;   the network connection [socket/rmi]
    - -h   &emsp;   the server host [ipv4]
    
  Examples:
  ```shell 
  java -jar CodexNaturalisClient.jar -i tui -n rmi
  ```
  ```shell 
  java -jar CodexNaturalisClient.jar -n socket -h 127.0.0.1 -i gui
  ```

  By default this command runs the GUI and establishes a connection with the server on localhost using socket.

## How to play
In the GUI, after you have selected a card in your hand, you can play it face up by left-clicking or face down by right-clicking.

## Disclaimers
We have tested the jar files on windows and fedora linux.

[_Codex Naturalis_](https://www.craniocreations.it/prodotto/codex-naturalis) is property of [_Cranio Creations_](https://www.craniocreations.it) and all of the copyrighted graphical assets used in this project were supplied by [_Politecnico di Milano_](https://www.polimi.it) in collaboration with their rights' holders.
