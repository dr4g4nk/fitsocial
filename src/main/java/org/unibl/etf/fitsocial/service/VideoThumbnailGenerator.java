package org.unibl.etf.fitsocial.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class VideoThumbnailGenerator {

    /**
     * Generiše thumbnail iz video fajla koristeći FFmpeg.
     *
     * @param videoFile         Ulazni video fajl (lokalni put)
     * @param outputDirectory   Folder u koji se sprema thumbnail slika
     * @param timestampSeconds  Vrijeme (u sekundama) iz kojeg se uzima frejm
     * @return Apsolutna putanja do generisane slike (String)
     * @throws IOException ako dođe do greške tokom procesa
     * @throws InterruptedException ako se proces prekine
     */
    public static String generateThumbnail(File videoFile, File outputDirectory, int timestampSeconds)
            throws IOException, InterruptedException {

        if (!videoFile.exists()) {
            throw new IllegalArgumentException("Video fajl ne postoji: " + videoFile.getAbsolutePath());
        }

        if (!outputDirectory.exists()) {
            if (!outputDirectory.mkdirs()) {
                throw new IOException("Ne mogu kreirati direktorijum: " + outputDirectory.getAbsolutePath());
            }
        }

        // Kreiraj jedinstveno ime za thumbnail
        String outputFileName = videoFile.getName() + ".jpg";
        File outputImageFile = new File(outputDirectory, outputFileName);

        // Build FFmpeg komandu
        ProcessBuilder builder = new ProcessBuilder(
                "ffmpeg",
                "-y",
                "-ss", String.format("00:00:%02d", timestampSeconds),
                "-i", videoFile.getAbsolutePath(),
                "-vframes", "1",
                "-q:v", "2",
                outputImageFile.getAbsolutePath()
        );

        builder.redirectErrorStream(true);
        Process process = builder.start();

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg nije uspio (exit code: " + exitCode + ")");
        }

        return outputImageFile.getAbsolutePath();
    }
}