/*
 * Copyright (C) 2011 divde <divde@free.fr>
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
package fr.free.divde.brasouverts.db.model;

import fr.free.divde.brasouverts.db.check.ModelCheck;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class Image extends DBEntity<Long> {

    private static final long serialVersionUID = 1L;
    private byte[] image;
    private transient BufferedImage usableImage;
    private transient boolean imageModified = false;

    public BufferedImage getImage() {
        if (usableImage == null && image != null) {
            ByteArrayInputStream is = new ByteArrayInputStream(image);
            try {
                usableImage = ImageIO.read(is);
            } catch (IOException ex) {
                Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return usableImage;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        storeImage();
        out.defaultWriteObject();
    }

    public void setImage(BufferedImage image) {
        if (image != usableImage) {
            this.usableImage = image;
            imageModified = true;
        }
    }

    @PrePersist
    @PreUpdate
    private void storeImage() {
        if (imageModified) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                ImageIO.write(usableImage, "png", os);
                image = os.toByteArray();
                imageModified = false;
            } catch (IOException ex) {
                Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String toString() {
        return "fr.free.divde.brasouverts.db.model.Image[ id=" + id + " ]";
    }

    @Override
    public void check(ModelCheck check) {
    }
}
