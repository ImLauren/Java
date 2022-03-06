package com.company;

public class Triangle {
    int side1, side2, side3;
    public Triangle(int a, int b, int c){
        side1 = a;
        side2 = b;
        side3 = c;
    }

    void Sides(){
        System.out.println("This is a Triangle with sides of length"+side1+","+side2+","+side3);
    }

    int getLongestSide(){
        int longest;
        if (side1>side2){
            longest = side1;
        }else{
            longest = side2;
        }
        if(side3>longest){
            longest = side3;
        }
        return longest;
    }

}
