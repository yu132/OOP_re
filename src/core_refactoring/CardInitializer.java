package core_refactoring;

/**
 * 牌初始化器，决定以何种方式初始化牌
 * @author 87663
 *
 */
public interface CardInitializer {

	/**
	 * 获取一张牌，按照该初始化器的原理返回
	 * @return 一张牌
	 */
	Card getCard(Components c);
	
}
