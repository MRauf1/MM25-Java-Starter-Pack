package mech.mania.StarterPack;

import mech.mania.API.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A class where contestants will implement their strategy for the MechMania25 Hackathon.
 */
public class Strategy {
    // define any private variables here
    // NOTE: Since the server may be restarted or moved mid-game, you MUST initialize any variables you put here in each of the below constructors.
    //       If the server is restarted or moved, these variables will not have the values you previously set them with.
    //       If you need truly persistent data, you could set up a database and communicate with that from your script

    /**
     * This constructor is called when a game is first started.
     * @param init The initial state of this new game
     * @see GameInit
     */
    public Strategy(GameInit init) {
        // initialize variables here
    }

    /**
     * This constructor is called if/when the server restarts in the middle of a game
     * @param state the current state of the game
     * @see GameState
     */
    public Strategy(GameState state) {
        // initialize variables here
    }

    /**
     * int[][] attackPattern = {
     *                 {0, 0, 0, 0, 0, 0, 0},
     *                 {0, 0, 0, 0, 0, 0, 0},
     *                 {0, 0, 0, 0, 0, 0, 0},
     *                 {0, 0, 0, 0, 2, 0, 0},
     *                 {0, 0, 0, 0, 2, 0, 0},
     *                 {0, 0, 0, 0, 0, 0, 0},
     *                 {0, 0, 0, 0, 0, 0, 0}
     *         };
     *         boolean[][] terrainPattern = {
     *                 {false, false, false, false, false, false, false},
     *                 {false, false, false, false, false, false, false},
     *                 {false, false, false, false, false, false, false},
     *                 {false, false, false, false, true, false, false},
     *                 {false, false, false, false, true, false, false},
     *                 {false, false, false, false, false, false, false},
     *                 {false, false, false, false, false, false, false}
     *         };
     *         int health = 7;
     *         int speed = 1;
     *
     *         Our previously winning combo except for 4 losses.
     *         Keep the attack as default one.
     *
     *         Version: f94db48b-e2f4-4e16-a8c7-74805df8d0c8
     *
     *
     *         Version 2: cce50df3-3831-48f3-9efe-25a2827a7a06
     */


    /**
     * Method to set unit initializations. Run at the beginning of a game, after assigning player numbers.
     * @return An array of {@link UnitSetup} objects which define attack pattern, terrain creation pattern, health, and speed.
     * @see UnitSetup
     */
    public UnitSetup[] getSetup(int playerNum){
        // Default values
        int[][] attackPattern = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        };
        boolean[][] terrainPattern = {
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false}
        };
        int health = 8;
        int speed = 3;

        UnitSetup unit1;
        UnitSetup unit2;
        UnitSetup unit3;
        if(playerNum == 1) {
            // Define units if player 1
            unit1 = new UnitSetup(attackPattern, terrainPattern, health, speed, 1);
            unit2 = new UnitSetup(attackPattern, terrainPattern, health, speed, 2);
            unit3 = new UnitSetup(attackPattern, terrainPattern, health, speed, 3);
        }
        else{
            // Define units if player 2
            unit1 = new UnitSetup(attackPattern, terrainPattern, health, speed, 4);
            unit2 = new UnitSetup(attackPattern, terrainPattern, health, speed, 5);
            unit3 = new UnitSetup(attackPattern, terrainPattern, health, speed, 6);
        }

        UnitSetup[] unitSetup = {unit1, unit2, unit3};
        return unitSetup;
    }
