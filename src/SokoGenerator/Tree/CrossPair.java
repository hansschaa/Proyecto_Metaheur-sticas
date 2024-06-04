/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SokoGenerator.Tree;

import java.util.Objects;

/**
 *
 * @author Hans
 */
public class CrossPair {
    public Pair pair;
    public Pair dir;
    

    public CrossPair(Pair pair, Pair dir) {
        this.pair = pair;
        this.dir = dir;
    }
    
    public void Print(){
        System.out.println("Pair: " + pair.i + "," + pair.j);
        System.out.println("Dir: " + dir.i + "," + dir.j);
    }   

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrossPair that = (CrossPair) o;
        return Objects.equals(pair, that.pair) &&
               Objects.equals(dir, that.dir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pair, dir);
    }
}
