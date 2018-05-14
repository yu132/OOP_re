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

public class SingleStepNextCardAnalyzerInitializer implements CardInitializer{
	
	private static Map<Integer,Components> map=new HashMap<>();
	
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
	
	private CardManagement cardGame;
	
	private CardChecker cardChecker=new CardChecker();
	
	private Card[] initCards=new Card[7];
	
	private int cardGiven=0;

	public void init(CardManagement cardGame){
		this.cardGame=cardGame;
	}
	
	@Override
	public Card getCard(Components c) {
		cardGiven++;
		
		if(cardGiven-1==0){
			Random rn=new Random();
			
			int n;
			int x=rn.nextInt(100);
			if(x<50)
				n=1;
			else if(x<90)
				n=2;
			else
				n=3;
			
			int[] temp=new int[n];
			
			RandomUniqueNumber rx=new RandomUniqueNumber(0, 5);
			
			for(int i=0;i<n;i++)
				temp[i]=rx.getNum();
			
			for(int i=0;i<6;i++){
				boolean flag=true;
				for(int j=0;j<n;j++){
					if(i==temp[j]){
						flag=false;
						break;
					}
				}
				
				if(flag){
					initCards[i]=cardChecker.getRandomUnusedCard();
				}else{
					int count=0;
					Card card;
					do{
						if(count==4){
							initCards[i]=cardChecker.getRandomUnusedCard();
							break;
						}
						card=CardImpl.valueOf(CardNumber.values()[12],CardType.values()[rn.nextInt(4)]);
						initCards[i]=card;
					}while(!cardChecker.checkCard(card));
				}
			}
			RandomUniqueNumber r1=new RandomUniqueNumber(0, 5);
			Random r2=new Random();
			while(true){
				int ix=r1.getNum();
				
				Card[] cards=initCards[ix].getCardStackable();
				if(cards!=null){
					if(r2.nextBoolean()){
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
				
				cards=initCards[ix].getCardNeedToStack();
				if(cards!=null){
					if(r2.nextBoolean()){
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
		
		if(cardGiven-1<=6){
			return initCards[cardGiven-1];
		}
		
		if(c==Components.DEALER){
			Random rx= new Random();
			if(rx.nextBoolean()||cardGame==null||cardGame.lastMove()==null){
				return cardChecker.getRandomUnusedCard();
			}
			
			ArrayList<Card> collector=new ArrayList<>();
			
			for(int i=1;i<=7;i++){
				Components temp=map.get(i);
				
				ArrayList<String> cards=cardGame.getTopCard(temp);
				if(cards.size()>=2){
					collector.add(CardImpl.valueOf(cards.get(0)));
				}else if(cards.size()==1){
					collector.add(CardImpl.valueOf(cards.get(0)));
				}
				
			}
			RandomUniqueNumber r=new RandomUniqueNumber(0, collector.size()-1);
			for(int i=0;i<collector.size();i++){
				int index=r.getNum();
				
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
				
			}
			return cardChecker.getRandomUnusedCard();
		}

		String[] movesp=cardGame.lastMove().split(" ");
		if(movesp.length==1)
			return cardChecker.getRandomUnusedCard();
		Components from=Components.valueOf(movesp[0]);
		Components to=Components.valueOf(movesp[1]);
		
		ArrayList<Card> collector=new ArrayList<>();
		ArrayList<Card> collectorup=new ArrayList<>();
		
		for(int i=0;i<=7;i++){
			Components temp=map.get(i);
			if(temp==from||temp==to)
				continue;
			if(temp==Components.DEALER){
				ArrayList<String> cards=cardGame.getTopCard(temp);
				if(cards.size()>=1)
					collector.add(CardImpl.valueOf(cards.get(cards.size()-1)));
			}else{
				ArrayList<String> cards=cardGame.getTopCard(temp);
				if(cards.size()>=2){
					collector.add(CardImpl.valueOf(cards.get(0)));
					collectorup.add(CardImpl.valueOf(cards.get(cards.size()-1)));
				}else if(cards.size()==1){
					collector.add(CardImpl.valueOf(cards.get(0)));
				}
			}
		}
		
		RandomUniqueNumber r=new RandomUniqueNumber(0, collector.size()+collectorup.size()-1);
		for(int i=0;i<collector.size()+collectorup.size();i++){
			int index=r.getNum();
			if(index<collector.size()){
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
			}else{
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
		return cardChecker.getRandomUnusedCard();
	}

}
