package nutritious.prog.utils;

import nutritious.prog.main.Game;

import java.awt.geom.Rectangle2D;

public class HelperMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
        if(!IsSolid(x, y, levelData))                                  // top left corner of our hitbox
            if(!IsSolid(x+width, y+height, levelData))           // bottom right corner of our hitbox
                if(!IsSolid(x+width, y, levelData))                 // top right corner of our hitbox
                    if(!IsSolid(x, y+height, levelData))            // bottom left corner of our hitbox
                        return true;

        return false;
    }

    private static boolean IsSolid(float x, float y, int [][] levelData){
        //check if we are inside of game boundaries
        if(x < 0 || x >= Game.GAME_WIDTH) return true;
        if(y < 0 || y >= Game.GAME_HEIGHT) return true;

        //we transform our (x,y) position to certain tile index (if tile size is 32 and our x == 100 we are in tile 4)
        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        //we search the array of tiles to find if we are on one
        int value = levelData[(int)yIndex][(int)xIndex];

        //check if value is a tile
        if(value >= 48 || value < 0 || value != 11) return true;        //11 is transparent tile

        return false;
    }

    //used when can move here returns false, but we still have space to move to wall
    //that is a fraction of our hitbox
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        //calculating on what tile are we at the moment
        int currentTile = (int)(hitbox.x / Game.TILES_SIZE);
        if(xSpeed > 0) {
            //wall is on the right
            int tileXPosition = currentTile * Game.TILES_SIZE; // currents tile left border in px
            int xOffset = (int)(Game.TILES_SIZE - hitbox.width); // free space around hitbox
            //we add these values to move our hitbox as far to the right as possible
            return tileXPosition + xOffset - 1; // -1 is for our hitbox to be right next to wall not in it
        } else {
            //wall is on the left
            return currentTile * Game.TILES_SIZE; // returns position in px of the very beginning of the tile we are in
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveTheFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int)(hitbox.y / Game.TILES_SIZE);
        if(airSpeed > 0) {
            //falling - touching floor
            int tileYPosition = currentTile * Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitbox.height);
            return tileYPosition + yOffset - 1;
        } else {
            //jumping - touching roof
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnTheFloor(Rectangle2D.Float hitbox, int[][] levelData) {
        //Check the one pixel below bottom left and bottom right corner
        //If both of those are not solid we are in the air
        if(!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData)) {
            if(!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData)) {
                return false;
            }
        }
        return true;
    }
}
