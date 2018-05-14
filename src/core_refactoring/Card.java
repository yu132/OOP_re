package core_refactoring;

/**
 * 牌类，每个对象表示一张纸牌
 * @author 87663
 */
public interface Card {
	
	/**
	 * 判断另一张纸牌是否能够堆叠在本张纸牌上
	 * 该方法适用于牌堆上
	 * @param card 另一张纸牌
	 * @return 是否能够堆叠及状态
	 */
	MoveState isStackableInHeap(Card card);
	
	/**
	 * 获得可以在该牌上叠放的牌
	 * @return 可以在该牌上叠放的牌
	 */
	Card[] getCardStackable();
	
	/**
	 * 获得可以叠放这张牌的牌
	 * @return 可以叠放这张牌的牌
	 */
	Card[] getCardNeedToStack();
	
	/**
	 * 判断另一张纸牌是否能够堆叠在本张纸牌上
	 * 该方法适用于箱子上
	 * @param card 另一张纸牌
	 * @return 是否能够堆叠
	 */
	boolean isStackableInBox(Card card);
	
	/**
	 * 获得卡牌数字
	 * @return 卡牌数字
	 */
	CardNumber getCardNumber();
	
	/**
	 * 获得卡牌花色
	 * @return 卡牌花色
	 */
	CardType getCardType();
	
}
