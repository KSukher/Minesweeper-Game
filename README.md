# Minesweeper-Game

A Java implementation of the classic Minesweeper game. This game features the original GUI grid with countdown timer, randomly generated bombs/flags, and a menu option for new games, saved games and highscores.

Notes for running the Minesweeper game,

- Add the <b>sqlite-jdbc-3.30.1.jar</b> file into the properties of the project if not already there.

- Before starting, terminate any other Java application that is running on the IDE.

- First start up the server by executing <b>MineSweeperServer.java</b>, then run the game in <b>MineSweeperStarter.java</b>.

- To change the difficulty of the game, adjust the `BOMB_PROBABILITY` variable in <b>Settings.java</b> down (<b>Easy</b>) or up (<b>Hard</b>).
  (Remember to terminate after saving the changes and rerunning the server and game.)
