package com.kasa777.modal;

import java.io.Serializable;

public class GameDashboardModel implements Serializable {
    String name;
    int background,drawable;

    public GameDashboardModel() {
    }

    public GameDashboardModel(String name, int background, int drawable) {
        this.name = name;
        this.background = background;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
