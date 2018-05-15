package core_refactoring.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import core_refactoring.*;

public class DealerImpl implements Dealer{
	
	/**
	 * 保存所有获得过的牌的数组
	 */
	private ArrayList<Card> cardQueueCache=new ArrayList<>();
	
	/**
	 * 保存现在的牌的数组
	 */
	private ArrayList<Card> cardQueue=new ArrayList<>();
	
	/**
	 * 顶部的牌的数量（玩家可以看到的牌）
	 */
	private int topCardNumber=0;
	
	/**
	 * 顶部的牌在数组中的索引
	 */
	private int cardIndex=0;
	
	/**
	 * 有多少张牌尚不知道
	 */
	private int unknowCard=24;
	
	/**
	 * 游戏模式
	 */
	private Mode mode;
	
	/**
	 * 牌初始化器
	 */
	private CardInitializer cardInitializer;
	
	/**
	 * 快照栈
	 */
	private Deque<String> snapshot=new ArrayDeque<>();
	
	/**
	 * 构造器
	 * @param mode 模式
	 * @param cardInitializer 牌初始化器
	 */
	public DealerImpl(Mode mode, CardInitializer cardInitializer) {
		super();
		this.mode = mode;
		this.cardInitializer = cardInitializer;
	}

	/**
	 * 获取快照的方法
	 * @return 快照
	 */
	private String getSnapshot(){
		
		StringBuilder sb=new StringBuilder(cardQueue.size()*10);
		
		boolean f=true;
		
		for(Card c:cardQueue){//对于现在牌的数组中的牌，全部保存
			if(f){
				f=false;
				sb.append(c.toString());
			}else
				sb.append(" "+c.toString());
		}
		
		return cardIndex+" "+topCardNumber+" "+unknowCard+"#"+sb.toString();//返回恢复的必须的信息
	}

	@Override
	public MoveState sentSingleCard(Component c) {
		
		if(topCardNumber==0)//如果顶部没有牌，则不能移动
			return MoveState.ILLEGAL_MOVE;//返回非法移动
		
		MoveState ms=c.getSingleCard(cardQueue.get(cardIndex+topCardNumber-1));//调用另一个组件的获得牌方法，并得到结果
		
		if(ms==MoveState.SUCCESS){//如果移动成功
			
			snapshot.push(getSnapshot());//拍快照
			
			cardQueue.remove(cardIndex+topCardNumber-1);//移除移走的牌
			topCardNumber--;//顶部的牌数量减一
			
			if(topCardNumber==0){//如果顶部的牌数量为0
				if(cardIndex>=1){//如果前面还有牌
					cardIndex--;//就把前面的一张拿出来
					topCardNumber=1;//放到最顶上
				}
			}
			
		}
		
		return ms;//返回移动状态
	}

	@Override
	public MoveState getSingleCard(Card card) {
		return MoveState.ILLEGAL_MOVE;//不允许接受牌
	}

	@Override
	public MoveState sentCards(Component c, int number) {
		if(number==1)//如果是一张牌
			return sentSingleCard(c);//调用一张牌的方法
		return MoveState.ILLEGAL_MOVE;//不允许一次移走多张牌
	}

	@Override
	public MoveState getCards(ArrayList<Card> cards) {
		return MoveState.ILLEGAL_MOVE;//不允许接受牌
	}

	@Override
	public ArrayList<String> getTopCard() {
		ArrayList<String> temp=new ArrayList<>();
		for(int i=0;i<topCardNumber;i++){//对于顶部的每张牌，都加入数组
			temp.add(cardQueue.get(cardIndex+i).toString());
		}
		return temp;
	}

	@Override
	public ArrayList<String> getAllCard() {
		ArrayList<String> temp=new ArrayList<>();
		for(int i=0;i<cardQueue.size();i++){//对于每张牌，都加入数组
			temp.add(cardQueue.get(i).toString());
		}
		for(int i=0;i<unknowCard;i++){//如果有没拿到过的牌，则加入null填充
			temp.add("null");
		}
		return temp;
	}

	@Override
	public boolean ismovable(int index) {
		return index==1;//只有顶部的牌能动
	}
	
	@Override
	public boolean undo() {
		if(snapshot.isEmpty())//如果快照为空，则不能够撤销
			return false;
		
		//解析快照
		String[] temp=snapshot.pop().split("#");
		String[] temp2=temp[0].split(" ");
		cardIndex=Integer.valueOf(temp2[0]);
		topCardNumber=Integer.valueOf(temp2[1]);
		unknowCard=Integer.valueOf(temp2[2]);
		
		int num=(mode==Mode.THREE_CARD_MODE)?3:1;//根据模式选择数量
		
		if(topCardNumber<num){//如果顶部的牌小于应有的牌，则抽取前面的牌补充
			if(cardIndex>=num-topCardNumber){
				cardIndex-=num-topCardNumber;
				topCardNumber=num;
			}
		}
		
		cardQueue.clear();//清空牌数组
		
		if(temp.length==2){//根据快照进行恢复牌
			String[] temp3=temp[1].split(" ");
			for(int i=0;i<temp3.length;i++)
				cardQueue.add(CardImpl.valueOf(temp3[i]));
		}
		
		return true;//返回成功撤销
	}
	
	@Override
	public boolean undoAll() {
		if(snapshot.isEmpty())//如果快照是空，则不能撤销
			return false;
		
		//将所有恢复到初始状态
		cardQueue=new ArrayList<>(cardQueueCache);
		cardIndex=0;
		topCardNumber=0;
		unknowCard=24;
		snapshot.clear();
		
		return true;//返回成功撤销
	}

	@Override
	public void nextCards() {
		
		snapshot.push(getSnapshot());//拍快照
		
		int nextNumber=(mode==Mode.THREE_CARD_MODE)?3:1;//根据模式选择牌的数量
		
		cardIndex+=topCardNumber;//移动顶牌的索引
		
		topCardNumber=nextNumber;//把顶牌数量赋值为下一次的牌数量
		
		if(unknowCard>0){//如果有没获取过的牌
			
			if(24-unknowCard>=cardQueueCache.size()){//如果是应该从牌初始化器中获得牌（没有撤销过）
				
				for(int i=0;i<nextNumber;i++){//获取模式指定数量的牌
					Card temp=cardInitializer.getCard(Components.DEALER);//从牌初始化器中获取
					cardQueue.add(temp);
					cardQueueCache.add(temp);
				}
				
			}else{//如果是应该从cardQueueCache中取得新牌（有过撤销行为）
				
				for(int i=0;i<nextNumber;i++){//获取模式指定数量的牌
					Card temp=cardQueueCache.get(24-unknowCard+i);//从cardQueueCache中获取
					cardQueue.add(temp);
				}
				
			}
			
			unknowCard-=nextNumber;//没获取过的牌减少
			
		}
		
		if(cardIndex>=cardQueue.size())//如果顶牌索引越界，证明已经遍历完一次了，则返回前面
			cardIndex=0;
		
		if(cardIndex+topCardNumber>=cardQueue.size()){//如果顶牌的数量比剩下的牌数量还多
			topCardNumber=cardQueue.size()-cardIndex;//则顶牌数量等于剩下牌的数量
		}
		
	}

}
