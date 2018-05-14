package core_refactoring;

/**
 * 游戏工厂类，负责游戏的组件构造及初始化
 * @author 87663
 */
public interface CardGameFactory {

	/**
	 * 返回一个默认难度的游戏
	 * @return 默认难度的游戏
	 */
	CardGame getCardGame();
	
	/**
	 * 返回一个指定难度的游戏
	 * @param d 游戏难度
	 * @return 指定难度的游戏
	 */
	CardGame getCardGame(Difficulty  d);
	
	/**
	 * 返回一个指定游戏模式的游戏
	 * @param mode 游戏模式
	 * @return 指定游戏模式的游戏
	 */
	CardGame getCardGame(Mode mode);
	
	/**
	 * 返回一个指定难度和模式的游戏
	 * @param d 游戏难度
	 * @param mode 游戏模式
	 * @return 指定难度和模式的游戏
	 */
	CardGame getCardGame(Difficulty  d,Mode mode);
	
}
