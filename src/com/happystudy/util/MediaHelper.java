package com.happystudy.util;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.*;
import java.util.Properties;

/**
 * Created by qi on 2017/5/27.
 */
public class MediaHelper {
    private static  String HappyMusic ;
    private static  String UnHappyMusic;
    static
    {
        try {
            Properties properties = new Properties();
            InputStream inputStream = MediaHelper.class
                    .getResourceAsStream("/media.properties");
            properties.load(new InputStreamReader(inputStream,"UTF-8"));
            HappyMusic = properties.getProperty("happyMusic");
            UnHappyMusic = properties.getProperty("unhappyMusic");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playHappyMusic()
    {
        playMusic(HappyMusic);
    }

    public static void playUnHappyMusic()
    {
        playMusic(UnHappyMusic);
    }

    private static void playMusic(String fileName)
    {
        try
        {
            File file = new File(fileName);
            if(file.exists())
                System.out.println("file exist");
            InputStream inputStream = new FileInputStream(fileName);
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        MediaHelper.playHappyMusic();;
    }

}
