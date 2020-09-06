package io.github.ahenteti.pptnotes.extractor;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path pptPath = getPptPath(args);
        try (XMLSlideShow ppt = new XMLSlideShow(Files.newInputStream(pptPath))) {
            for (XSLFSlide slide : ppt.getSlides()) {
                StringBuilder noteAstText = new StringBuilder();
                for (XSLFShape note : slide.getNotes()) {
                    if (note instanceof XSLFTextShape) {
                        XSLFTextShape text = (XSLFTextShape) note;
                        for (XSLFTextParagraph paragraph : text.getTextParagraphs()) {
                            noteAstText.append(paragraph.getText());
                            noteAstText.append("\n");
                        }
                    }
                }
                String pptFileName = pptPath.getFileName().toString();
                String pptFileNameWithoutExt = FilenameUtils.removeExtension(pptFileName);
                String slideNotesFileName = pptFileNameWithoutExt + ".slide-" + slide.getSlideNumber() + ".txt";
                Path slideNotesPath = Paths.get(slideNotesFileName);
                try (BufferedWriter writer = Files.newBufferedWriter(slideNotesPath)) {
                    writer.write(noteAstText.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Path getPptPath(String[] args) {
        if (args.length != 1) {
            throw new RuntimeException("ppt path is mandatory");
        }
        return Paths.get(args[0]);
    }
}
