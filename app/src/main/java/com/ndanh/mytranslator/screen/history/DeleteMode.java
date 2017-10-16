package com.ndanh.mytranslator.screen.history;


import java.util.Observable;

/**
 * Created by ndanh on 5/12/2017.
 */

public class DeleteMode extends Observable {

    private static DeleteMode instance = new DeleteMode ();

    public static boolean isDeleteMode() {
        return instance.isDeleteMode ;
    }

    private boolean isDeleteMode;

    public static DeleteMode getInstance(){
        if(instance == null) instance = new DeleteMode ();
        return instance;
    }

    private DeleteMode() {
        this.isDeleteMode = false;
    }



    public void off(){
        isDeleteMode = false;
        this.setChanged ();
        this.notifyObservers ();
    }
    public void on(){
        isDeleteMode = true;
        this.setChanged ();
        this.notifyObservers ();
    }

    public static void clean() {
        instance = null;
    }
}

