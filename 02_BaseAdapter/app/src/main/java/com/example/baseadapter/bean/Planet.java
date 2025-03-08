package com.example.baseadapter.bean;

import com.example.baseadapter.R;

import java.util.ArrayList;
import java.util.List;

public class Planet {
    public  int image;
    public  String name;
    public  String desc;

    public Planet(int image, String name, String desc) {
        this.image = image;
        this.name = name;
        this.desc = desc;
    }
    private static int[] icnArray={R.drawable.baseline_1k_plus_24,R.drawable.baseline_bluetooth_24,R.drawable.baseline_1k_plus_24,R.drawable.baseline_1k_plus_24,R.drawable.baseline_bluetooth_24};
private static String[] nameArray={"Earth","Mars","dsdsd","qqqqq","bbbbb"};
private static String[] descArray={"Even the most powerful supercomputers are useless if they cannot access the data and the algorithms they need to operate. The Internet is the world's largest supercomputer, and it is accessible to everyone. It is the ultimate source of information and the key to unlocking the secrets of the universe.",
        " supercomputers are useless if they cannot access the data and the algorithms they need to operate. The Internet is the world's largest supercomputer, and it is accessible to everyone. It is the ultimate source of information and the key to unlocking the secrets of the universe.",
        "Even thepercomputers are useless if they cannot access the data and the algorithms they need to operate. The Internet is the world's largest supercomputer, and it is accessible to everyone. It is the ultimate source of information and the key to unlocking the secrets of the universe.",
        " supercomputers are useless if they cannot access the data and the algorithms they need to operate. The Internet is the world's largest supercomputer, and it is accessible to everyone. It is the ultimate source of information and the key to unlocking the secrets of the universe.",
        "Even thepercomputers are useless if they cannot access the data and the algorithms they need to operate. The Internet is the world's largest supercomputer, and it is accessible to everyone. It is the ultimate source of information and the key to unlocking the secrets of the universe."};

public static List<Planet> getPlanetArray(){
        List<Planet> planets=new ArrayList<>();
        for (int i = 0; i < icnArray.length; i++) {
            planets.add(new Planet(icnArray[i],nameArray[i],descArray[i]));
        }
        return planets;
    }
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
