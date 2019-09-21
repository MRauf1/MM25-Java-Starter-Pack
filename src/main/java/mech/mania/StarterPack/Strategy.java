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
     * Method to set unit initializations. Run at the beginning of a game, after assigning player numbers.
     * @return An array of {@link UnitSetup} objects which define attack pattern, terrain creation pattern, health, and speed.
     * @see UnitSetup
     */
    public UnitSetup[] getSetup(int playerNum){
        // Default values
        int[][] attackPattern = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0},
                {0, 0, 1, 2, 1, 0, 0},
                {0, 1, 1, 0, 1, 1, 0},
                {0, 0, 1, 2, 1, 0, 0},
                {0, 0, 0, 1, 0, 0, 0},
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
        int health = 4;
        int speed = 4;

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
        System.out.println(isTerrainPresent(gameState, 0, 2));
        System.out.println(isTerrainPresent(gameState, 0, 1));
         */

        Direction[] unitOneDirection = null;
        Direction[] unitTwoDirection = null;

        // Default values
        Decision[] turnResponse = new Decision[myUnits.size()];
        for(int u = 0; u < myUnits.size(); u++) {
            int priority = u + 1;
            Direction[] movementSteps = new Direction[myUnits.get(u).getSpeed()];


            for(int s = 0; s < movementSteps.length; s++) {
                int currentRow = 11 - myUnits.get(priority - 1).getPos().y;
                int currentColumn = myUnits.get(priority - 1).getPos().x;
                List<Direction> availableDirections = getAvailableDirections(gameState, priority, myUnits,
                                unitOneDirection, unitTwoDirection, currentRow, currentColumn);
                int random = (int) (Math.random() * availableDirections.size());

                for(Direction direction : availableDirections) {
                    System.out.println(direction);
                }
                System.out.println(currentRow);
                System.out.println(currentColumn + "\n");
                Direction randomDir = availableDirections.get(random);
                System.out.println(randomDir);
                Position newPosition;
                switch (randomDir) {
                    case UP:
                        System.out.println("Up direction");
                        movementSteps[s] = Direction.UP;
                        newPosition = new Position(currentColumn, 12 - currentRow);
                        myUnits.get(priority - 1).setPos(newPosition);
                        System.out.println("New position: " + myUnits.get(priority - 1).getPos().x
                                    + " " + myUnits.get(priority - 1).getPos().y);
                        break;
                    case DOWN:
                        System.out.println("Down direction");
                        movementSteps[s] = Direction.DOWN;
                        newPosition = new Position(currentColumn, 10 - currentRow);
                        myUnits.get(priority - 1).setPos(newPosition);
                        System.out.println("New position: " + myUnits.get(priority - 1).getPos().x
                                    + " " + myUnits.get(priority - 1).getPos().y);
                        break;
                    case LEFT:
                        System.out.println("Left direction");
                        movementSteps[s] = Direction.LEFT;
                        newPosition = new Position(currentColumn - 1, 11 - currentRow);
                        myUnits.get(priority - 1).setPos(newPosition);
                        System.out.println("New position: " + myUnits.get(priority - 1).getPos().x
                                                + " " + myUnits.get(priority - 1).getPos().y);
                        break;
                    case RIGHT:
                        System.out.println("Right direction");
                        movementSteps[s] = Direction.RIGHT;
                        newPosition = new Position(currentColumn + 1, 11 - currentRow);
                        myUnits.get(priority - 1).setPos(newPosition);
                        System.out.println("New position: " + myUnits.get(priority - 1).getPos().x
                                    + " " + myUnits.get(priority - 1).getPos().y);
                        break;
                    case STAY:
                        movementSteps[s] = Direction.STAY;
                        break;
                }

            }

            if(priority == 1) {
                unitOneDirection = movementSteps;
            } else if(priority == 2) {
                unitTwoDirection = movementSteps;
            }


            // Attack if would damage walls or an enemy unit
            Direction attackDirection = Direction.STAY;
            List<Pair<Position, Integer>> posOfAttack =
                    gameState.getPositionsOfAttackPattern(
                            gameState.getPositionAfterMovement(myUnits.get(u).getPos(), movementSteps),
                            myUnits.get(u).getAttack(), Direction.UP);
            for(Pair p : posOfAttack){
                Position pos = (Position)p.getFirst();
                try {
                    Tile t = gameState.getTiles()[pos.x][pos.y];
                    if (t.getType() != Tile.Type.BLANK || (t.getUnit() != null && t.getUnit().getPlayerNum() != playerNum)) {
                        attackDirection = Direction.UP;
                        break;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    continue;
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
        Tile[][] tiles = gameState.getTiles();
        int oldRow = row;
        row = 11 - column;
        column = oldRow;
        System.out.println("Terrain present " + row);
        System.out.println("Terrain present " + column);
        System.out.println(tiles[row][column].getType());
        return !(tiles[row][column].getType().equals(Tile.Type.BLANK));
    }

    public static List<Position> friendlyMechPositions(GameState gameState, int priority, List<Unit> myUnits,
                                                   Direction[] unitOneMoves, Direction[] unitTwoMoves) {
        Unit unitTwoInitial = myUnits.get(1);
        Unit unitThreeInitial = myUnits.get(2);
        ArrayList<Position> result = new ArrayList<Position>();
        if(priority == 1) {
            result.add(unitTwoInitial.getPos());
            result.add(unitThreeInitial.getPos());
        } else if(priority == 2) {
            Position unitOneFinal = gameState.getPositionAfterMovement(myUnits.get(0).getPos(), unitOneMoves);
            result.add(unitOneFinal);
            result.add(unitThreeInitial.getPos());
        } else if(priority == 3) {
            Position unitOneFinal = gameState.getPositionAfterMovement(myUnits.get(0).getPos(), unitOneMoves);
            Position unitTwoFinal = gameState.getPositionAfterMovement(myUnits.get(1).getPos(), unitTwoMoves);
            result.add(unitOneFinal);
            result.add(unitTwoFinal);
        }

        return result;
    }
    /*
    public static boolean isSafePosition(GameState gameState, int priority, List<Unit> myUnits,
                                         Direction[] unitOneMoves, Direction[] unitTwoMoves, int row, int column) {
        Position[] unsafePositions = friendlyMechPositions(gameState, priority, myUnits, unitOneMoves, unitTwoMoves);
        int rowOne = 11 - unsafePositions[0].y;
        int columnOne = unsafePositions[0].x;
        int rowTwo = 11 - unsafePositions[1].y;
        int columnTwo = unsafePositions[1].x;
        if(!isTerrainPresent(gameState, row, column) && row != rowOne && row != rowTwo && column != columnOne &&
            column != columnTwo) {
            return true;
        }
        return false;
    }
     */

    public static List<Direction> getAvailableDirections(GameState gameState, int priority, List<Unit> myUnits,
                                                         Direction[] unitOneMoves, Direction[] unitTwoMoves,
                                                         int row, int column) {
        int rowOne = -1;
        int columnOne = -1;
        int rowTwo = -1;
        int columnTwo = -1;
        List<Direction> result = new ArrayList<Direction>();
        if(myUnits.size() == 3) {
            List<Position> unsafePositions = friendlyMechPositions(gameState, priority, myUnits, unitOneMoves, unitTwoMoves);
            rowOne = 11 - unsafePositions.get(0).y;
            columnOne = unsafePositions.get(0).x;
            rowTwo = 11 - unsafePositions.get(1).y;
            columnTwo = unsafePositions.get(1).x;
        }
        System.out.println("Mech number " + (priority - 1));
        System.out.println("Current row " + row);
        System.out.println("Current column " + column);
        System.out.println("Friendly mech: " + rowOne + " " + columnOne);
        System.out.println("Friendly mech: " + rowTwo + " " + columnTwo);
        //System.out.println("Checking terrain for UP: " + !isTerrainPresent(gameState, row, column + 1));
        System.out.println("Checking 1st mech for RIGHT: " + (row != rowOne || column + 1 != columnOne));
        System.out.println("Checking 2nd mech for RIGHT: " + (row != rowTwo || column + 1 != columnTwo));
        if(row > 0 && !isTerrainPresent(gameState, row - 1, column) && (row - 1 != rowOne || column != columnOne) &&
                (row - 1 != rowTwo || column != columnTwo)) {
            result.add(Direction.UP);
        }

        if(row < 11 && !isTerrainPresent(gameState, row + 1, column) && (row + 1 != rowOne || column != columnOne) &&
                (row + 1 != rowTwo || column != columnTwo)) {
            result.add(Direction.DOWN);
        }

        if(column > 0 && !isTerrainPresent(gameState, row, column - 1) && (row != rowOne || column - 1 != columnOne) &&
                (row != rowTwo || column - 1 != columnTwo)) {
            result.add(Direction.LEFT);
        }

        if(column < 11 && !isTerrainPresent(gameState, row, column + 1) && (row != rowOne || column + 1 != columnOne) &&
                (row != rowTwo || column + 1 != columnTwo)) {
            result.add(Direction.RIGHT);
        }

        // Move only if there is no other option
        if(result.size() == 0) {
            result.add(Direction.STAY);
        }

        return result;
    }

}
