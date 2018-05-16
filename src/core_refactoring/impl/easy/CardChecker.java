package core_refactoring.impl.easy;

import core_refactoring.Card;
import core_refactoring.CardNumber;
import core_refactoring.CardType;
import core_refactoring.impl.CardImpl;
import core_refactoring.util.RandomUniqueNumber;

/**
 * 用于检查牌是否已经被发出过
 * @author 87663
 *
 */
public class CardChecker {

	/**
	 * 标记某张牌已经被发过了
	 */
	private boolean[] used=new boolean[52];
	
	/**
	 * 用于返回某张没被发过的牌
	 */
	private RandomUniqueNumber r=new RandomUniqueNumber(0,51);
	
	/**
	 * 检查某张牌是否被发过了，如果没发过，则标记成发过了
	 * @param card 需要检查的牌
	 * @return 这张牌是否被发过了
	 */
	public boolean checkCard(Card card){
		
		if(card==null)//如果这张牌是空
			return false;//则返回假
		
		int index=card.getCardType().ordinal()*13+card.getCardNumber().ordinal();//获取牌的索引
		
		if(used[index]){//检查该牌
			return false;//如果用过，则返回假
		}else{//如果没用过
			used[index]=true;//标记为用过了
			return true;//返回真
		}
	}
	
	/**
	 * 随机获取一张没用过的牌
	 * @return 一张没用过的牌
	 */
	public Card getRandomUnusedCard(){
		while(true){//不停的找牌
			int n=r.getNum();//随机获得一张牌
			if(!used[n]){//如果没用过
				used[n]=true;//设为用过
				return CardImpl.valueOf(CardNumber.values()[n%13], CardType.values()[n/13]);//并返回这张牌
			}
		}
	}
	
	/**
	 * 重置该检查器
	 */
	public void reset(){
		used=new boolean[52];
	}

}
