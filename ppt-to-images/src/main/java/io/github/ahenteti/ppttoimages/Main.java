package io.github.ahenteti.ppttoimages;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path pptPath = getPptPath(args);
        try (XMLSlideShow ppt = new XMLSlideShow(Files.newInputStream(pptPath))) {
            for (XSLFSlide slide : ppt.getSlides()) {
                BufferedImage image = from(slide, ppt);
                writeToFile(image, pptPath, slide);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(BufferedImage image, Path pptPath, XSLFSlide slide) throws IOException {
        String pptFileName = pptPath.getFileName().toString();
        String pptFileNameWithoutExt = FilenameUtils.removeExtension(pptFileName);
        String slideImageFileName = pptFileNameWithoutExt + ".slide-" + slide.getSlideNumber() + ".png";
        FileOutputStream imageOutputStream = new FileOutputStream(slideImageFileName);
        javax.imageio.ImageIO.write(image, "png", imageOutputStream);
    }

    private static BufferedImage from(XSLFSlide slide, XMLSlideShow ppt) {
        Dimension pageSize = ppt.getPageSize();
        BufferedImage image = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(Color.white);
        graphics.fill(new Rectangle2D.Float(0, 0, pageSize.width, pageSize.height));
        slide.draw(graphics);
        return image;
    }

    private static Path getPptPath(String[] args) {
        if (args.length != 1) {
            throw new RuntimeException("ppt path is mandatory");
        }
        return Paths.get(args[0]);
    }
}
