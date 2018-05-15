package core_refactoring.impl;

import core_refactoring.Card;
import core_refactoring.CardNumber;
import core_refactoring.CardType;
import core_refactoring.MoveState;

/**
 * 牌类的实现类
 * @author 87663
 */
public class CardImpl implements Card{
	
	/**
	 * 静态数组，用于存储所有的卡牌对象
	 */
	private static CardImpl[][] cards=new CardImpl[4][13];
	
	/**
	 * 卡牌的数字
	 */
	public final CardNumber CARDNUMBER;
	
	/**
	 * 卡牌的种类
	 */
	public final CardType CARDTYPE;
	
	/**
	 * 卡牌类的构造器
	 * 私有的是因为这些卡牌都只需要生成一张就够了
	 * @param cardNumber
	 * @param cardType
	 */
	private CardImpl(CardNumber cardNumber, CardType cardType) {
		super();
		CARDNUMBER = cardNumber;
		CARDTYPE = cardType;
	}
	
	/**
	 * 卡牌对象的获取方法，如果没有构造过的，那就构造一个
	 * 如果构造过了，那就直接从静态数组中取出来返回
	 * @param cardNumber 卡牌的数字
	 * @param cardType 卡牌的种类
	 * @return 一个卡牌对象
	 */
	public static CardImpl valueOf(CardNumber cardNumber, CardType cardType){
		int number=cardNumber.ordinal();
		int type=cardType.ordinal();
		if(cards[type][number]==null){//通过卡牌的数字和种类来获得卡牌，如果没有这张牌，就构造一张
			cards[type][number]=new CardImpl(cardNumber,cardType);
		}
		return cards[type][number];
	}
	
	/**
	 * 通过字符串构造卡牌，构造的卡牌toString产生的字符串和参数相同
	 * @param toString 构造卡牌的字符串
	 * @return
	 */
	public static CardImpl valueOf(String toString){
		String[] temp=toString.split("_");//分开字符串的卡牌数字和卡牌种类
		try{
			CardType cardType=CardType.valueOf(temp[0]);//得到卡牌种类
			CardNumber cardNumber=CardNumber.valueOf(temp[1]);//得到卡牌数字
			return valueOf(cardNumber,cardType);//调用通过种类和数字的方法返回
		}catch(Exception e){
			return null;//如果出错，那么返回null
		}
	}
	
	@Override
	public CardNumber getCardNumber() {
		return CARDNUMBER;//返回卡牌数字
	}

	@Override
	public CardType getCardType() {
		return CARDTYPE;//返回卡牌种类
	}

	@Override
	public MoveState isStackableInHeap(Card card) {
		if(CARDNUMBER.ordinal()!=card.getCardNumber().ordinal()+1)//在牌堆中，叠上的牌必须比当前牌数字小1
			return MoveState.WRONG_NUMBER;
		
		switch(CARDTYPE){//卡牌种类必须相反
		case CLUBS:
		case SPADES:
			switch(card.getCardType()){
			case CLUBS:
			case SPADES:
				return MoveState.WRONG_COLOR;
			case DIAMONDS:
			case HEARTS:
				return MoveState.SUCCESS;
			}
		case DIAMONDS:
		case HEARTS:
			switch(card.getCardType()){
			case CLUBS:
			case SPADES:
				return MoveState.SUCCESS;
			case DIAMONDS:
			case HEARTS:
				return MoveState.WRONG_COLOR;
			}
		default://如果出现了未知类型，则返回非法
			return MoveState.ILLEGAL_MOVE;
		}
	}

	@Override
	public boolean isStackableInBox(Card card) {
		if(CARDNUMBER.ordinal()!=card.getCardNumber().ordinal()-1)//在收集器中，叠上的牌必须比当前牌数字大1
			return false;
		
		switch(CARDTYPE){//卡牌种类必须相同
		case CLUBS:
			switch(card.getCardType()){
			case CLUBS:
				return true;
			case SPADES:
			case DIAMONDS:
			case HEARTS:
				return false;
			}
		case SPADES:
			switch(card.getCardType()){
			case SPADES:
				return true;
			case CLUBS:
			case DIAMONDS:
			case HEARTS:
				return false;
			}
		case DIAMONDS:
			switch(card.getCardType()){
			case DIAMONDS:
				return true;
			case CLUBS:
			case SPADES:
			case HEARTS:
				return false;
			}
		case HEARTS:
			switch(card.getCardType()){
			case HEARTS:
				return true;
			case CLUBS:
			case SPADES:
			case DIAMONDS:
				return false;
			}
		default://如果出现了未知类型，则返回非法
			return false;
		}
	}

	@Override
	public String toString() {
		return CARDTYPE+"_"+CARDNUMBER;//返回卡牌的字符串表示
	}

	@Override
	public Card[] getCardStackable() {
		if(CARDNUMBER==CardNumber.ACE)//如果是ACE的话，没有牌可以放在上面
			return null;//返回空
		Card[] cards=new Card[2];
		if(CARDTYPE==CardType.CLUBS||CARDTYPE==CardType.SPADES){//返回牌色相反，数字小一的牌
			cards[0]=valueOf(CardNumber.values()[CARDNUMBER.ordinal()-1], CardType.DIAMONDS);
			cards[1]=valueOf(CardNumber.values()[CARDNUMBER.ordinal()-1], CardType.HEARTS);
		}else{
			cards[0]=valueOf(CardNumber.values()[CARDNUMBER.ordinal()-1], CardType.CLUBS);
			cards[1]=valueOf(CardNumber.values()[CARDNUMBER.ordinal()-1], CardType.SPADES);
		}
		return cards;
	}
	
	@Override
	public Card[] getCardNeedToStack() {
		if(CARDNUMBER==CardNumber.KING)//如果是KING，其不能放在任何一张牌上面
			return null;
		Card[] cards=new Card[2];
		if(CARDTYPE==CardType.CLUBS||CARDTYPE==CardType.SPADES){//返回牌色相反，数字大一的牌
			cards[0]=valueOf(CardNumber.values()[CARDNUMBER.ordinal()+1], CardType.DIAMONDS);
			cards[1]=valueOf(CardNumber.values()[CARDNUMBER.ordinal()+1], CardType.HEARTS);
		}else{
			cards[0]=valueOf(CardNumber.values()[CARDNUMBER.ordinal()+1], CardType.CLUBS);
			cards[1]=valueOf(CardNumber.values()[CARDNUMBER.ordinal()+1], CardType.SPADES);
		}
		return cards;
	}
}
