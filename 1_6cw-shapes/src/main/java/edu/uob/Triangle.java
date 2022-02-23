package edu.uob;

class Triangle extends TwoDimensionalShape implements MultiVariantShape{

  int side1;
  int side2;
  int side3;
  static int count=0;

  public Triangle(int a, int b, int c) {
      side1 = a;
      side2 = b;
      side3 = c;
      count++;
  }

  int getLongestSide() {
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

  double calculateArea() {
    double p = (side1 + side2 + side3)/2;
    double area = Math.sqrt(p*(p-side1)*(p-side2)*(p-side3));
    return area;
  }

  int calculatePerimeterLength() {
    return side1+side2+side3;
  }

  public String toString() {
    return "This is a Triangle with sides of length "+side1+","+side2+","+side3;
  }


  //判断形状
  public TriangleVariant getVariant (){
    long longside = getLongestSide();
    long sides = 0;
    long mul = 0;
    if (longside == side1){
      sides = (long)side2 + (long)side3;
      mul = (long)side2*(long)side2 + (long)side3*(long)side3;
    }else if(longside == side2){
      sides  = (long)side1 + (long)side3;
      mul = (long)side1*(long)side1 + (long)side3*(long)side3;
    }else if(longside == side3){
      sides = (long)side2 + (long)side1;
      mul = (long)side1*(long)side1 + (long)side2*(long)side2;
    }

    TriangleVariant var = TriangleVariant.IMPOSSIBLE;
    if (side1<=0 || side2<=0 || side3<=0){
      var = TriangleVariant.ILLEGAL;   // <0不合法
    } else if(sides>=longside){
      var = TriangleVariant.SCALENE;  //不等边三角形
      if (sides == longside){
        var = TriangleVariant.FLAT;   //平
      }else if(mul == longside*longside){
        var = TriangleVariant.RIGHT;   //直角三角形
      }else if (side1 == side2 && side2 == side3){
        var = TriangleVariant.EQUILATERAL;   //等边三角形
      }else if(side1==side2 || side1==side3 || side2==side3){
        var = TriangleVariant.ISOSCELES;  //等腰三角形
      }
    }

    return var;
  }


  int getPopulation(){
    return count;
  }
}

