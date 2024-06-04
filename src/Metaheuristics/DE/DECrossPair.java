/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.DE;

import Metaheuristics.GA.GABoard;
import SokoGenerator.Tree.CrossPair;
import SokoGenerator.Tree.Pair;

/**
 *
 * @author Hans
 */
public class DECrossPair extends CrossPair{
    public GABoard gaBoard;
    public DECrossPair(Pair pair, Pair dir, GABoard gaBoard) {
        super(pair, dir);
        this.gaBoard = gaBoard;
    }
    
}
