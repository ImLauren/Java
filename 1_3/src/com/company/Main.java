package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Triangle testTriangle = new Triangle(4, 5, 7);
        int longestSide = testTriangle.getLongestSide();
        testTriangle.Sides();
        System.out.println("The longest side of the triangle is " + longestSide);
    }
}
