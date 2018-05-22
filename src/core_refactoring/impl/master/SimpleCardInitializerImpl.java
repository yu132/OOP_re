package core_refactoring.impl.master;

import java.util.Random;

import core_refactoring.*;
import core_refactoring.impl.CardImpl;

/**
 * 纯随机的牌初始化器，完全是随机发牌
 * @author 87663
 *
 */
public class SimpleCardInitializerImpl implements CardInitializer{

	//没给的牌存储数组
	private int[][] notGiven=new int[4][13];
	
	//每组还剩下的牌
	private int[] eachNumber=new int[4];
	
	private Random r=new Random();
	
	//构造器
	public SimpleCardInitializerImpl() {
		for(int i=0;i<4;i++)//把牌初始化好
			for(int j=0;j<13;j++)
				notGiven[i][j]=j;
		for(int i=0;i<4;i++)//每组的数量给初始化好
			eachNumber[i]=13;
	}

	@Override
	public Card getCard(Components c) {
		//计算是否还有牌可给
		int count=0;
		for(int i=0;i<4;i++){
			if(eachNumber[i]!=0)
				count++;
		}
		if(count==0)
			throw new RuntimeException("No more card to give");
		//计算是否还有牌可给
		
		//随机抽取种类
		int type=r.nextInt(count);
		
		
		for(int i=0;i<4;i++){
			if(eachNumber[i]==0)//如果这个种类没牌了，就跳过
				continue;
			if(type==0){
				
				int numberi=r.nextInt(eachNumber[i]);//抽取牌的索引
				
				int number=notGiven[i][numberi];//把牌拿出来
				
				notGiven[i][numberi]=notGiven[i][--eachNumber[i]];//把最后面没抽放到这张被抽走的位置上
				
				return CardImpl.valueOf(CardNumber.values()[number],CardType.values()[i]);//返回真正的牌
				
			}else
				type--;//移动到那个种类
		}
		
		return null;
	}
	
}
