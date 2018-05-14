package core_refactoring.impl.easy;

import core_refactoring.Card;
import core_refactoring.CardNumber;
import core_refactoring.CardType;
import core_refactoring.impl.CardImpl;
import core_refactoring.util.RandomUniqueNumber;

public class CardChecker {

	private boolean[] used=new boolean[52];
	
	private RandomUniqueNumber r=new RandomUniqueNumber(0,51);
	
	public boolean checkCard(Card card){
		if(card==null)
			return false;
		int index=card.getCardType().ordinal()*13+card.getCardNumber().ordinal();
		System.out.println(index);
		if(used[index]){
			return false;
		}else{
			used[index]=true;
			return true;
		}
	}
	
	public Card getRandomUnusedCard(){
		while(true){
			int n=r.getNum();
			if(!used[n]){
				used[n]=true;
				return CardImpl.valueOf(CardNumber.values()[n%13], CardType.values()[n/13]);
			}
		}
	}
	
	public void reset(){
		used=new boolean[52];
	}

}
