package core_refactoring.impl.easy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import core_refactoring.Card;
import core_refactoring.CardInitializer;
import core_refactoring.CardManagement;
import core_refactoring.CardNumber;
import core_refactoring.CardType;
import core_refactoring.Components;
import core_refactoring.impl.CardImpl;
import core_refactoring.util.RandomUniqueNumber;

/**
 * 根据当前牌局情况来进行发牌的发牌器
 * @author 87663
 *
 */
public class SingleStepNextCardAnalyzerInitializer implements CardInitializer{
	
	/**
	 * 根据数字对组件名称进行映射
	 */
	private static Map<Integer,Components> map=new HashMap<>();
	
	/**
	 * 映射的初始化
	 */
	static {
		map.put(0, Components.DEALER);
		map.put(1, Components.CARD_HEAP_1);
		map.put(2, Components.CARD_HEAP_2);
		map.put(3, Components.CARD_HEAP_3);
		map.put(4, Components.CARD_HEAP_4);
		map.put(5, Components.CARD_HEAP_5);
		map.put(6, Components.CARD_HEAP_6);
		map.put(7, Components.CARD_HEAP_7);
	}
	
	/**
	 * 表示一个牌局管理器
	 */
	private CardManagement cardGame;
	
	/**
	 * 牌检查器
	 */
	private CardChecker cardChecker=new CardChecker();
	
	/**
	 * 初始化的牌堆的第一张牌
	 */
	private Card[] initCards=new Card[7];
	
	/**
	 * 已经给过的牌的数量
	 */
	private int cardGiven=0;

	/**
	 * 初始化方法，因为不能在构造CardManagement的时候进行this引用溢出，所以就延迟一点进行初始化操作
	 * @param cardGame
	 */
	public void init(CardManagement cardGame){
		this.cardGame=cardGame;
	}
	
