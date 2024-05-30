/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Metaheuristics.SA;

import Metaheuristics.Metaheuristics;
import SokoGenerator.GeneratorUtils;

/**
 *
 * @author Hans
 */
public class SABoard {

 
    public int fitness;
    public char[][] board;
    
    public SABoard(){
        this.fitness = -1;
    }
    
    public SABoard Copy(SABoard toCopy) {
        
        toCopy.board = GeneratorUtils.CloneCharArray(this.board);
        toCopy.fitness = this.fitness;
        return toCopy;
    }

    void Show() {
        System.out.println("Fitness: " + fitness);
        System.out.println("Board:");
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
