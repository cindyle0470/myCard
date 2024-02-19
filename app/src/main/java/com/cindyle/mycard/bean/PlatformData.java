package com.cindyle.mycard.bean;

public class PlatformData {
    public int phy1;
    public int phy2;
    public int pp1;
    public int pp2;
    public int star;
    public int god;
    public int door;
    public int space;
    public int other;

    public PlatformData() {
    }

    public PlatformData(int phy1, int phy2, int pp1, int pp2,int star, int god, int door, int space, int other) {
        this.phy1 = phy1;
        this.phy2 = phy2;
        this.pp1 = pp1;
        this.pp2 = pp2;
        this.star = star;
        this.god = god;
        this.door = door;
        this.space = space;
        this.other = other;
    }

    public void dataClear(){
        this.phy1 = 0;
        this.phy2 = 0;
        this.pp1 = 0;
        this.pp2 = 0;
        this.star = 0;
        this.god = 0;
        this.door = 0;
        this.space = 0;
        this.other = 0;
    }
}
