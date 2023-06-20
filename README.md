
# [Walter the Rabbit NFT Generator](https://nobilitytoken.com)

This project is used to generate our custom Walter the Rabbit NFT Collection




## Generate Video

### Iterate on images at 2 frames per second

    ffmpeg -framerate 2 -i %d.png -vf "scale=600x600" -c:v libx264 -r 30 output2.mp4

### With zoom & pan

    ffmpeg -framerate 60 -i %d.png -vf "zoompan=z='min(zoom+0.0015,5)':d=20, scale=600x600" -c:v libx264 generated.mp4
    
    // Zoom to the center instead of top left (scale should go after other filters)
    ffmpeg -framerate 60 -i %d.png -vf "scale=600:-2,zoompan=z='min(zoom+0.0015,5)':d=20:x='iw/2-(iw/zoom/2)':y='ih/2-(ih/zoom/2)" -c:v libx264 test2.mp4

custom size 600x600
d: 20 - how many frames it generates for each original frame
y,x to center the zoom

### Add watermark
Centered

    ffmpeg -i test3.mp4 -i logoWatermark.png -filter_complex "overlay=(W-w)/2:(H-h)/2" -codec:a copy output.mp4

Top Right with 5px padding
    
    ffmpeg -i test3.mp4 -i logoWatermark.png -filter_complex "overlay=main_w-overlay_w-5:5" -codec:a copy output.mp4

## Run Locally

### Requirements
- Java JDK 16+
- Gradle 7+

### Clone the project

```bash
  git clone https://github.com/Nobility-Token/nft-generator.git
```

### Open the project

- Open your favorite Java IDE such as IntelliJ
- Open the project using the `build.gradle` file

### File structure
Place your images in the following folder structure
```
Root Directory
|-output
|- -images
|- -metadata
|-images
|- -1.backgrounds
|- -2.glow
|- -3.sword
|- -4.akimbo
|- -5.body
|- -6.breastplate
|- -7.pauldron
|- -8.helmet
|- -9.front
```
The only files that are important in this file structure are `output` and `images`.
If you change the amount of components in the images directory, be sure to change the field `totalLayers` in `Generator.java`


## Authors

- [@Patrity (Tony Costanzo)](https://www.github.com/Patrity)
- [@rmcmk](https://github.com/rmcmk)
