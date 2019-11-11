/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.util.Random;
import java.util.Stack;


public class AI {
    private int startSearchX = 0;
    private int startSearchY = 0;
    private int lastX = 0;
    private int lastY = 0;
    private int x = 0;
    private int y = 0;
    private boolean lastGuessHit = false;
    public Stack<Integer> stack = new Stack<Integer>();
    private Random gen = new Random();
    private int lastDir = 0;

    /**
     * Constructor
     */
    public AI() {
        this.generate();
    }

    /**
     * 
     */
    public void reset() {
        startSearchX = 0;
        startSearchY = 0;
        lastX = 0;
        lastY = 0;
        x = 0;
        y = 0;
        lastGuessHit = false;
        stack.empty();
        lastDir = 0;
        generate();
    }

    /**
     *
     * @return the X coordinate of the next space the PC wants to guess
     */
    public int nextX() {
        if (x == lastX && y == lastY) {
            this.generate();
        }
        return x;
    }

    /**
     *
     * @return the Y coordinate of the next space the PC wants to guess
     */
    public int nextY() {
        if (x == lastX && y == lastY) {
            this.generate();
        }
        return y;
    }

    /**
     * Generates a new X coordinate and Y coordinate for the PC to guess
     * 
     * Follows this general formula:
     *  Pick random spot
     *  If hit, search nearby spots for remainder of ship until a ship is destroyed or out of places to search
     *  Repeat
     */
    public void generate() {
        //If no boat found yet, pick random coordinate
        if (!lastGuessHit && stack.isEmpty()) {
            x = gen.nextInt(10);
            y = gen.nextInt(10);
            return;
        }

        //If miss space during adjacent space search
        if (!lastGuessHit && !stack.isEmpty()) {
            lastX = startSearchX;
            x = lastX;
            lastY = startSearchY;
            y = lastY;
            tryToMove();
            return;
        }
        //If ship was found in lastHit then try to move in next queued direction
        if (lastGuessHit && !stack.isEmpty()) {
            tryToMove();
            return;
        }

        if (lastGuessHit && stack.isEmpty()) {
            //Pick random direction 
            int dir = gen.nextInt(4);
            startSearchX = lastX;
            startSearchY = lastY;
            //Add all directions to stack in random order
            int i = 4;
            while (i > 0) {
                stack.push(dir);
                dir++;
                if (dir == 4)
                    dir = 0;
                i--;
            }

            tryToMove();
            return;

        }


    }


    /**
     * While there are still queued directions to search AND we can't move in current direction,
     * make current direction = next queued direction
     * If no more queued directions, generate random coordinates
     */
        public void tryToMove() {
        int dir = (int) stack.pop();
        while (!move(dir)) {
            if (stack.isEmpty()) {
                x = gen.nextInt(10);
                y = gen.nextInt(10);
                lastDir = dir;
                return;
            } else {
                dir = (int) stack.pop();
            }
        }
        lastDir = dir;
    }

    /**
     * Tries to move in specified direction
     * 
     * @param dir the specified direction to move
     * @return true if able to move in that direction, false otherwise
     */
    public boolean move(int dir) {
        //Move North
        if (dir == 0) {
            if (y != 0) {
                x = lastX;
                y = lastY - 1;
                return true;
            } else {
                return false;
            }
        }

        //Move East
        if (dir == 1) {
            if (x != 9) {
                x = lastX + 1;
                y = lastY;
                return true;
            } else {
                return false;
            }
        }

        //Move South
        if (dir == 2) {
            if (y != 9) {
                x = lastX;
                y = lastY + 1;
                return true;
            } else {
                return false;
            }
        }

        //dir == 3. Move West
        if (x != 0) {
            x = lastX - 1;
            y = lastY;
            return true;
        } else {
            return false;

        }
    }

    /**
     * Plan next decision depending on how well previous guess performed
     *
     * @param getHit whether or not last guess hit
     * @param getDestroy whether or not last guess destroyed a ship
     */
    public void feedback(boolean getHit, boolean getDestroy) {
        if (getDestroy) {
            stack.clear();
            lastX = 0;
            lastY = 0;
            x = 0;
            y = 0;
            lastDir = 0;
            lastGuessHit = false;
            return;
        }

        lastX = x;
        lastY = y;

        //If hit and we can keep going in the direction we just came from, keep searching in that direction
        if (getHit) {
            lastGuessHit = true;
            if (!stack.isEmpty()) {
                int dir = lastDir;
                if (dir == 0 && lastY == 0) {
                    return;
                }
                if (dir == 1 && lastX == 9) {
                    return;
                }
                if (dir == 2 && lastY == 9) {
                    return;
                }
                if (dir == 3 && lastX == 0) {
                    return;
                }
                //Add direction to keep searching in current direction
                stack.push(dir);
            }
            return;
        }
        else {
            lastGuessHit = false;
        }
    }

}
