package com.company;

public class Main {

    public static void main(String[] args) {
        int [][] matrix = {{1,2,3,4},{5,6,7,8},{9,10,11,12}};
        int [] result =  spiralOrder(matrix);
        for (int k=0; k<result.length;k++){
            System.out.println(result[k]);
        }

    }

        public static int[] spiralOrder(int[][] matrix) {
            int lenRow = matrix.length;
            if(lenRow==0){
                int [] result = new int[0];
                return result;
            }
            int lenCol = matrix[0].length;
            int size = lenRow*lenCol;
            int [] result = new int[size];
            int count=0;

            int cnt= 0;
            if (lenRow%2==0){
                cnt = lenRow/2;
            }else {
                cnt = lenRow/2+1;
            }

            for (int k=0; k<cnt; k++){
                for (int j=k; j<lenCol-k;j++){
                    if (count<size){
                        result[count] = matrix[k][j];
                        count++;
                    }
                    //System.out.println(matrix[k][j]);
                }
                for (int i=k+1; i<lenRow-k; i++){
                    if (count<size){
                        result[count] = matrix[i][lenCol-k-1];
                        count++;
                    }
                    //System.out.println(matrix[i][lenCol-k-1]);
                }
                for (int j=lenCol-k-2; j>=k; j--){
                    if (count<size){
                        result[count] = matrix[lenRow-k-1][j];
                        count++;
                    }
                    //System.out.println(matrix[lenRow-k-1][j]);
                }
                for (int i=lenRow-k-2; i>k; i--){
                    if (count<size){
                        result[count] = matrix[i][k];
                        count++;
                    }
                    //System.out.println(matrix[i][k]);
                }
            }
            return result;
        }
}
