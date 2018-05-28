package core_refactoring.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import core_refactoring.*;

/**
 * 牌堆类的实现类
 * @author 
 */
public class CardHeapImpl implements CardHeap{
	
	Components thisc;
	/**
	 * 保存未翻开纸牌数目
	 */
	private int unopenedCard;

	/**
	 * 保存牌堆中纸牌总数
	 */
	private int totalNumber;
	
	/**
	 * 记录初始化时纸牌总数
	 */
	private int start_totalNumber;
	
	/**
	 * 标记当前操作是否有新的纸牌翻开
	 */
	private boolean openNew=false;
	
	/**
	 * 生成纸牌
	 */
	CardInitializer generate;
	
	/**
	 * 构造函数
	 */
	public CardHeapImpl(int number,CardInitializer a,Components thisc){
		this.thisc=thisc;
		unopenedCard = number-1;
		start_totalNumber=totalNumber = number;
		generate = a;
		Card begin = a.getCard(thisc);
		cardStack.push(begin);
		openedcard2.push(begin);
		openstate.push(false);
	}
	
	/**
	 * 一个栈，用于存储当前在该牌堆内的卡牌
	 */
	private Deque<Card> cardStack=new ArrayDeque<>();
	
	/**
	 * 一个栈，用于存储每一次对这个牌堆操作前的快照
	 */
	private Deque<String> snapshot=new ArrayDeque<>();
	
	/**
	 * 一个栈，用于存储翻开过的纸牌
	 */
	private Deque<Card> openedcard=new ArrayDeque<>();
	
	/**
	 * 一个栈，用于存储翻开过的所有纸牌 
	 */
	private Deque<Card> openedcard2=new ArrayDeque<>();
	/**
	 * 一个栈，用于维护翻牌状态
	 */
	private Deque<Boolean> openstate=new ArrayDeque<>();
	/**
	 * 内部方法，获取当前牌堆内卡牌的快照，用于撤销操作
	 * @return 当前卡牌的快照，以String方式存储
	 */
	private String getSnapshot(){
		StringBuilder sb=new StringBuilder(cardStack.size()*20);
		boolean f=true;
		for(Card c:cardStack){//依次向字符串构造器中加入卡牌的信息
			if(f){
				f=false;
				sb.append(c.toString());
			}else
				sb.append(" "+c.toString());
		}
		return sb.toString();
	}
	
	
	@Override
	public MoveState sentSingleCard(Component c) {
		if(cardStack.isEmpty())//如果空，则不可移动
			return MoveState.ILLEGAL_MOVE;
		
		Card topCard=cardStack.peek();//得到牌堆顶部的牌
		MoveState ms=c.getSingleCard(topCard);//判断是否移动成功
		if(ms==MoveState.SUCCESS){//如果可以移动成功进行的一系列操作
			if(totalNumber-1 == unopenedCard ){//如果当前牌堆只翻开一张牌
				
				totalNumber--;//牌堆内牌的总数减1
				snapshot.push(getSnapshot());//加入一个操作快照
				cardStack.pop();//弹出移走的牌
				
				if(unopenedCard>0&&openedcard.isEmpty()){//如果当前还有未被翻开的牌并且openedcard缓存里没有牌
					Card temp = generate.getCard(thisc);//生成新的牌面
					cardStack.push(temp);
					openedcard2.push(temp);
					openNew=true;//状态更新
					unopenedCard--;
				}else if(unopenedCard>0&&!openedcard.isEmpty()){//如果当前还有未被翻开的牌并且openedcard缓存里有牌
					Card temp = openedcard.pop();//将openedcard里的牌弹出
					cardStack.push(temp);
					openNew=true;//状态更新
					unopenedCard--;
				}else{
					openNew=false;//状态更新
				}
				
				
			}else{//当前不止一张牌被翻开
				totalNumber--;
				snapshot.push(getSnapshot());//加入一个操作快照
				cardStack.pop();
				openNew=false;//状态更新
			}
		openstate.push(openNew);//状态更新
		}
		
		
		return ms;
	}

	@Override
	public MoveState getSingleCard(Card card) {
		if(cardStack.isEmpty()){//如果当前牌堆为空
			if(card.getCardNumber()==CardNumber.KING){//只允许接受的牌面为K
				snapshot.push(getSnapshot());//加入一个操作快照
				cardStack.push(card);
				totalNumber++;
				openNew=false;//状态更新
				openstate.push(openNew);
				return MoveState.SUCCESS;
			}else
				return MoveState.HEAP_TOP_IS_NOT_K;
		}
		
		Card topCard=cardStack.peek();//得到牌堆顶部的牌
		MoveState temp=topCard.isStackableInHeap(card);//判断是否移动成功
		if(temp==MoveState.SUCCESS){//如果移动成功
			snapshot.push(getSnapshot());//加入一个操作快照
			cardStack.push(card);
			totalNumber++;
			openNew=false;//状态更新
			openstate.push(openNew);
			return MoveState.SUCCESS;
		}else
			return temp;
	}

