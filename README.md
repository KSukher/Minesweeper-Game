# Minesweeper-Game

A Java implementation of the classic Minesweeper game. This game features the original GUI grid with countdown timer, randomly generated bombs/flags, and a menu option for new games, saved games and highscores.

Notes for running the Minesweeper game,

- Add the <b>sqlite-jdbc-3.30.1.jar</b> file into the properties if not already done so.

- Before starting, terminate any other Java application that is running on the IDE.

- First start up the server in <b>MineSweeperServer.java</b>, then run the game in <b>MineSweeperStarter.java</b>.

- If testing the highscore feature, or changing the difficulty of the game in general,
  in <b>Settings.java</b> the `BOMB_PROBABILITY` variable can be adjusted down (<b>Easy</b>) or up (<b>Hard</b>).
  (Remember to terminate after saving the change and rerunning the server and game.)
