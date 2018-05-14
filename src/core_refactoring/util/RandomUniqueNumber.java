package core_refactoring.util;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 * 返回不重复随机数的类
 * @author 87663
 *
 */
public class RandomUniqueNumber {

	private int[] temp;
	private int length;
	private int allLength;
	
	static Random r=new Random();

	/**
	 * 构造器
	 * @param from 随机数从多少开始（包含）
	 * @param to 随机数到多少结束（包含）
	 */
	public RandomUniqueNumber(int from,int to) {
		if(from>to)
			throw new IllegalArgumentException("From can't be bigger than to");
		this.temp = new int[to-from+1];
		length=to-from+1;
		allLength=length;
		for(int i=from,j=0;i<=to;i++,j++)
			temp[j]=i;
	}
	
	/**
	 * 获得一个不重复的随机数
	 * @return 一个不重复的随机数
	 */
	public int getNum(){
		if(length==0)
			throw new NoSuchElementException("No more number");
		int l=r.nextInt(length);
		int n=temp[l];
		temp[l]=temp[--length];
		temp[length]=n;
		return n;
	}
	
	/**
	 * 重置该随机数生成器
	 */
	public void reSet(){
		length=allLength;
	}
}
