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
		if(cards[type][number]==null){
			cards[type][number]=new CardImpl(cardNumber,cardType);
		}
		return cards[type][number];
	}
	
	public static CardImpl valueOf(String toString){
		String[] temp=toString.split("_");
		try{
			CardType cardType=CardType.valueOf(temp[0]);
			CardNumber cardNumber=CardNumber.valueOf(temp[1]);
			return valueOf(cardNumber,cardType);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public CardNumber getCardNumber() {
		return CARDNUMBER;
	}

	@Override
	public CardType getCardType() {
		return CARDTYPE;
	}

	@Override
	public MoveState isStackableInHeap(Card card) {
		if(CARDNUMBER.ordinal()!=card.getCardNumber().ordinal()+1)
			return MoveState.WRONG_NUMBER;
		switch(CARDTYPE){
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
		default:
			return MoveState.ILLEGAL_MOVE;
		}
	}

	@Override
	public boolean isStackableInBox(Card card) {
		if(CARDNUMBER.ordinal()!=card.getCardNumber().ordinal()-1)
			return false;
		
		switch(CARDTYPE){
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
		default:
			return false;
		}
	}

	@Override
	public String toString() {
		return CARDTYPE+"_"+CARDNUMBER;
	}

	@Override
	public Card[] getCardStackable() {
		if(CARDNUMBER==CardNumber.ACE)
			return null;
		Card[] cards=new Card[2];
		if(CARDTYPE==CardType.CLUBS||CARDTYPE==CardType.SPADES){
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
		if(CARDNUMBER==CardNumber.KING)
			return null;
		Card[] cards=new Card[2];
		if(CARDTYPE==CardType.CLUBS||CARDTYPE==CardType.SPADES){
			cards[0]=valueOf(CardNumber.values()[CARDNUMBER.ordinal()+1], CardType.DIAMONDS);
			cards[1]=valueOf(CardNumber.values()[CARDNUMBER.ordinal()+1], CardType.HEARTS);
		}else{
			cards[0]=valueOf(CardNumber.values()[CARDNUMBER.ordinal()+1], CardType.CLUBS);
			cards[1]=valueOf(CardNumber.values()[CARDNUMBER.ordinal()+1], CardType.SPADES);
		}
		return cards;
	}
}
