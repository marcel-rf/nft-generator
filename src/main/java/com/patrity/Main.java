package com.patrity;

public class Main {

    public Main() {
        final Generator generator = new Generator();
        long startTime = System.currentTimeMillis();
        long lastTime;
        for (int i = 1; i <= NFTConfig.TOTAL_TO_GENERATE; i++) {
            lastTime = System.currentTimeMillis();
            generator.createNFT(i);
            long timeTaken = System.currentTimeMillis() - lastTime;
            long currentTime = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("Generated " + i + "/" + NFTConfig.TOTAL_TO_GENERATE + " in " + currentTime + "s (" + timeTaken + "ms)");
        }


        //FIXME this program doesn't end. if system exit is used immediately here, pngs are not generated
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("finished running");
        System.exit(0);
    }

    public static void main(String[] args) {
        new Main();
    }
}
