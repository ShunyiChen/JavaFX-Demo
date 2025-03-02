/*
 * Copyright (C) 2016 Shunyi Chen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dockingsoftware.autorepairsystem.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageUtils {

    /**
     * Create new image view.
     * 
     * @param fileName
     * @return 
     */
    public static ImageView createImageView(String fileName) {
        InputStream istream;
        try {
            istream = new FileInputStream(new File("res/image/"+fileName));
            return new ImageView(new Image(istream));
        } catch (FileNotFoundException ex) {
        }
        return null;
    }
}