	@Override
	public Card getCard(Components c) {
		
		cardGiven++;//给的牌加一
		
		if(cardGiven-1==0){//如果没给过牌
			
			Random rn=new Random();//一个随机数生成器
			
			int n;//KING的个数
			
			int x=rn.nextInt(100);//一个小于100的随机数
			
			if(x<50)//50%有1个KING
				n=1;
			else if(x<90)//40%有两个KING
				n=2;
			else//10%有三个KING
				n=3;
			
			int[] temp=new int[n];
			
			RandomUniqueNumber rx=new RandomUniqueNumber(0, 5);
			
			for(int i=0;i<n;i++)//抽取KING的位置
				temp[i]=rx.getNum();
			
			for(int i=0;i<6;i++){//给6个位置都初始化牌
				boolean flag=true;//标识，true表示不是KING
				
				for(int j=0;j<n;j++){//检查这个位置是不是KING
					
					if(i==temp[j]){//是KING就跳出
						flag=false;
						break;
					}
					
				}
				
				if(flag){//不是KING
					initCards[i]=cardChecker.getRandomUnusedCard();//随机初始化一张牌
				}else{
					int count=0;//计数
					Card card;
					RandomUniqueNumber rt=new RandomUniqueNumber(0, 3);
					do{
						if(count==4){//如果超出了4次，肯定KING被用光了
							initCards[i]=cardChecker.getRandomUnusedCard();//随机初始化一张牌
							break;
						}
						card=CardImpl.valueOf(CardNumber.values()[12],CardType.values()[rt.getNum()]);//初始一张KING
						initCards[i]=card;
					}while(!cardChecker.checkCard(card));//如果这张KING没被用过就结束，否则重新检查另一张KING
				}
			}
			
			RandomUniqueNumber r1=new RandomUniqueNumber(0, 5);//从6个位置选一个位置
			Random r2=new Random();
			
			while(true){
				int ix=r1.getNum();//选一个可以移动走的牌的位置
				
				Card[] cards=initCards[ix].getCardStackable();//获得可以叠的牌
				
				if(cards!=null){//若有牌可以叠
					
					if(r2.nextBoolean()){//随机先后顺序
						
						if(cardChecker.checkCard(cards[0])){//如果第一张可以用
							initCards[6]=cards[0];//最后一张设为这张
							return initCards[0];//返回第一张
						}
						
						if(cardChecker.checkCard(cards[1])){//第二张可以用
							initCards[6]=cards[1];//最后一张设为这张
							return initCards[0];//返回第一张
						}
						
					}else{//顺序与前面相反
						if(cardChecker.checkCard(cards[1])){
							initCards[6]=cards[1];
							return initCards[0];
						}
						if(cardChecker.checkCard(cards[0])){
							initCards[6]=cards[0];
							return initCards[0];
						}
					}
				}
				
				cards=initCards[ix].getCardNeedToStack();//获取需要叠的牌
				
				if(cards!=null){//如果存在这样的牌
					
					if(r2.nextBoolean()){//与上面类似
						
						if(cardChecker.checkCard(cards[0])){
							initCards[6]=cards[0];
							return initCards[0];
						}
						
						if(cardChecker.checkCard(cards[1])){
							initCards[6]=cards[1];
							return initCards[0];
						}
						
					}else{
						
						if(cardChecker.checkCard(cards[1])){
							initCards[6]=cards[1];
							return initCards[0];
						}
						
						if(cardChecker.checkCard(cards[0])){
							initCards[6]=cards[0];
							return initCards[0];
						}
						
					}
				}
			}
		}
		
		if(cardGiven-1<=6){//如果给过的牌小于等于6，则都是牌堆拿走的牌，因为只有牌堆在构造时就拿牌，其他的都不拿的
			return initCards[cardGiven-1];//依次返回牌堆的初始化的牌
		}
		
		if(c==Components.DEALER){//如果是发牌器拿牌
			
			Random rx= new Random();
			if(rx.nextInt(100)<20||cardGame==null||cardGame.lastMove()==null){//10%的概率，或是牌局管理器没初始化，或是上次的移动不存在
				return cardChecker.getRandomUnusedCard();
			}
			
			ArrayList<Card> collector=new ArrayList<>();
			
			for(int i=1;i<=7;i++){//依次收集每个牌堆的顶部的牌
				
				Components temp=map.get(i);
				
				ArrayList<String> cards=cardGame.getTopCard(temp);
				if(cards.size()>=1){
					collector.add(CardImpl.valueOf(cards.get(0)));
				}
				
			}
			
			//随机发某个牌堆上可叠的牌
			RandomUniqueNumber r=new RandomUniqueNumber(0, collector.size()-1);
			
			for(int i=0;i<collector.size();i++){//依次检查牌是否可用
				int index=r.getNum();
				
				Card nowCard=collector.get(index);//获取顶部的牌
				Card[] nextCards=nowCard.getCardStackable();//获取可叠的牌
				
				if(nextCards==null)//如果不存在可叠的牌
					continue;
				
				if(cardChecker.checkCard(nextCards[0])){//分别检查牌是否可用
					return nextCards[0];
				}
				
				if(cardChecker.checkCard(nextCards[1])){//分别检查牌是否可用
					return nextCards[1];
				}
				
			}
			
			return cardChecker.getRandomUnusedCard();//如果没有可以叠的牌，则随机返回一张牌
		}
		
		//牌堆的发牌

		String[] movesp=cardGame.lastMove()==null?null:cardGame.lastMove().split(" ");//获取上次移动的信息
		
		Components from=(movesp==null?true:movesp.length==1)?null:Components.valueOf(movesp[0]);//获取上次移动的来源，如果没有则是null
		
		ArrayList<Card> collector=new ArrayList<>();//收集可以向上叠牌的牌
		
		ArrayList<Card> collectorup=new ArrayList<>();//收集可以叠牌的牌
		
		for(int i=0;i<=7;i++){//对于每个组件，收集两种牌
			
			Components temp=map.get(i);
			
			if(temp==from)//因为是移动完之后发牌，所以这个地方没有牌了，所以跳过
				continue;
			
			if(temp==Components.DEALER){//对于发牌器，只收集第一张牌
				ArrayList<String> cards=cardGame.getTopCard(temp);
				if(cards.size()>=1)
					collector.add(CardImpl.valueOf(cards.get(cards.size()-1)));
			}else{//对于牌堆，收集两种牌
				ArrayList<String> cards=cardGame.getTopCard(temp);
				if(cards.size()>=2){
					collector.add(CardImpl.valueOf(cards.get(0)));
					collectorup.add(CardImpl.valueOf(cards.get(cards.size()-1)));
				}else if(cards.size()==1){
					collector.add(CardImpl.valueOf(cards.get(0)));
				}
			}
			
		}
		
		RandomUniqueNumber r=new RandomUniqueNumber(0, collector.size()+collectorup.size()-1);//随机抽取可移动牌的顺序
		
		for(int i=0;i<collector.size()+collectorup.size();i++){//对于每个组件，都检查要发牌的牌是否被用过了
			
			int index=r.getNum();//随机抽取某张牌
			
			if(index<collector.size()){//如果是可叠的牌，就找一张能放上去的牌
				
				Card nowCard=collector.get(index);
				Card[] nextCards=nowCard.getCardStackable();
				if(nextCards==null)
					continue;
				if(cardChecker.checkCard(nextCards[0])){
					return nextCards[0];
				}
				if(cardChecker.checkCard(nextCards[1])){
					return nextCards[1];
				}
				
			}else{//如果是需要放上去的牌，则找一张能叠的牌
				
				Card nowCard=collectorup.get(index-collector.size());
				Card[] nextCards=nowCard.getCardNeedToStack();
				if(nextCards==null)
					continue;
				if(cardChecker.checkCard(nextCards[0])){
					return nextCards[0];
				}
				if(cardChecker.checkCard(nextCards[1])){
					return nextCards[1];
				}
				
			}
		}
		
		return cardChecker.getRandomUnusedCard();//如果没有的话，就随机返回一张牌
	}

}
