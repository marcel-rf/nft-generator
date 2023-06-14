package com.patrity;

import com.patrity.model.Attribute;
import com.patrity.model.AttributeType;
import com.patrity.model.Knight;
import com.patrity.model.Rarity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Generator {
    private final Random random = new Random();
    private final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final Path BASE_OUTPUT_PATH = Paths.get("output");
    private final Set<List<Attribute>> attributesSet = new HashSet();

    public void createNFT(int index) {
        final String baseUrl = "https://nft.nobilitytoken.com/knights/image/";

        List<Attribute> attributes = new ArrayList<>();

        for (int i = 0; i <= NFTConfig.TOTAL_LAYERS - 1; i++) {
            AttributeType currentType = AttributeType.values()[i];
            if (i == 0)
                attributes.add(getAttribute(currentType, null));
            else
                attributes.add(getAttribute(currentType, attributes.get(i-1)));
        }

        // check if there's isn't already another with the same attribute list (same image)
        if (attributesSet.contains(attributes)) {
            System.err.println("repeated attributes for a character, skipping...");
            return;
        }

        System.out.println("*************** adding attributeList " + attributes);
        attributesSet.add(attributes);

        String imageUrl = baseUrl + index + ".png";
        Knight knight = new Knight(index, imageUrl, attributes);

        System.out.println("generate nft image url " + imageUrl);
        generateImage(knight);
        System.out.println("submitted image generation task " + imageUrl);
        generateMetaData(knight);
    }

    public Attribute getAttribute(AttributeType type, Attribute previous) {
        Rarity rarity = Rarity.getRandomRarity();
        File file = getRandomFile(type.folderPath, rarity, type, previous);
        if (file == null) {
            System.err.println("got null random file in folder " + type.folderPath);
            return new Attribute(type, null, null, rarity);
        }
        System.out.println("got random file " + file.getAbsolutePath());
        String fileName = file.getName().substring(2).replace(".png", "");
        return new Attribute(type, fileName, file, rarity);
    }


    private File getRandomFile(String path, Rarity rarity, AttributeType type, Attribute previous) {
        File folder = new File(path);
        List<File> fileList = Arrays.stream(Objects.requireNonNull(folder.listFiles())).collect(Collectors.toList());

        List<File> filtered = fileList.stream().filter((file) -> {
//            System.out.println("checking file " + file.getName());
            String rarityChar = rarity.prefix;
            String fileName = file.getName().toLowerCase();

            //FIXME not sure what this is for
            if (type == AttributeType.BODY && false) {
                String prevType = "ignore";
                if (previous == null || previous.name == null) {
                    System.out.println("null previous attribute or name "+ previous + " path " + path + " rarity " + rarity  + " type " + type);
                } else {
                    // Previous type is 4 digit prefix of name
                    prevType = previous.name.substring(0, 4).toLowerCase();
                    rarityChar = previous.rarity.prefix;
                }
                if (previous != null) {
                    return fileName.startsWith(rarityChar) && (fileName.contains(prevType));
                } else  {
                    return fileName.startsWith(rarityChar);
                }

            } else {
                return fileName.startsWith(rarityChar) || type == AttributeType.BACKGROUND_COLOR;
            }
        }).collect(Collectors.toList());

        if (filtered.size() == 0) {
            System.err.println("no files found that match filters for path " + path + " rarity " + rarity);
            return null;
        }

        return filtered.get(random.nextInt(filtered.size()));
    }

    @SneakyThrows
    private void generateImage(Knight knight) {
        service.submit(() -> {
            System.out.println("starting image generating for " + knight.image);
            List<BufferedImage> imgList = getAttributeImages(knight);
            BufferedImage combined = new BufferedImage(NFTConfig.IMAGE_WIDTH, NFTConfig.IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics gfx = combined.getGraphics();

            imgList.forEach((img) -> {
//                System.out.println("combining image " + img.toString());
                gfx.drawImage(img, 0, 0, null);
            });

            try {
                Path path = BASE_OUTPUT_PATH.resolve("images");
                if (!Files.exists(path)) Files.createDirectories(path);
                File output = path.resolve(knight.id + ".png").toFile();
                ImageIO.write(combined, "PNG", output);

                System.out.println("wrote png file to " + output.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("FAILED TO write image " + e.getMessage());
            }
        });
    }

    private List<BufferedImage> getAttributeImages(Knight knight) {
        List<BufferedImage> imgList = new ArrayList<>();
        for (int i = 0; i <= AttributeType.values().length - 1; i++) {
            Attribute attribute = knight.attributes.get(i);
            File file = attribute.file;
            System.out.println("checking image for i" + i + " attr " +  attribute.name + " file " + file + " rar "  + attribute.rarity);
            if (file != null) {
                System.out.println("adding image " + file.getPath());
                try {
                    imgList.add(ImageIO.read(file));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("NULL file for attr " + attribute + " item " + knight.toString());
            }
        }
//        System.out.println("return list of images from attributes " + imgList.size());
        return imgList;
    }

    @SneakyThrows
    private void generateMetaData(Knight knight) {
        Path path = BASE_OUTPUT_PATH.resolve("metadata");
        if (!Files.exists(path)) Files.createDirectories(path);
        Utils.mapper.writeValue(path.resolve(knight.id + ".json").toFile(), knight);
    }
}
