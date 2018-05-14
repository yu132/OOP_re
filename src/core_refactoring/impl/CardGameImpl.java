package core_refactoring.impl;

import java.util.ArrayList;

import core_refactoring.*;

public class CardGameImpl implements CardGame{

	private CardManagement cardGameManager;
	
	private CardGameAnalyzer cardGameAnalyzer;
	
	private PointCounter pointCounter;
	
	private Timer timer;

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
		return cardGameManager.moveCards(from, to, number);
	}

	@Override
	public void nextCard() {
		cardGameManager.nextCard();
	}

	@Override
	public ArrayList<String> getAllCard(Components c) {
		return cardGameManager.getAllCard(c);
	}

	@Override
	public boolean undoable() {
		return cardGameManager.undoable();
	}

	@Override
	public boolean undo() {
		return cardGameManager.undo();
	}

	@Override
	public boolean undoAll() {
		return cardGameManager.undoAll();
	}

	@Override
	public String getTips() {
		return cardGameAnalyzer.getBestTips();
	}

	@Override
	public boolean isGameOver() {
		return cardGameAnalyzer.isGameOver();
	}

	@Override
	public boolean isGameFinish() {
		return cardGameAnalyzer.isGameFinish();
	}

	@Override
	public long getTime() {
		return timer.getTime();
	}

	@Override
	public int getPoint() {
		return pointCounter.getPoint();
	}

	@Override
	public ArrayList<String> getTopCard(Components c) {
		return cardGameManager.getTopCard(c);
	}

}
