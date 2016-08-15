package com.lx.game.item.util;


/**
 * 
 * ClassName: MathUtil <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-7-14 下午7:49:49 <br/>
 * 
 * @author yxd
 * @version
 */
public class MathUtil {
	
	/**
	 * 必有一个
	 * 
	 * @param arr
	 * @return
	 */
	public static int getRandomByArr(int[] probability) {
		int rand = Const.random.nextInt(Const.DENOMINATOR);
		int sum = 0;
		for (int i = 0; i < probability.length; i++) {
			sum += probability[i];
			if (rand < sum) {
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * 可能没有
	 * 
	 * @param arr
	 * @return
	 */
	public static int getRandomByArrAndNoth(int arr[]) {
		int rand = Const.random.nextInt(Const.DENOMINATOR);
		int sum = 0;
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
			if (rand < sum) {
				return i;
			}
		}
		return -1;
	}
	
	public static boolean getRandomByProbability(int probability) {
		int rand = Const.random.nextInt(Const.DENOMINATOR);
		if (rand < probability) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * 交换数组中指定的两元素的位置
	 * 
	 * @param data
	 * 
	 * @param x
	 * 
	 * @param y
	 */
	private static void swap(int[] data, int x, int y) {
		
		int temp = data[x];
		
		data[x] = data[y];
		
		data[y] = temp;
		
	}
	
	/**
	 * 
	 * 直接选择排序法----选择排序的一种
	 * 
	 * 方法：每一趟从待排序的数据元素中选出最小（或最大）的一个元素， 顺序放在已排好序的数列的最后，直到全部待排序的数据元素排完。
	 * 
	 * 性能：比较次数O(n^2),n^2/2
	 * 
	 * 交换次数O(n),n
	 * 
	 * 交换次数比冒泡排序少多了，由于交换所需CPU时间比比较所需的CUP时间多，所以选择排序比冒泡排序快。
	 * 
	 * 但是N比较大时，比较所需的CPU时间占主要地位，所以这时的性能和冒泡排序差不太多，但毫无疑问肯定要快些。
	 * 
	 * 
	 * 
	 * @param data 要排序的数组
	 * 
	 * @param sortType 排序类型
	 * 
	 * @return
	 */
	public static void bubbleSort(int[] data) {
		int index;
		
		for (int i = 1; i < data.length; i++) {
			
			index = 0;
			
			for (int j = 1; j <= data.length - i; j++) {
				
				if (data[j] > data[index]) {
					
					index = j;
				}
				
			}
			// 交换在位置data.length-i和index(最大值)两个数
			swap(data, data.length - i, index);
			
		}
	}
	
	/**
	 * 平均值 getAverage:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param data
	 * @return
	 */
	public static int getAverage(int[] data) {
		int index = data[0];
		for (int i = 1; i < data.length; i++) {
			index += data[i];
		}
		return index / data.length;
	}
	
	
	
}
