package core_refactoring;

/**
 * 游戏中的牌堆组件，负责管理下面的牌
 * @author 87663
 */
public interface CardHeap extends Component{

	/**
	 * 检查上次移动牌，是否有新的牌被打开
	 * @return 是否有新的牌被打开
	 */
	boolean openCardLastRound();
	
}
