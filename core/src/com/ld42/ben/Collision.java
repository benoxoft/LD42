package com.ld42.ben;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Collision {

    public static RectangleMapObject detectCollision(Rectangle rect, MapLayer layer) {
        for (RectangleMapObject rectangleObject : layer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rect, rectangle)) {
                return rectangleObject;
            }
        }
        return null;
    }

    public static RectangleMapObject detectDoorCollision(Rectangle rect, MapLayer doorsLayer) {
        for (RectangleMapObject rectangleObject : doorsLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rectangle, rect)) {
                if(((Boolean) rectangleObject.getProperties().get("closed"))) {
                    return rectangleObject;
                }
            }
        }
        return null;
    }

}
