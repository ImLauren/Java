package com.company;

public class Main {

    public static void main(String[] args) {

        /*二分查找
        int [] nums = {-1,0,3,5,9,12};
        int target = 9;
        int result=0;
        result = search(nums, target);
        System.out.println(result);
        target = 2;
        result = search(nums, target);
        System.out.println(result);
         */


      }

      //704 二分查找
    public static int search(int[] nums, int target) {
        int len = nums.length;
        int left=0;
        int right = len-1;
        while(left<=right){
            int mid = (left + right)/2;
            if(nums[mid] == target){
                return mid;
            }else if(nums[mid]>target){
                right = mid-1;
            }else if(nums[mid] <target){
                left = mid+1;
            }
        }

        return -1;
    }

    //374 猜数字大小
    public class Solution extends GuessGame {
        public int guessNumber(int n) {
            int left=0;
            int right = n;
            while(left<=right){
                int mid = left + (right-left)/2;
                int re = guess(mid);
                if (re == 0){
                    return mid;
                }else if(re == 1){
                    left = mid+1;
                }else if(re == -1){
                    right = mid-1;
                }
            }
            return 0;

        }
    }

}