/////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a List of Direction arrays, one for each unit still alive, that contains
     * the optimal directions for attack; namely those without friendlies or out of bounds
     * chars
     * @param gameState A game state variable
     * @return A list of direction arrays
     */
    public List<Direction[]> possibleAttacks(GameState gameState) {

        List<Direction[]> allAttacks = new ArrayList<Direction[]>(); // ret list

        // has every direction possible
        List<Direction> dirs = new ArrayList<Direction>();

        dirs = this.fillDirList(dirs);

        int playerNum = gameState.getPlayerNum();
        List<Unit> myUnits = gameState.getPlayerUnits(playerNum);
        List<Unit> enemyUnits = gameState.getPlayerUnits(playerNum == 1 ? 2 : 1);

        for(int u = 0; u < myUnits.size(); u++) {
            Position unitPos = myUnits.get(u).getPos();
            int x = unitPos.x;
            int y = unitPos.y;

            // Checks corners, and then end points to possibly eliminate bad targets

            if(this.checkPosition(new Position(x+1, y+1), myUnits) == -1) {
                dirs.remove(Direction.UP);
                dirs.remove(Direction.LEFT);
            }

            else if(this.checkPosition(new Position(x, y+2), myUnits) == -1) { dirs.remove(Direction.UP); }

            else if(this.checkPosition(new Position(x-2, y), myUnits) == -1) { dirs.remove(Direction.LEFT); }


            if(this.checkPosition(new Position(x-1, y-1), myUnits) == -1) {
                dirs.remove(Direction.RIGHT);
                dirs.remove(Direction.DOWN);
            }

            else if(this.checkPosition(new Position(x+2, y), myUnits) == -1) { dirs.remove(Direction.RIGHT); }

            else if(this.checkPosition(new Position(x, y-2), myUnits) == -1) { dirs.remove(Direction.DOWN); }

            allAttacks.add(this.convertListArray(dirs)); // adds the filtered array of directions to the list
            dirs = this.fillDirList(dirs); // idk about that, should fill up the list again
        }

        return allAttacks;

    }


    /**
     * Checks the tile at the passed position, returns 0 if tile is clear,
     * -1 for ally or out of bounds
     * @param pos A passed Position object
     * @param myUnits A list of this player's units
     * @return 0 if the tile does not have a friendly unit or is out of bounds, else, -1
     */
    public int checkPosition(Position pos, List<Unit> myUnits) {
        if(pos.x > 11 || pos.x < 0 || pos.y > 11 || pos.y < 0) { return -1; }

        for(Unit u : myUnits) {
            if(pos.x == u.getPos().x || pos.y == u.getPos().y) { return -1; }
        }


        return 0;
    }

    /**
     * Prototype checkPosition method- returns value > 0 for enemy on tile
     * @param pos Given Position object
     * @param myUnits A list of friendly units
     * @param enemyUnits A list of enemy units
     * @return An integer describing the entity on the given position
     */
    public int checkPosition(Position pos, List<Unit> myUnits, List<Unit> enemyUnits) {
        if(pos.x > 11 || pos.x < 0 || pos.y > 11 || pos.y < 0) { return -1; }

        for(Unit u : myUnits) {
            if(pos.x == u.getPos().x || pos.y == u.getPos().y) { return -1; }
        }

        int enemyCount = 0;
        for(Unit e : enemyUnits) {
            Position enemyPos = e.getPos();
            int eX = enemyPos.x;
            int eY = enemyPos.y;

            if(eX == pos.x && eY == pos.y) { enemyCount++; }
        }

        return enemyCount;
    }

    /**
     * List to array
     * @param dirs List of directions
     * @return Array of directions
     */
    public Direction[] convertListArray(List<Direction> dirs) {

        Direction[] arrDirs = new Direction[dirs.size()];
        int i = 0;
        for(Direction dir : dirs) {
            arrDirs[i] = dir;
            i++;
        }

        return arrDirs;
    }

    /**
     * Fills a list of directions with all possible directions
     * @param emptyList An empty direction list
     * @return A full direction list
     */
    public List<Direction> fillDirList(List<Direction> emptyList) {
        emptyList.add(Direction.UP);
        emptyList.add(Direction.DOWN);
        emptyList.add(Direction.LEFT);
        emptyList.add(Direction.RIGHT);

        return emptyList;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method to implement the competitors strategy in the next turn of the game. This is where competitors should be
     * putting most of their code.
     * @param gameState An object recording the current state of the game.
     * @return An object representing the actions to execute this turn. Includes the movement and attack directions
     * for each unit and the priorities (order) in which to execute them.
     * @see Decision
     */
    public Decision[] doTurn(GameState gameState){

        // TODO:
        // Function that returns the board with positions not to go to
        // Code for randomized movement
        // Function that decides the direction of the attack based on the map
        // Update values to account for shrinking board

        // Function that returns the board with positions not to go to
        // - getTiles() doesn't include friendly/enemy mechs

        int playerNum = gameState.getPlayerNum();
        List<Unit> myUnits = gameState.getPlayerUnits(playerNum);
        List<Unit> enemyUnits = gameState.getPlayerUnits(playerNum == 1 ? 2 : 1);
        System.out.println("Total num of my units: " + myUnits.size());
        /*
        // For testing
        Tile[][] tiles = gameState.getTiles();
        for(Tile[] array : tiles) {
            for(Tile tile : array) {
                System.out.println(tile.getType());
            }
            System.out.println("New row");
        }
        */

        Direction[] unitOneDirection = null;
        Direction[] unitTwoDirection = null;
        Position initUnitOne = null;
        Position initUnitTwo = null;

        // Default values
        Decision[] turnResponse = new Decision[myUnits.size()];
        for(int u = 0; u < myUnits.size(); u++) {
            int priority = u + 1;
            Direction[] movementSteps = new Direction[myUnits.get(u).getSpeed()];

            if(priority == 1) {
                initUnitOne = myUnits.get(0).getPos();
            } else if(priority == 2) {
                initUnitTwo = myUnits.get(1).getPos();
            }

            for(int s = 0; s < movementSteps.length; s++) {
                System.out.println("Init in for loop start position " + myUnits.get(priority - 1).getPos().x + " " + myUnits.get(priority - 1).getPos().y);
                int currentRow = 11 - myUnits.get(priority - 1).getPos().y;
                int currentColumn = myUnits.get(priority - 1).getPos().x;
                List<Direction> availableDirections = getAvailableDirections(gameState, initUnitOne, initUnitTwo, priority, myUnits,
                                unitOneDirection, unitTwoDirection, currentRow, currentColumn);
                int random = (int) (Math.random() * availableDirections.size());

                //for(Direction direction : availableDirections) {
                //    System.out.println(direction);
                //}
                System.out.println(currentRow);
                System.out.println(currentColumn);
                Direction randomDir = availableDirections.get(random);
                //System.out.println(randomDir);
                Position newPosition;
                switch (randomDir) {
                    case UP:
                        System.out.println("Up direction\n");
                        movementSteps[s] = Direction.UP;
                        newPosition = new Position(currentColumn, 12 - currentRow);
                        myUnits.get(priority - 1).setPos(newPosition);
                        //System.out.println("New position: " + myUnits.get(priority - 1).getPos().x
                        //            + " " + myUnits.get(priority - 1).getPos().y);
                        break;
                    case DOWN:
                        System.out.println("Down direction");
                        movementSteps[s] = Direction.DOWN;
                        newPosition = new Position(currentColumn, 10 - currentRow);
                        myUnits.get(priority - 1).setPos(newPosition);
                        //System.out.println("New position: " + myUnits.get(priority - 1).getPos().x
                        //            + " " + myUnits.get(priority - 1).getPos().y);
                        break;
                    case LEFT:
                        System.out.println("Left direction");
                        movementSteps[s] = Direction.LEFT;
                        newPosition = new Position(currentColumn - 1, 11 - currentRow);
                        myUnits.get(priority - 1).setPos(newPosition);
                        //System.out.println("New position: " + myUnits.get(priority - 1).getPos().x
                        //                        + " " + myUnits.get(priority - 1).getPos().y);
                        break;
                    case RIGHT:
                        System.out.println("Right direction");
                        movementSteps[s] = Direction.RIGHT;
                        newPosition = new Position(currentColumn + 1, 11 - currentRow);
                        myUnits.get(priority - 1).setPos(newPosition);
                        //System.out.println("New position: " + myUnits.get(priority - 1).getPos().x
                        //            + " " + myUnits.get(priority - 1).getPos().y);
                        break;
                    case STAY:
                        movementSteps[s] = Direction.STAY;
                        break;
                }

            }

            if(priority == 1) {
                unitOneDirection = movementSteps;
                //for(Direction direction : unitOneDirection) {
                //    System.out.println(direction);
                //}
            } else if(priority == 2) {
                unitTwoDirection = movementSteps;
            }

            int randomFriendlyFireBuffer = (int) (Math.random() * 3) + 9;
            if(gameState.getTurnsTaken() < randomFriendlyFireBuffer) {
                turnResponse[u] = new Decision(priority, movementSteps, Direction.STAY, myUnits.get(u).getId());
                continue;
            }

            Direction attackDirection = Direction.STAY;
            // Skipping STAY direction
            for(int i = 0; i < Direction.values().length - 1; i++) {
                Direction recommendedAttackDirection = checkAttackDirection(gameState, enemyUnits, myUnits,
                                                        movementSteps, u, (Direction.values())[i]);
                if(recommendedAttackDirection != null && Direction.STAY != recommendedAttackDirection) {
                    attackDirection = recommendedAttackDirection;
                    break;
                }
            }


            turnResponse[u] = new Decision(priority, movementSteps, attackDirection, myUnits.get(u).getId());
        }

        return turnResponse;
    }

    /**
     *
     * @param gameState
     * @param row       row and column are in traditional CS sense with (0, 0) being top left
     * @param column    but tiles is set in different format where (0, 0) in traditional sense is (11, 0)
     * @return
     */
    public static boolean isTerrainPresent(GameState gameState, int row, int column) {
        Tile[][] tiles = getTiles(gameState);
        int oldRow = row;
        row = 11 - column;
        column = oldRow;
        //System.out.println("Terrain present " + row);
        //System.out.println("Terrain present " + column);
        //System.out.println(tiles[row][column].getType());
        return !(tiles[row][column].getType().equals(Tile.Type.BLANK));
    }

    public static List<Position> friendlyMechPositions(GameState gameState, Position initUnitOne, Position initUnitTwo, int priority, List<Unit> myUnits,
                                                   Direction[] unitOneMoves, Direction[] unitTwoMoves) {
        Unit unitTwoInitial = myUnits.get(1);
        Unit unitThreeInitial = myUnits.get(2);
        ArrayList<Position> result = new ArrayList<Position>();
        Tile[][] tiles = gameState.getTiles();
        if(priority == 1) {
            result.add(unitTwoInitial.getPos());
            result.add(unitThreeInitial.getPos());
        } else if(priority == 2) {
            System.out.println("Unit 1 start: " + initUnitOne.x + " " + initUnitOne.y);
            Position unitOneFinal = gameState.getPositionAfterMovement(initUnitOne, unitOneMoves);

            tiles[11 - unitOneFinal.y][unitOneFinal.x].setUnit(myUnits.get(0));

            result.add(unitOneFinal);
            result.add(unitThreeInitial.getPos());
            System.out.println("Mech 2's perspective. Mech 1 x: " + result.get(0).x);
            System.out.println("Mech 2's perspective. Mech 1 y: " + result.get(0).y);
        } else if(priority == 3) {
            Position unitOneFinal = gameState.getPositionAfterMovement(initUnitOne, unitOneMoves);
            Position unitTwoFinal = gameState.getPositionAfterMovement(initUnitTwo, unitTwoMoves);

            tiles[11 - unitOneFinal.y][unitOneFinal.x].setUnit(myUnits.get(0));
            tiles[11 - unitTwoFinal.y][unitTwoFinal.x].setUnit(myUnits.get(1));

            result.add(unitOneFinal);
            result.add(unitTwoFinal);
        }

        return result;
    }

    public static List<Direction> getAvailableDirections(GameState gameState, Position initUnitOne, Position initUnitTwo, int priority, List<Unit> myUnits,
                                                         Direction[] unitOneMoves, Direction[] unitTwoMoves,
                                                         int row, int column) {
        int rowOne = -1;
        int columnOne = -1;
        int rowTwo = -1;
        int columnTwo = -1;
        List<Direction> result = new ArrayList<Direction>();
        if(myUnits.size() == 3) {
            List<Position> unsafePositions = friendlyMechPositions(gameState, initUnitOne, initUnitTwo, priority, myUnits, unitOneMoves, unitTwoMoves);
            rowOne = 11 - unsafePositions.get(0).y;
            columnOne = unsafePositions.get(0).x;
            rowTwo = 11 - unsafePositions.get(1).y;
            columnTwo = unsafePositions.get(1).x;
        }

        int padding = 0;
        int turns = gameState.getTurnsTaken();
        if(turns >= 10) {
            padding = 1;
        }
        if(turns >= 15) {
            padding = 2;
        }
        if(turns >= 20) {
            padding = 3;
        }
        if(turns >= 24) {
            padding = 4;
        }
        if(turns >= 27) {
            padding = 5;
        }
        if(turns >= 28) {
            padding = 6;
        }

        System.out.println("Mech number " + (priority - 1));
        System.out.println("Current row " + row);
        System.out.println("Current column " + column);
        System.out.println("Friendly mech: " + rowOne + " " + columnOne);
        System.out.println("Friendly mech: " + rowTwo + " " + columnTwo);
        int myTeamNum = 1;
        if(myUnits.get(0).getId() == 4 || myUnits.get(0).getId() == 5 || myUnits.get(0).getId() == 6) {
            myTeamNum = 2;
        }

        //System.out.println("Checking terrain for UP: " + !isTerrainPresent(gameState, row, column + 1));
        //System.out.println("Checking 1st mech for RIGHT: " + (row != rowOne || column + 1 != columnOne));
        //System.out.println("Checking 2nd mech for RIGHT: " + (row != rowTwo || column + 1 != columnTwo));
        if (row > 0 && !isTerrainPresent(gameState, row - 1, column) &&
                    (row - 1 != rowOne || column != columnOne) && (row - 1 != rowTwo || column != columnTwo) && row > padding) {
            result.add(Direction.UP);
        }

        if(row < 11 && !isTerrainPresent(gameState, row + 1, column) && (row + 1 != rowOne || column != columnOne) &&
                    (row + 1 != rowTwo || column != columnTwo) && row < 11 - padding) {
            result.add(Direction.DOWN);
        }

        if (column > 0 && !isTerrainPresent(gameState, row, column - 1) && (row != rowOne || column - 1 != columnOne) &&
                    (row != rowTwo || column - 1 != columnTwo) && column > padding) {
            result.add(Direction.LEFT);
        }

        if (column < 11 && !isTerrainPresent(gameState, row, column + 1) && (row != rowOne || column + 1 != columnOne) &&
                    (row != rowTwo || column + 1 != columnTwo) && column < 11 - padding) {
            result.add(Direction.RIGHT);
        }

        // Move only if there is no other option
        if(result.size() == 0) {
            result.add(Direction.STAY);
        }

        return result;
    }


    // Update the tiles with the values of the border
    public static Tile[][] getTiles(GameState gameState) {
        Tile[][] tiles = gameState.getTiles();
        int turnsTaken = gameState.getTurnsTaken();
        int padding = 0;

        if(turnsTaken >= 15 - 1) {
            updateTilesWithBorders(tiles, padding);
        }
        if(turnsTaken >= 20 - 1) {
            padding = 1;
            updateTilesWithBorders(tiles, padding);
        }
        if(turnsTaken >= 24 - 1) {
            padding = 2;
            updateTilesWithBorders(tiles, padding);
        }
        if(turnsTaken >= 27 - 1) {
            padding = 2;
            updateTilesWithBorders(tiles, padding);
        }
        if(turnsTaken >= 29 - 1) {
            padding = 2;
            updateTilesWithBorders(tiles, padding);
        }
        if(turnsTaken >= 30 - 1) {
            padding = 3;
            updateTilesWithBorders(tiles, padding);
        }

        return tiles;
    }


    // Fill rows and columns with DESTRUCTIBLE given some padding to represent the border
    public static void updateTilesWithBorders(Tile[][] tiles, int padding) {
        // Fill rows
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 11; j++) {
                tiles[(1 - i)*(padding) + (i) * (11 - padding)][j].setType(Tile.Type.DESTRUCTIBLE);
            }
        }

        // Fill columns
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 11; j++) {
                tiles[j][(1 - i)*(padding) + (i) * (11 - padding)].setType(Tile.Type.DESTRUCTIBLE);
            }
        }
    }

    public static Direction checkAttackDirection(GameState gameState, List<Unit> enemyUnits, List<Unit> myUnits,
                                                 Direction[] movementSteps, int u, Direction intendedDirection) {

        // Attack if would damage walls or an enemy unit
        int enemyTeamNum = 1;
        if(enemyUnits.get(0).getId() == 4 || enemyUnits.get(0).getId() == 5 || enemyUnits.get(0).getId() == 6) {
            enemyTeamNum = 2;
        }
        // Get positions of attack
        Direction attackDirection = Direction.STAY;
        List<Pair<Position, Integer>> posOfAttack =
                gameState.getPositionsOfAttackPattern(
                        gameState.getPositionAfterMovement(myUnits.get(u).getPos(), movementSteps),
                        myUnits.get(u).getAttack(), intendedDirection);


        for(Pair p : posOfAttack){
            Position pos = (Position)p.getFirst();
            try {
                Tile t = gameState.getTiles()[pos.x][pos.y];
                if(enemyTeamNum == 1) {
                    if (t.getType() != Tile.Type.BLANK || (t.getUnit().getId() == 1 || t.getUnit().getId() == 2 || t.getUnit().getId() == 3)) {
                        attackDirection = intendedDirection;
                    }
                    if(t.getUnit().getId() == 4 || t.getUnit().getId() == 5 || t.getUnit().getId() == 6) {
                        return Direction.STAY;
                    }
                } else if(enemyTeamNum == 2) {
                    if (t.getType() != Tile.Type.BLANK || (t.getUnit().getId() == 4 || t.getUnit().getId() == 5 || t.getUnit().getId() == 6)) {
                        attackDirection = intendedDirection;
                    }
                    if(t.getUnit().getId() == 1 || t.getUnit().getId() == 2 || t.getUnit().getId() == 3) {
                        return Direction.STAY;
                    }
                }
            } catch(Exception e){
                continue;
            }
        }

        return attackDirection;

    }

}
