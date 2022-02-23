package edu.uob;

import static edu.uob.Triangle.count;

public class Shapes {

  // TODO use this class as then entry point; play around with your shapes, etc
  public static void main(String[] args) {
    // write your code here

    //creat array
    TwoDimensionalShape[] arr = new TwoDimensionalShape[100];
    for (int i = 0; i < 100; i++) {
      double n = Math.random();
      if (n < 0.3) {
        arr[i] = new Circle(i + 1);
      } else if (n < 0.6) {
        arr[i] = new Rectangle(i / 2 + 1, i * 3 / 4 + 1);
      } else {
        arr[i] = new Triangle(i * 2 + 6 - 5, i / 2 + 1, i + 1);
      }
    }

    int cnt = 0;
    for (int i = 0; i < 100; i++) {
      System.out.println(arr[i].toString());
      if (arr[i] instanceof MultiVariantShape) {
        cnt++;
      }
    }

    System.out.println("There are " + cnt + " Triangles");
    System.out.println("In Triangle class, there are " +count+" Triangles");
    System.out.println();

    //若不是triangle会红字warning
    TwoDimensionalShape firstShape = arr[10];
    // Down-cast the shape into a triangle
    Triangle firstTriangle = (Triangle)firstShape;
    TriangleVariant variant = firstTriangle.getVariant();


    double area;
    int per;

    /*
    //circle
    Circle cir =new Circle(2);
    area = cir.calculateArea();
    per = cir.calculatePerimeterLength();
    System.out.println(cir.toString());
    System.out.println("Area = "+area);
    System.out.println("Perimeter = "+per);
    if(cir instanceof MultiVariantShape) {System.out.println("This shape has multiple variants");}
    else {System.out.println("This shape has only one variant");}
    System.out.println();

    //Rectangle
    Rectangle rec = new Rectangle(3, 4);
    area = rec.calculateArea();
    per = rec.calculatePerimeterLength();
    System.out.println(rec.toString());
    System.out.println("Area = "+area);
    System.out.println("Perimeter = "+per);
    if(rec instanceof MultiVariantShape) {System.out.println("This shape has multiple variants");}
    else {System.out.println("This shape has only one variant");}
    System.out.println();
    */


    //Triangle
    Triangle tri = new Triangle(5,5,5);
    area = tri.calculateArea();
    per = tri.calculatePerimeterLength();
    System.out.println(tri.toString());
    System.out.println("Area = "+area);
    System.out.println("Perimeter = "+per);
    TriangleVariant var = tri.getVariant();
    if(tri instanceof MultiVariantShape) {System.out.println("This shape has multiple variants");}
    else {
      System.out.println("This shape has only one variant");
      System.out.println(var);
    }

  }
}
