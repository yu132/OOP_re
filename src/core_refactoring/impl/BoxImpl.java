package core_refactoring.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import core_refactoring.Box;
import core_refactoring.Card;
import core_refactoring.CardNumber;
import core_refactoring.Component;
import core_refactoring.MoveState;

/**
 * 箱子类的实现类
 * @author 87663
 */
public class BoxImpl implements Box{

	/**
	 * 一个栈，用于存储当前在该箱子内的卡牌
	 */
	private Deque<Card> cardStack=new ArrayDeque<>();
	
	/**
	 * 一个栈，用于存储每一次对这个箱子操作前的快照
	 */
	private Deque<String> snapshot=new ArrayDeque<>();
	
	/**
	 * 内部方法，获取当前箱子内卡牌的快照，用于撤销操作
	 * @return 当前卡牌的快照，以String方式存储
	 */
	private String getSnapshot(){
		StringBuilder sb=new StringBuilder(cardStack.size()*10);
		boolean f=true;
		for(Card c:cardStack){//依次向字符串构造器中加入卡牌的信息
			if(f){
				f=false;
				sb.append(c.toString());
			}else
				sb.append(" "+c.toString());
		}
		return sb.toString();
	}
	
	@Override
	public MoveState sentSingleCard(Component c) {
		if(cardStack.isEmpty())//如果空，则不可移动
			return MoveState.ILLEGAL_MOVE;
		
		Card topCard=cardStack.peek();//看牌
		MoveState ms=c.getSingleCard(topCard);//把牌送给另一个组件
		
		if(ms==MoveState.SUCCESS){//如果成功的话
			snapshot.push(getSnapshot());//加入一个操作快照
			cardStack.pop();//把牌拿走
		}
		
		return ms;//返回操作结果
	}

	@Override
	public MoveState getSingleCard(Card card) {
		if(cardStack.isEmpty()){//如果是空的
			if(card.getCardNumber()==CardNumber.ACE){//检查送来的牌是不是ACE
				snapshot.push(getSnapshot());//是的话，拍快照
				cardStack.push(card);//将牌加入
				return MoveState.SUCCESS;//返回成功
			}else
				return MoveState.WRONG_COLLECTION;//否则返回错误收集结果
		}
		
		//如果不为空
		Card topCard=cardStack.peek();//看顶部的牌
		if(topCard.isStackableInBox(card)){//检查给的牌是否能够放上去
			snapshot.push(getSnapshot());//如果可以的话，就拍一个快照
			cardStack.push(card);//把牌加入
			return MoveState.SUCCESS;//返回成功
		}else
			return MoveState.WRONG_COLLECTION;//否则返回失败
	}

	@Override
	public MoveState sentCards(Component c, int number) {//这个组件不允许一次送走多张牌
		if(number==1)//如果牌数量为一
			return sentSingleCard(c);//则调用送一张的方法
		return MoveState.ILLEGAL_MOVE;//否则返回非法操作
	}

	@Override
	public MoveState getCards(ArrayList<Card> cards) {//这个组件不允许一次得到多张牌
		return MoveState.ILLEGAL_MOVE;//返回非法操作
	}

	@Override
	public ArrayList<String> getTopCard() {//获取顶部的牌
		ArrayList<String> temp=new ArrayList<>();
		if(!cardStack.isEmpty())//如果非空
			temp.add(cardStack.peek().toString());//则将顶部的牌放入数组总
		return temp;//并返回这个数组
	}

	@Override
	public ArrayList<String> getAllCard() {
		ArrayList<String> temp=new ArrayList<>();
		boolean f=true;
		for(Card c:cardStack){//对于每张牌，都加入数组中
			if(f){
				f=false;
				temp.add(c.toString());
			}else
				temp.add(" "+c.toString());
		}
		return temp;//返回这个数组
	}

	@Override
	public boolean ismovable(int index) {
		return index==1;//如果是第一张就可以移动
	}

	@Override
	public boolean undo() {
		if(snapshot.isEmpty())//如果没有剩下的快照了，就不能撤销
			return false;
		
		String last=snapshot.pop();//取出最后一张快照
		cardStack.clear();//清空牌
		
		if(!last.equals("")){//将牌恢复成快照的样子
			String[] cards=last.split(" ");
			for(int i=0;i<cards.length;i++)
				cardStack.push(CardImpl.valueOf(cards[cards.length-1-i]));
		}
		return true;//返回撤销成功
	}

	@Override
	public boolean undoAll() {
		if(snapshot.isEmpty())//如果没有剩下的快照了，就不能撤销
			return false;
		
		cardStack.clear();//撤销回初始状态时，没有牌
		snapshot.clear();//也没有快照
		
		return true;//返回成功
	}

}
