## Goal of the project  

The aim is to create a minimalist version of Team Fight Tactics using robot instead of champions.  
The project should take about one week and help understanding programming concept in Java,   
including polymorphism and manipulation of graphic interfaces.  

## Basic Game principles  

The main idea behind automatic chess game style is that each opponents has a set of units, each with their 
own behavior and caracteristics. The players can choose their units and position them on a board.  
Then, all the units actions are managed internally, players can't interact at this time.  

The goal is therefore to choose units appropriately and position them the best possible way.  

## Specifics principle linked to this project  

Player will be allocated a given numbers of bolt, allowing them to buy robots in a shop.  
During the game, some interactives games (such as answering questions or best-guessing the result of a match)  
will allow players to gain bolt depending on their results.  

*The goal of a team is either to make the map they play on "Dirty" or "Clean"*  
Each robot can make a cell dirty or clean when they pass on it (depending of their team) or influence other robots.  

## Game architecture  

Hierarchical composition relation : Match <- Rounds <- Turns (A cycle where all robot acted) <- Step (Robot action)  
Between rounds, the interactives games will be played and 

## Project architecture  

As of now, there are 4 main files family, which are :

GameManager.java -> Which manage Robots actions in relation to match/rounds/turns, 
keep track on the map state and call Displayer  

GraphicInterface.java -> Should contains all the methods and attributes related to the display
  
Robot.java (and it's children) -> Should define the methods and attributes of robots

Monde.java -> Manage the map creation and possible settings  
