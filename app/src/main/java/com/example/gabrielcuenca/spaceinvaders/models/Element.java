package com.example.gabrielcuenca.spaceinvaders.models;

abstract class Element {

    private int lifePoints;
    private String texture;
    private int damage;

    public Element(int lifePoints, String texture, int damage) {
        this.lifePoints = lifePoints;
        this.texture = texture;
        this.damage = damage;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
