package core_refactoring;

/**
 * 表示左上角的发牌器，负责左上角牌的管理
 * @author 87663
 */
public interface Dealer extends Component{

	/**
	 * 移动牌
	 */
	void nextCards();
	
}
