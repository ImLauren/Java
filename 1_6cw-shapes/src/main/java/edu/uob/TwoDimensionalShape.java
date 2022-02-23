package edu.uob;

abstract class TwoDimensionalShape {

  public TwoDimensionalShape() {}

  abstract double calculateArea();

  abstract int calculatePerimeterLength();

  //?
  Colour setColour(){
    Colour cor = Colour.RED;
    return cor;
  }

  //?
  Colour getColour(){
    Colour cor = Colour.RED;
    return cor;
  }
}
