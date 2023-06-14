package com.patrity.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AttributeType {
    BACKGROUND_COLOR("./images/0.background.color"),
    BACKGROUND("./images/1.backgrounds"),
//    GLOW("./images/2.glow"),
//    SWORD("./images/3.sword"),
//    AKIMBO("./images/4.akimbo"),
    BODY("./images/3.body"),
//    BREASTPLATE("./images/6.breastplate"),
//    PAULDRON("./images/7.pauldron"),
//    HELMET("./images/8.helmet"),
//    FRONT("./images/9.front")
    ;

    public String folderPath;
}
