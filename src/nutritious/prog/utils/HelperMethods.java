package nutritious.prog.utils;

import nutritious.prog.main.Game;

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
}
