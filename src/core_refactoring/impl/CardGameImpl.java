package core_refactoring.impl;

import java.util.ArrayList;

import core_refactoring.*;

public class CardGameImpl implements CardGame{

	/**
	 * 牌局管理器
	 */
	private CardManagement cardGameManager;
	
	/**
	 * 牌局分析器
	 */
	private CardGameAnalyzer cardGameAnalyzer;
	
	/**
	 * 计分器
	 */
	private PointCounter pointCounter;
	
	/**
	 * 计时器
	 */
	private Timer timer;

	/**
	 * 构造器 
	 * @param cardGameManager 牌局管理器
	 * @param cardGameAnalyzer 牌局分析器
	 * @param pointCounter 计分器
	 * @param timer 计时器
	 */
	public CardGameImpl(CardManagement cardGameManager, CardGameAnalyzer cardGameAnalyzer, PointCounter pointCounter,
			Timer timer) {
		super();
		this.cardGameManager = cardGameManager;
		this.cardGameAnalyzer = cardGameAnalyzer;
		this.pointCounter = pointCounter;
		this.timer = timer;
	}

	@Override
	public MoveState moveCards(Components from, Components to, int number) {
		return cardGameManager.moveCards(from, to, number);//调用内部组件的方法
	}

	@Override
	public void nextCard() {
		cardGameManager.nextCard();//调用内部组件的方法
	}

	@Override
	public ArrayList<String> getTopCard(Components c) {
		return cardGameManager.getTopCard(c);//调用内部组件的方法
	}
	
	@Override
	public ArrayList<String> getAllCard(Components c) {
		return cardGameManager.getAllCard(c);//调用内部组件的方法
	}

	@Override
	public boolean undoable() {
		return cardGameManager.undoable();//调用内部组件的方法
	}

	@Override
	public boolean undo() {
		return cardGameManager.undo();//调用内部组件的方法
	}

	@Override
	public boolean undoAll() {
		return cardGameManager.undoAll();//调用内部组件的方法
	}

	@Override
	public String getTips() {
		return cardGameAnalyzer.getBestTips();//调用内部组件的方法
	}

	@Override
	public boolean isGameOver() {
		return cardGameAnalyzer.isGameOver();//调用内部组件的方法
	}

	@Override
	public boolean isGameFinish() {
		return cardGameAnalyzer.isGameFinish();//调用内部组件的方法
	}

	@Override
	public long getTime() {
		return timer.getTime();//调用内部组件的方法
	}

	@Override
	public int getPoint() {
		return pointCounter.getPoint();//调用内部组件的方法
	}


}
