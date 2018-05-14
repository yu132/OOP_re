package core_refactoring.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import core_refactoring.*;

public class DealerImpl implements Dealer{
	
	private ArrayList<Card> cardQueueCache=new ArrayList<>();
	
	private ArrayList<Card> cardQueue=new ArrayList<>();
	
	private int topCardNumber=0;
	
	private int cardIndex=0;
	
	private int unknowCard=24;
	
	private Mode mode;
	
	private CardInitializer cardInitializer;
	
	private Deque<String> snapshot=new ArrayDeque<>();
	
	public DealerImpl(Mode mode, CardInitializer cardInitializer) {
		super();
		this.mode = mode;
		this.cardInitializer = cardInitializer;
	}

	private String getSnapshot(){
		StringBuilder sb=new StringBuilder(cardQueue.size()*10);
		boolean f=true;
		for(Card c:cardQueue){
			if(f){
				f=false;
				sb.append(c.toString());
			}else
				sb.append(" "+c.toString());
		}
		return cardIndex+" "+topCardNumber+" "+unknowCard+"#"+sb.toString();
	}

	@Override
	public MoveState sentSingleCard(Component c) {
		if(topCardNumber==0)
			return MoveState.ILLEGAL_MOVE;
		
		MoveState ms=c.getSingleCard(cardQueue.get(cardIndex+topCardNumber-1));
		if(ms==MoveState.SUCCESS){
			snapshot.push(getSnapshot());
			c.getSingleCard(cardQueue.get(cardIndex+topCardNumber-1));
			cardQueue.remove(cardIndex+topCardNumber-1);
			topCardNumber--;
			
			if(topCardNumber==0){
				if(cardIndex>=1){
					cardIndex--;
					topCardNumber=1;
				}
			}
			
		}
		return ms;
	}

	@Override
	public MoveState getSingleCard(Card card) {
		return MoveState.ILLEGAL_MOVE;
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
		System.out.println(topCardNumber);
		for(int i=0;i<topCardNumber;i++){
			temp.add(cardQueue.get(cardIndex+i).toString());
		}
		return temp;
	}

	@Override
	public ArrayList<String> getAllCard() {
		ArrayList<String> temp=new ArrayList<>();
		for(int i=0;i<cardQueue.size();i++){
			temp.add(cardQueue.get(i).toString());
		}
		for(int i=0;i<unknowCard;i++){
			temp.add("null");
		}
		return temp;
	}

	@Override
	public boolean ismovable(int index) {
		return index==1;
	}

	
//存疑
	
	
	@Override
	public boolean undo() {
		if(snapshot.isEmpty())
			return false;
		
		String[] temp=snapshot.pop().split("#");
		
		String[] temp2=temp[0].split(" ");
		
		cardIndex=Integer.valueOf(temp2[0]);
		topCardNumber=Integer.valueOf(temp2[1]);
		unknowCard=Integer.valueOf(temp2[2]);
		
		int num=(mode==Mode.THREE_CARD_MODE)?3:1;
		
		if(topCardNumber<num){
			if(cardIndex>=num-topCardNumber){
				cardIndex-=num-topCardNumber;
				topCardNumber=num;
			}
		}
		
		cardQueue.clear();
		
		if(temp.length==2){
			String[] temp3=temp[1].split(" ");
			for(int i=0;i<temp3.length;i++)
				cardQueue.add(CardImpl.valueOf(temp3[i]));
		}
		
		System.out.println(cardQueue);
		System.out.println(cardQueueCache);
		System.out.println();
		
		return true;
	}
	
	
//存疑
	
	
	@Override
	public boolean undoAll() {
		if(snapshot.isEmpty())
			return false;
		
		cardQueue=new ArrayList<>(cardQueueCache);
		
		cardIndex=0;
		topCardNumber=0;
		unknowCard=24;
		
		snapshot.clear();
		
		return true;
	}

	@Override
	public void nextCards() {
		snapshot.push(getSnapshot());
		int nextNumber=(mode==Mode.THREE_CARD_MODE)?3:1;
		cardIndex+=topCardNumber;
		topCardNumber=nextNumber;
		if(unknowCard>0){
			if(24-unknowCard>=cardQueueCache.size()){
				for(int i=0;i<nextNumber;i++){
					Card temp=cardInitializer.getCard(Components.DEALER);
					cardQueue.add(temp);
					cardQueueCache.add(temp);
				}
			}else{
				for(int i=0;i<nextNumber;i++){
					Card temp=cardQueueCache.get(24-unknowCard+i);
					cardQueue.add(temp);
				}
			}
			unknowCard-=nextNumber;
		}
		
		if(cardIndex>=cardQueue.size())
			cardIndex=0;
		if(cardIndex+topCardNumber>=cardQueue.size()){
			topCardNumber=cardQueue.size()-cardIndex;
		}
		
		System.out.println(cardQueue);
		System.out.println(cardQueueCache);
		System.out.println();
	}

}