	@Override
	public MoveState sentCards(Component c, int number) {
		
		if(cardStack.isEmpty()||totalNumber-unopenedCard<number)//如果当前牌堆为空或者移动牌的数目大于翻开的牌的数目，则移动失败
			return MoveState.ILLEGAL_MOVE;
		ArrayList<Card> li = new ArrayList<>();//存放移动的牌序列
		Card topCard=cardStack.peek();//从小到大，循环完为最大数字牌面
		snapshot.push(getSnapshot());//加入一个操作快照
		for(int i =0;i<number;i++){
			topCard = cardStack.pop();
			li.add(0, topCard);
		}
		MoveState ms=c.getCards(li);//判断是否成功
		if(ms==MoveState.SUCCESS){//如果移动成功
			totalNumber-=number;
			if(totalNumber==unopenedCard){//判断是否有需要新翻开的牌
				if(unopenedCard>0){//需要翻牌
					unopenedCard--;
					openNew=true;//更新状态
					openstate.push(openNew);
					if(openedcard.isEmpty()){//生成新的牌
						Card temp = generate.getCard(thisc);
						cardStack.push(temp);//更新状态
						openedcard2.push(temp);
					}else{//从openedcard里弹出来一张牌
						Card temp = openedcard.pop();
						cardStack.push(temp);
					}
				}else{
					openNew = false;//更新状态
					openstate.push(openNew);
				}
			}else{
				openNew = false;//更新状态
				openstate.push(openNew);
			}
		}else{//不成功则还原之前快照
			snapshot.pop();
			for(int i =0;i<number;i++){
				topCard = li.get(i);
				cardStack.push(topCard);;
			}
		}
		return ms;
		
	}

	@Override
	public MoveState getCards(ArrayList<Card> cards) {
		if(cardStack.isEmpty()){//当前牌堆为空
			if(cards.get(0).getCardNumber()==CardNumber.KING){//只接受顶部为K的牌序列
				snapshot.push(getSnapshot());//加入一个操作快照
				for(int i =0;i<cards.size();i++){
					cardStack.push(cards.get(i));
				}//添加牌到cardStack
				totalNumber+=cards.size();
				openNew=false;//更新状态
				openstate.push(openNew);//加入一个操作快照
				return MoveState.SUCCESS;
			}else
				return MoveState.HEAP_TOP_IS_NOT_K;
		}
		
		Card topCard=cardStack.peek();//获得牌堆顶部纸牌
		MoveState temp=topCard.isStackableInHeap(cards.get(0));//判断移动状态
		if(temp==MoveState.SUCCESS){//可以移动
			snapshot.push(getSnapshot());//加入一个操作快照
			for(int i =0;i<cards.size();i++){
				cardStack.push(cards.get(i));
			}
			totalNumber+=cards.size();
			openNew=false;//更新状态
			openstate.push(openNew);
			return MoveState.SUCCESS;
		}else
			return temp;
	}

	@Override
	public ArrayList<String> getTopCard() {//获取顶部的牌
		ArrayList<String> temp=new ArrayList<>();//存放获取的牌序
		
		for(Card c:cardStack){
			temp.add(c.toString());
		}
		
		return temp;//并返回这个数组
	}

	@Override
	public ArrayList<String> getAllCard() {//获得全部牌，包括为未开的牌
		ArrayList<String> temp=new ArrayList<>();//存放获取的牌序
		for(Card c:cardStack){
			temp.add(c.toString());
			
		}
		for(int i=0;i<unopenedCard;i++){
			temp.add("null");
		}
		return temp;//并返回这个数组
	}

	@Override
	public boolean ismovable(int index) {//判断能否移动
		if (index>totalNumber-unopenedCard){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean undo() {//撤销操作
		if(snapshot.isEmpty())//如果快照为空则不可操作
			return false;
		
		String last=snapshot.pop();
		openNew = openstate.pop();//获取上一次的快照
		if(openNew){//如果撤销的操作有新翻开牌
			unopenedCard++;
			if(!cardStack.isEmpty())
				openedcard.push(cardStack.peek());
		}
		cardStack.clear();
		
		if(!last.equals("")){//将快照内保存的牌序还原回去
			String[] cards=last.split(" ");
			int visiableNumber = cards.length;
			for(int i=0;i<cards.length;i++)
				cardStack.push(CardImpl.valueOf(cards[cards.length-1-i]));
			totalNumber = unopenedCard+visiableNumber;
		}else{//牌堆为空
			totalNumber=0;
		}

		return true;
	}

	@Override
	public boolean undoAll() {
		if(snapshot.isEmpty())//如果快照为空则不可操作
			return false;
		
		String last=snapshot.peekLast();//得到最原始的快照
		//更新状态
		cardStack.clear();
		snapshot.clear();
		openedcard.clear();
		openstate.clear();
		openstate.push(false);
		String[] cards=last.split(" ");
		//将快照内保存的牌序还原回去
		for(int i=0;i<cards.length;i++)
			cardStack.push(CardImpl.valueOf(cards[i]));
		unopenedCard = start_totalNumber-1;
		totalNumber = start_totalNumber;
		
		for(Card c:openedcard2){
			openedcard.push(c);
		}
		
		return true;
	}

	@Override
	public boolean openCardLastRound() {
		
		return openstate.peek();
	}

}
