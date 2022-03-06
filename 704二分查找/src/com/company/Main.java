package com.company;

public class Main {

    //二分查找
    public static void main(String[] args) {
        int [] nums = {-1,0,3,5,9,12};
        int target = 9;
        int result=0;
        result = search(nums, target);
        System.out.println(result);
        target = 2;
        result = search(nums, target);
        System.out.println(result);
      }

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
}
