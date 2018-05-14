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
		for(Card c:cardStack){
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
		if(cardStack.isEmpty())
			return MoveState.ILLEGAL_MOVE;
		
		Card topCard=cardStack.peek();
		MoveState ms=c.getSingleCard(topCard);
		if(ms==MoveState.SUCCESS){
			snapshot.push(getSnapshot());
			cardStack.pop();
		}
		
		return ms;
	}

	@Override
	public MoveState getSingleCard(Card card) {
		if(cardStack.isEmpty()){
			if(card.getCardNumber()==CardNumber.ACE){
				snapshot.push(getSnapshot());
				cardStack.push(card);
				return MoveState.SUCCESS;
			}else
				return MoveState.WRONG_COLLECTION;
		}
		
		Card topCard=cardStack.peek();
		if(topCard.isStackableInBox(card)){
			snapshot.push(getSnapshot());
			cardStack.push(card);
			return MoveState.SUCCESS;
		}else
			return MoveState.WRONG_COLLECTION;
	}

	@Override
	public MoveState sentCards(Component c, int number) {
		return MoveState.ILLEGAL_MOVE;
	}

	@Override
	public MoveState getCards(ArrayList<Card> cards) {
		return MoveState.ILLEGAL_MOVE;
	}

	@Override
	public ArrayList<String> getTopCard() {
		ArrayList<String> temp=new ArrayList<>();
		if(!cardStack.isEmpty())
			temp.add(cardStack.peek().toString());
		return temp;
	}

	@Override
	public ArrayList<String> getAllCard() {
		ArrayList<String> temp=new ArrayList<>();
		boolean f=true;
		for(Card c:cardStack){
			if(f){
				f=false;
				temp.add(c.toString());
			}else
				temp.add(" "+c.toString());
		}
		return temp;
	}

	@Override
	public boolean ismovable(int index) {
		return index==1;
	}

	@Override
	public boolean undo() {
		if(snapshot.isEmpty())
			return false;
		
		String last=snapshot.pop();
		cardStack.clear();
		
		if(!last.equals("")){
			String[] cards=last.split(" ");
			for(int i=0;i<cards.length;i++)
				cardStack.push(CardImpl.valueOf(cards[cards.length-1-i]));
		}
		return true;
	}

	@Override
	public boolean undoAll() {
		if(snapshot.isEmpty())
			return false;
		
		cardStack.clear();
		snapshot.clear();
		
		return true;
	}

}
