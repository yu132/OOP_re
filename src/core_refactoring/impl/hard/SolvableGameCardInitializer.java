package core_refactoring.impl.hard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import core_refactoring.Card;
import core_refactoring.CardInitializer;
import core_refactoring.CardNumber;
import core_refactoring.CardType;
import core_refactoring.Components;
import core_refactoring.MoveState;
import core_refactoring.impl.CardImpl;
import core_refactoring.util.RandomUniqueNumber;

/**
 * 获取一个可解牌局的牌初始化器 保证牌局可解 适用于3卡模式。但是3卡模式下可解的牌局，在1卡模式下也一定可解
 * 
 * @author 87663
 *
 */
public class SolvableGameCardInitializer implements CardInitializer {

	/**
	 * 一个可解的牌局
	 * 
	 * @author 87663
	 *
	 */
	public static class SolvableCardGame {

		/**
		 * 从数组到组件名称的映射
		 */
		private final static Map<Integer, Components> heapMap = new HashMap<>();

		/**
		 * 映射的初始化
		 */
		static {
			heapMap.put(0, Components.CARD_HEAP_1);
			heapMap.put(1, Components.CARD_HEAP_2);
			heapMap.put(2, Components.CARD_HEAP_3);
			heapMap.put(3, Components.CARD_HEAP_4);
			heapMap.put(4, Components.CARD_HEAP_5);
			heapMap.put(5, Components.CARD_HEAP_6);
			heapMap.put(6, Components.CARD_HEAP_7);
		}

		/**
		 * 定义操作的接口
		 * 
		 * @author 87663
		 *
		 */
		public static interface Operation {
		}

		/**
		 * 表示进行了一个NEXT操作
		 * 
		 * @author 87663
		 *
		 */
		public static class NextOperation implements Operation {
			@Override
			public String toString() {
				return "Next";
			}

			@Override
			public boolean equals(Object obj) {
				if (obj instanceof NextOperation)
					return true;
				else
					return false;
			}
		}

		/**
		 * 表示进行了一个MOVE操作，其中存了关于移动的一些信息
		 * 
		 * @author 87663
		 *
		 */
		public static class MoveOperation implements Operation {

			/**
			 * 移动的来源
			 */
			private Components from;

			/**
			 * 移动的目标
			 */
			private Components to;

			/**
			 * 移动的数量
			 */
			private int num;

			/**
			 * 获取移动的来源
			 * 
			 * @return 移动的来源
			 */
			public Components getFrom() {
				return from;
			}

			/**
			 * 获取移动的目标
			 * 
			 * @return 移动的目标
			 */
			public Components getTo() {
				return to;
			}

			/**
			 * 获取移动的数量
			 * 
			 * @return 移动的数量
			 */
			public int getNum() {
				return num;
			}

			public MoveOperation(Components from, Components to, int num) {
				super();
				this.from = from;
				this.to = to;
				this.num = num;
			}

			@Override
			public String toString() {
				return "Move" + " " + from + " " + to + " " + num;
			}

			@Override
			public boolean equals(Object obj) {
				if (obj instanceof MoveOperation) {
					if ((((MoveOperation) obj).from == from) && (((MoveOperation) obj).to == to)
							&& (((MoveOperation) obj).num == num)) {
						return true;
					}
				}
				return false;
			}
		}

		/**
		 * 存储索引和大小的类
		 * 
		 * @author 87663
		 *
		 */
		public static class IndexAndSize implements Comparable<IndexAndSize> {
			private int index;
			private int size;

			public IndexAndSize(int index, int size) {
				super();
				this.index = index;
				this.size = size;
			}

			@Override
			public int compareTo(IndexAndSize o) {
				return o.size - size;
			}
		}

		/**
		 * 所有用于初始化牌堆的牌
		 */
		private ArrayList<Card>[] CardHeap;

		/**
		 * 所有用于初始化发牌器的牌
		 */
		private ArrayList<Card> Dealer;

		/**
		 * 表示一个移动顺序，按照这个顺序移动就可以完成这个游戏
		 */
		private ArrayList<Operation> mvlist;

		/**
		 * 内部构造器
		 * 
		 * @param cardHeap
		 *            所有用于初始化牌堆的牌
		 * @param dealer
		 *            所有用于初始化发牌器的牌
		 * @param mvlist
		 *            移动顺序
		 */
		private SolvableCardGame(ArrayList<Card>[] cardHeap, ArrayList<Card> dealer, ArrayList<Operation> mvlist) {
			super();
			CardHeap = cardHeap;
			Dealer = dealer;
			this.mvlist = mvlist;
		}

		private static Random r = new Random();

		/**
		 * 初始化牌堆，给牌堆的后4列随机放上4列按混色排好的4组牌
		 * 
		 * @param CardHeap
		 */
		private static void initHeap(ArrayList<Card> CardHeap[]) {
			boolean f = true;// 间插的顺序标志

			for (int i = 12; i >= 0; i--) {

				if (f) {
					if (r.nextBoolean()) {
						CardHeap[3].add(CardImpl.valueOf(CardNumber.values()[i], CardType.CLUBS));
						CardHeap[4].add(CardImpl.valueOf(CardNumber.values()[i], CardType.SPADES));
					} else {
						CardHeap[3].add(CardImpl.valueOf(CardNumber.values()[i], CardType.SPADES));
						CardHeap[4].add(CardImpl.valueOf(CardNumber.values()[i], CardType.CLUBS));
					}

					if (r.nextBoolean()) {
						CardHeap[5].add(CardImpl.valueOf(CardNumber.values()[i], CardType.DIAMONDS));
						CardHeap[6].add(CardImpl.valueOf(CardNumber.values()[i], CardType.HEARTS));
					} else {
						CardHeap[5].add(CardImpl.valueOf(CardNumber.values()[i], CardType.HEARTS));
						CardHeap[6].add(CardImpl.valueOf(CardNumber.values()[i], CardType.DIAMONDS));
					}

					f = false;// 换顺序
				} else {
					if (r.nextBoolean()) {
						CardHeap[3].add(CardImpl.valueOf(CardNumber.values()[i], CardType.DIAMONDS));
						CardHeap[4].add(CardImpl.valueOf(CardNumber.values()[i], CardType.HEARTS));
					} else {
						CardHeap[3].add(CardImpl.valueOf(CardNumber.values()[i], CardType.HEARTS));
						CardHeap[4].add(CardImpl.valueOf(CardNumber.values()[i], CardType.DIAMONDS));
					}

					if (r.nextBoolean()) {
						CardHeap[5].add(CardImpl.valueOf(CardNumber.values()[i], CardType.CLUBS));
						CardHeap[6].add(CardImpl.valueOf(CardNumber.values()[i], CardType.SPADES));
					} else {
						CardHeap[5].add(CardImpl.valueOf(CardNumber.values()[i], CardType.SPADES));
						CardHeap[6].add(CardImpl.valueOf(CardNumber.values()[i], CardType.CLUBS));
					}

					f = true;// 换顺序
				}
			}
		}

		/**
		 * 模拟从牌堆移动到牌堆（反向移动）
		 * 
		 * @param CardHeap
		 *            表示7个牌堆
		 * @param coverNumber
		 *            盖住的牌的计数数组
		 * @param from
		 *            移动的来源
		 * @param to
		 *            移动的目标
		 * @param num
		 *            移动的数量
		 * @param mvlist
		 *            移动的顺序数组
		 * @return 移动成功与否
		 */
		private static boolean fromHeaptoHeap(ArrayList<Card> CardHeap[], int[] coverNumber, int from, int to, int num,
				ArrayList<Operation> mvlist) {

			if (num <= 0)// 如果移动的牌小于等于0，非法
				return false;

			if (CardHeap[from].size() - coverNumber[from] < num)// 如果来源牌堆的剩下的牌比要求移动的牌要少，则非法
				return false;

			if (CardHeap[from].size() - coverNumber[from] == 1)// 如果来源牌堆中仅剩一张牌，这种情况下不能移动
				return false;

			if (CardHeap[to].size() != coverNumber[to]) {// 如果目标牌堆中还有牌是没被盖上的
				if (!CardHeap[to].isEmpty()// 目标非空
						&& CardHeap[to].get(CardHeap[to].size() - 1).isStackableInHeap(
								CardHeap[from].get(CardHeap[from].size() - num)) != MoveState.SUCCESS)// 检查是否能够叠上去
					return false;// 如果不能叠上去，那么就返回失败
			}

			CardHeap[to].addAll(CardHeap[from]// 目标牌堆添加这些移动的牌
					.subList(CardHeap[from].size() - num, CardHeap[from].size()));

			for (int i = 0; i < num; i++) {// 来源牌堆删去这些移动的牌
				CardHeap[from].remove(CardHeap[from].size() - 1);
			}

			mvlist.add(new MoveOperation(heapMap.get(from), heapMap.get(to), num));// 移动成功，加入操作数组

			return true;// 返回成功
		}

		/**
		 * 盖住牌堆的牌
		 * 
		 * @param CardHeap
		 *            表示7个牌堆
		 * @param coverNumber
		 *            盖住的牌的计数数组
		 * @param x
		 *            需要盖住的牌堆的索引
		 * @return 是否成功盖住
		 */
		private static boolean coverHeapCard(ArrayList<Card> CardHeap[], int[] coverNumber, int x) {

			if (coverNumber[x] == x)// 如果要盖住的牌已经够了
				return false;// 返回失败

			if (CardHeap[x].size() == coverNumber[x] + 1) {// 如果总牌数比盖上的牌数多一张
				coverNumber[x]++;// 盖上
				return true;// 返回成功
			}

			return false;// 否则返回失败
		}

		/**
		 * 检查是否能盖住
		 * 
		 * @param CardHeap
		 *            表示7个牌堆
		 * @param coverNumber
		 *            盖住的牌的计数数组
		 * @param x
		 *            需要检查的牌堆的索引
		 * @return 是否能盖住
		 */
		private static boolean isHeapCoverableMax(ArrayList<Card> CardHeap[], int[] coverNumber, int x) {
			if (coverNumber[x] == x)
				return false;
			else
				return true;
		}

		/**
		 * 模拟从牌堆移动到发牌器（反向移动）
		 * 
		 * @param CardHeap
		 *            表示7个牌堆
		 * @param coverNumber
		 *            盖住的牌的计数数组
		 * @param tempDealer
		 *            表示发牌器
		 * @param from
		 *            来源牌堆的索引
		 * @param to
		 *            表示发牌器中第几个组（3个为一组）
		 * @param togiveNum
		 *            表示这一轮中发牌器中每组需要被给的牌的数量
		 * @param lastGroup
		 *            表示这一轮移动最后一组的索引
		 * @param mvlist
		 *            移动的顺序数组
		 * @return 移动是否成功
		 */
		private static boolean fromHeapToDealer(ArrayList<Card> CardHeap[], int[] coverNumber, Card[][] tempDealer,
				int from, int to, int[] togiveNum, int lastGroup, ArrayList<Operation> mvlist) {
			if (CardHeap[from].size() - coverNumber[from] <= 1) {// 如果排队中剩余的牌不足以移动
				return false;// 返回失败
			}

			Card c = CardHeap[from].remove(CardHeap[from].size() - 1);// 如果够牌，移动一定成功，所以先从牌堆中移除这张牌

			if (to != lastGroup)// 如果不是最后一组
				tempDealer[to][3 - (togiveNum[to]--)] = c;// 从前向后依次加入牌
			else {// 如果是最后一组
				for (int i = 0; i < 3; i++) {// 从前向后添加，因为可能不满，如果按照上面的添加方法就可能会导致前面有空位
					if (tempDealer[to][i] == null) {// 检查是否为空
						tempDealer[to][i] = c;// 如果是空位，就放进去
						togiveNum[to]--;// 要给的数量减一
						break;
					}
				}
			}
			mvlist.add(new MoveOperation(heapMap.get(from), Components.DEALER, 1));// 移动成功，加入操作数组
			return true;
		}

		/**
		 * 表示把发牌器中的牌准备好之后，再整理牌堆中的牌 使得这7个牌堆都能到达只有一张牌亮着，其余都是盖上的
		 * 而且盖上的牌恰好是和他们的索引相同（从0开始算的话）
		 * 
		 * @param CardHeap
		 *            表示7个牌堆
		 * @param coverNumber
		 *            盖住的牌的计数数组
		 * @param mvlist
		 *            移动的顺序数组
		 * @return 表示是否能够整理成功，如果整理不成功的话，就需要重新生成游戏
		 */
		private static boolean organizeHeap(ArrayList<Card> CardHeap[], int[] coverNumber,
				ArrayList<Operation> mvlist) {

			// 牌堆的状态数组
			int[] need = new int[7];// 表示一个牌堆需要的牌
			int[] togive = new int[7];// 表示一个牌堆有几张牌是亮着的
			boolean[] finish = new boolean[7];// 表示这个牌堆是否已经完成（到达目标状态）

			RandomUniqueNumber ruH1 = new RandomUniqueNumber(0, 6);// 移动的来源

			RandomUniqueNumber ruH2 = new RandomUniqueNumber(1, 6);// 移动的目标，至于为什么第一个不能移入
																	// 是因为为了防止卡死，就留一个牌堆进行特殊的操作
																	// 来防止卡死

			// 计算上面几个状态数组的值
			for (int i = 0; i < 7; i++) {
				need[i] = i - coverNumber[i];
				togive[i] = CardHeap[i].size() - coverNumber[i];
				if (need[i] == 0 && togive[i] == 1)
					finish[i] = true;
			}

			boolean kasi = true;// 表示牌堆的整理被卡死了的标记

			while (true) {// 无限循环整理牌堆，直到整理完了跳出

				if (finish[0]) {// 第一个牌堆总是很好整理，但是很多时候需要第一个牌堆来运牌，所以在其他的牌堆还没有完成时
								// 暂时不能让第一个牌堆呈完成状态，因为完成的牌堆就不再移动了
					finish[0] = finish[1] && finish[2] && finish[3] && finish[4] && finish[5] && finish[6];
				}

				boolean flag = true;// 检查整理是否已经完成了的标记

				for (int i = 1; i < 7; i++)// 如果完成的话就设为真，否则为假
					if (!finish[i]) {
						flag = false;
						break;
					}

				if (flag)// 如果完成
					break;// 就退出

				int h1 = 0;// 表示来源的牌堆的索引
				int h2 = 0;// 表示目标的牌堆的索引

				ruH1.reSet();// 重置来源的选择
				l: for (int x = 0; x < 7; x++) {

					boolean flagx = r.nextBoolean();// 表示随机的两种状态

					h1 = ruH1.getNum();// 随机抽取 来源牌堆

					if (h1 == 0)// 因为 牌堆一 限制只能一次移动一张
						flagx = false;// 所以设置成第二种

					if (finish[h1])// 如果来源已经完成了
						continue;// 就跳过

					if (togive[h1] <= 1)// 如果来源能给的牌少于两张
						continue;// 也跳过

					ruH2.reSet();// 重置目标的选择
					for (int y = 1; y < 7; y++) {

						h2 = ruH2.getNum();// 随机选取 目标牌堆

						if (h1 == h2)// 如果来源和目标是相同的
							continue;// 就跳过

						if (finish[h2])// 如果目标已经完成
							continue;// 也跳过

						int num = flagx ? CardHeap[h1].size() - coverNumber[h1] - 1 : 1;// 根据模式选取移动的数量

						if (fromHeaptoHeap(CardHeap, coverNumber, h1, h2, num, mvlist)) {// 模拟移动

							if (flagx) {// 选择少牌的那部分进行盖牌
								coverHeapCard(CardHeap, coverNumber, h1);
							} else {
								coverHeapCard(CardHeap, coverNumber, h2);
							}

							// 重算状态数组的值
							togive[h1] = CardHeap[h1].size() - coverNumber[h1];
							need[h1] = h1 - coverNumber[h1];

							togive[h2] = CardHeap[h2].size() - coverNumber[h2];
							need[h2] = h2 - coverNumber[h2];

							if (need[h1] == 0 && togive[h1] == 1)
								finish[h1] = true;
							if (need[h2] == 0 && togive[h2] == 1)
								finish[h2] = true;
							// 重算状态数组的值

							kasi = false;// 移动过应该没有卡死

							// 但是如果是往复移动，证明肯定是卡死了
							if (mvlist.get(mvlist.size() - 1).equals(mvlist.get(mvlist.size() - 3)))
								kasi = true;

							break l;// 完成过一次移动
						}
					}
				}

				if (kasi) {// 如果卡死了，则需要做特殊处理

					boolean cover = false;// 检查是否有牌能被盖上的标记

					for (int i = 1; i < 7; i++)// 检查是否有牌能被盖上
						if (coverHeapCard(CardHeap, coverNumber, i))
							cover = true;

					if (!cover) {// 如果没有牌能被盖上，则需要做别的处理

						// 统计每个牌堆中明牌的数量
						ArrayList<IndexAndSize> slist = new ArrayList<>();
						for (int i = 1; i < 7; i++) {
							slist.add(new IndexAndSize(i, CardHeap[i].size() - coverNumber[i]));
						}

						Collections.sort(slist);// 按明牌数量进行逆向排序

						boolean faliure = true;// 处理失败的标记，如果失败了，则是真的卡死了，只能重新生成游戏了

						for (int x = 0; x < 6; x++) {
							h1 = slist.get(x).index;// 优先处理牌多的

							if (finish[h1])// 如果牌堆完成了，则不需要处理
								continue;

							if (togive[h1] <= 1)// 如果牌堆没有可以移动的，也不需要处理
								continue;

							h2 = 0;// 把多的牌全移动到牌堆1，来进行周转
							if (fromHeaptoHeap(CardHeap, coverNumber, h1, h2, CardHeap[h1].size() - coverNumber[h1] - 1,
									mvlist)) {// 模拟移动

								faliure = false;// 如果移动成功了，则没有失败

								coverHeapCard(CardHeap, coverNumber, h1);// 把来源的牌盖上

								// 重算状态数组的值
								togive[h1] = CardHeap[h1].size() - coverNumber[h1];
								need[h1] = h1 - coverNumber[h1];

								togive[h2] = CardHeap[h2].size() - coverNumber[h2];
								need[h2] = h2 - coverNumber[h2];

								if (need[h1] == 0 && togive[h1] == 1)
									finish[h1] = true;
								if (need[h2] == 0 && togive[h2] == 1)
									finish[h2] = true;
								// 重算状态数组的值
							}

						}

						if (faliure)// 如果卡死了，就返回失败，这个牌堆无法整理，或者整理的时候遇到了无法解决的卡死问题
									// 这个时候就需要重新生成牌局
							return false;// 返回失败
					}
				} else {// 如果没卡死
					kasi = true;// 把卡死设置成默认值
				}

			}

			return true;// 如果成功整理，就返回成功

		}

		/**
		 * 整理发牌器，其实就是在发牌器中找位置放新的卡，一轮一轮做，直到做完为之（发牌器中抽空）
		 * 
		 * @param tempDealer
		 *            表示发牌器
		 * @param togiveNum
		 *            表示这一轮中发牌器中每组需要被给的牌的数量
		 * @param allDealerNum
		 *            表示发牌器还需要几张牌（总共，不是一轮）
		 * @return 数组 [0]-> allDealerNum 表示发牌器还需要几张牌(总共，不是一轮) [1]-> (groupNext -
		 *         1) 表示总共的组数少一（就是这一轮最后一组的索引）
		 */
		private static int[] organizeDealer(Card[][] tempDealer, int[] togiveNum, int allDealerNum) {

			int[] ret = new int[2];// 表示两个返回值

			int x = 0;// 计算有几个位置是有牌的
			int group = 0;// 表示组的数量

			// 计算有牌位置的数量
			l: for (int i = 1; i <= 8; i++) {
				group = i;
				for (int j = 0; j < 3; j++) {
					if (tempDealer[i - 1][j] == null)
						break l;
					else
						x++;
				}
			}

			int temp1 = r.nextInt(1050) + 1;// 随机数，用于下面抽取

			int groupNext;// 下一次组的数量
			int addGroup;// 添加组的数量

			// 根据随机数抽取下一组的添加组的数量
			if (temp1 < 300)
				addGroup = 0;

			else if (temp1 < 700)
				addGroup = 1;

			else if (temp1 < 990)
				addGroup = 2;

			else if (temp1 < 995)
				addGroup = 3;

			else if (temp1 < 997)
				addGroup = 4;

			else if (temp1 < 998)
				addGroup = 5;

			else if (temp1 < 999)
				addGroup = 6;

			else {// 表示这次发牌器中不添加牌，继续进行牌堆移动
				for (int i = 0; i < togiveNum.length; i++)
					togiveNum[i] = 0;

				ret[0] = allDealerNum;
				ret[1] = -1;

				return ret;
			}

			// 如果添加的组数过多
			if (group + addGroup >= 8)
				groupNext = 8;// 就把组数设成上限
			else
				groupNext = group + addGroup;// 否则就是直接加

			int nextCardNumber = (groupNext - 1) * 3 + r.nextInt(3) + 1;// 计算需要多少张牌

			if (nextCardNumber > 24)// 如果下一次的牌过多
				nextCardNumber = 24;// 设为上限

			if (nextCardNumber <= x) {// 如果算完牌的数量还少了
				if (r.nextBoolean()) {// 50%设成多1张
					nextCardNumber = x + 1;
				} else {
					if (r.nextBoolean()) {// 25%设成多两张
						nextCardNumber = x + 2;
					} else {// 25%直接返回不做操作
						for (int i = 0; i < togiveNum.length; i++)
							togiveNum[i] = 0;

						ret[0] = allDealerNum;
						ret[1] = -1;

						return ret;
					}
				}
			}

			int orgNum = nextCardNumber - x;// 新加的牌

			int xx = ((nextCardNumber - 1) / 3 + 1);// 重新算的组的数量，因为上面可能会有加牌，可能会多出一组来

			if (xx < 8)// 计算出来的组，如果过多了，则设成上限
				groupNext = xx;
			else
				groupNext = 8;

			// 重算发牌器所需要的牌
			if (allDealerNum - orgNum >= 0)
				allDealerNum -= orgNum;
			else {
				orgNum = allDealerNum;
				allDealerNum = 0;
			}

			int leave = nextCardNumber - (groupNext - 1) * 3;// 最后一组最大的容量空间

			// 对前面的组进行整理，除了最后一组
			if (groupNext - 2 >= 0) {
				RandomUniqueNumber ru = new RandomUniqueNumber(0, groupNext - 2);// 抽取前面除了最后一组的组

				while (true) {// 整理直到完成
					ru.reSet();// 重置

					// 表示一次整理，但是一次整理可能完不成
					for (int i = 0; i < groupNext - 1; i++) {

						int g = ru.getNum();// 随机选取抽取顺序

						int next = r.nextInt(orgNum + 1 > 4 ? 4 : orgNum + 1);// 抽下一次的这一组新加的牌的数量

						if (next > orgNum) {// 如果抽的数量多了，就减少
							next = orgNum;
						}

						if (togiveNum[g] + next > 3) {// 如果要给的牌牌大于这一组的容纳量，就补充到最大
							orgNum = orgNum + togiveNum[g] - 3;
							togiveNum[g] = 3;
						} else {// 如果没到最大容纳量，就直接相加就可以了
							orgNum -= next;
							togiveNum[g] += next;
						}

					}

					if (orgNum <= leave) {// 如果剩下的牌能全给最后一组
						if (r.nextBoolean())// 50%的概率不再给前面的组
							break;
					}
				}

			}
			togiveNum[groupNext - 1] = orgNum;// 最后一组新得的牌就是剩下的牌

			Card[][] tempDealerNew = new Card[8][3];// 重组发牌器

			int inxde1 = 0;// 表示旧发牌器的组的索引
			int indexIn = 0;// 表示旧发牌器的组内索引

			for (int i = 0; i < groupNext - 1; i++) {// 表示新的发牌器的组
				for (int j = 0; j < 3 - togiveNum[i]; j++) {// 表示新的发牌器的组内的牌
					if (indexIn == 3) {// 旧发牌器的组内索引到了3，就移动到下一组
						indexIn = 0;
						inxde1++;
					}
					tempDealerNew[i][j] = tempDealer[inxde1][indexIn++];// 把旧发牌器的牌按上面的空间移动新的发牌器中
					x--;// 剩下的牌减一
				}
			}

			// 最后一组做特殊处理
			for (int i = 0; i < x; i++) {
				if (indexIn == 3) {
					indexIn = 0;
					inxde1++;
				}
				tempDealerNew[groupNext - 1][i] = tempDealer[inxde1][indexIn++];
			}

			ret[0] = allDealerNum;// 返回值赋值
			ret[1] = groupNext - 1;// 返回值赋值

			// 把新发牌器中重新整理好的牌的顺序重新写回旧的对象中
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 3; j++)
					tempDealer[i][j] = tempDealerNew[i][j];

			return ret;// 返回返回值
		}

		/**
		 * 获取一个可解游戏
		 * 
		 * @return 一个可解游戏
		 */
		private static SolvableCardGame getASolvableCardGamePro() {

			ArrayList<Operation> mvlist = new ArrayList<>();//一个存储移动顺序的数组

			@SuppressWarnings("unchecked")
			ArrayList<Card>[] CardHeap = new ArrayList[7];//表示7个牌堆

			int[] coverNumber = new int[7];//盖住的牌的计数数组

			//初始化牌堆
			for (int i = 0; i < 7; i++) {
				CardHeap[i] = new ArrayList<>();
			}

			int allDealerNum = 24;//发牌器需要的牌数

			RandomUniqueNumber ruH1 = new RandomUniqueNumber(1, 6);//后六个牌堆
			RandomUniqueNumber ruH2 = new RandomUniqueNumber(1, 6);//后六个牌堆

			Card[][] tempDealer = new Card[8][3];//表示发牌器

			int[] togiveNum = new int[8];//表示每次发牌器每组要获得的牌

			initHeap(CardHeap);//初始化牌堆

			while (allDealerNum != 0) {//给发牌器牌直到24张发够

				int[] ret = organizeDealer(tempDealer, togiveNum, allDealerNum);//先腾出空间用于放牌

				allDealerNum = ret[0];//更新需要牌的数量

				for (int i = 7; i >= 0; i--) {//发牌器是从后向前给牌

					int tt = togiveNum[i];//这组需要的牌的数量
					for (int j = 0; j < tt; j++) {//给需要数量的牌

						int temp = 0;
						
						while (temp < 50) {//做完第一次之后有50%概率跳出
							int temp2 = r.nextInt(100);//随机数
							
							if (temp2 < 40) {//40%概率，移动可移动的全部牌
								
								ruH1.reSet();//重置来源牌堆
								l: for (int x = 1; x <= 6; x++) {
									
									int h1 = ruH1.getNum();//随机抽取来源牌堆
									
									ruH2.reSet();//重置目标牌堆
									for (int y = 1; y <= 6; y++) {
										
										int h2 = ruH2.getNum();//随机抽取目标牌堆
										
										if (h1 == h2)//如果来源和目标相同，就跳过
											continue;

										int num = CardHeap[h1].size() - coverNumber[h1] - 1;//移动可移动的全部牌
										
										if (isHeapCoverableMax(CardHeap, coverNumber, h1)//检查是否移动成功
												&& fromHeaptoHeap(CardHeap, coverNumber, h1, h2, num, mvlist)) {
											
											//两者移动完都可能只剩一张牌，能盖住就盖住
											coverHeapCard(CardHeap, coverNumber, h1);
											coverHeapCard(CardHeap, coverNumber, h1);

											break l;
										}
									}
								}
								
							} else if (temp2 < 80) {//40%概率，只移动一张牌，逻辑与上面大体相同
								
								ruH1.reSet();
								l: for (int x = 1; x <= 6; x++) {
									
									int h1 = ruH1.getNum();
									
									ruH2.reSet();
									for (int y = 1; y <= 6; y++) {
										
										int h2 = ruH2.getNum();
										
										if (h1 == h2)
											continue;
										
										if (isHeapCoverableMax(CardHeap, coverNumber, h2)
												&& fromHeaptoHeap(CardHeap, coverNumber, h1, h2, 1, mvlist)) {
											
											coverHeapCard(CardHeap, coverNumber, h1);
											coverHeapCard(CardHeap, coverNumber, h2);

											break l;
										}
									}
								}

							} else if (temp2 < 95) {//15%概率，移动一半能移动的牌
								
								ruH1.reSet();
								l: for (int x = 1; x <= 6; x++) {
									
									int h1 = ruH1.getNum();
									
									ruH2.reSet();
									
									if (CardHeap[h1].size() - coverNumber[h1] == 1)//没牌可移，不移
										continue;

									for (int y = 1; y <= 6; y++) {
										
										int h2 = ruH2.getNum();
										
										if (h1 == h2)
											continue;
										
										int num = (CardHeap[h1].size() - coverNumber[h1]) / 2;
										
										if (num == 1)//如果只有一张牌，就不移了
											continue;
										
										if (fromHeaptoHeap(CardHeap, coverNumber, h1, h2, num, mvlist)) {
											coverHeapCard(CardHeap, coverNumber, h1);
											coverHeapCard(CardHeap, coverNumber, h2);

											break l;
										}
									}
								}
							}
							//5%概率什么都不做

							temp = r.nextInt(100);//随机下一次的行为
						}

						//随机抽取牌堆向发牌器给牌
						
						ruH1.reSet();//重置牌堆的抽取
						for (int x = 1; x <= 6; x++) {
							
							int from = ruH1.getNum();//随机选取牌堆给牌

							if (CardHeap[from].size() - coverNumber[from] == 1)//检查是否有牌可移，没有就不移动
								continue;

							if (fromHeapToDealer(CardHeap, coverNumber, tempDealer, from, i, togiveNum, ret[1],
									mvlist)) {//检查是否能给牌
								
								coverHeapCard(CardHeap, coverNumber, from);//给完牌检查是否只剩一张，如果只剩一张就盖住

								break;
							}
						}
						
					}

					mvlist.add(new NextOperation());//每移动完一次就NEXT一次
				}
			}

			//做完之后建立发牌器，把临时的值传进来
			ArrayList<Card> Dealer = new ArrayList<>();
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 3; j++) {
					Dealer.add(tempDealer[i][j]);
				}
			}

			//整理牌堆，如果整理不成功，就返回失败
			if (!organizeHeap(CardHeap, coverNumber, mvlist))
				return null;

			//否则就返回一个可完成的游戏
			return new SolvableCardGame(CardHeap, Dealer, mvlist);
		}

		/**
		 * 获取一个可解的游戏
		 * @return 可解的游戏 
		 */
		public static SolvableCardGame getASolvableCardGame() {
			
			SolvableCardGame ans = getASolvableCardGamePro();//获取一个可完成的游戏
			
			while (ans == null)//如果获取不成功，就重新获取，一般来说两次之内能够获得一个可解的游戏
				ans = getASolvableCardGamePro();
			
			Collections.reverse(ans.mvlist);

			for (int i = 0; i < 7; i++)//牌堆整理完之后和发牌的顺序相反，所以要反向
				Collections.reverse(ans.CardHeap[i]);

			return ans;//返回一个真正必定可解的游戏
		}

	}

	//先获取一个可解的游戏
	private SolvableCardGame scg = SolvableCardGame.getASolvableCardGame();

	//下一个应该给的发牌器的牌的索引
	private int dealerIndex = 0;

	//下一个应该给的牌堆的牌的全部索引
	private int[] heapIndexes = new int[7];

	@Override
	public Card getCard(Components c) {

		switch (c) {//对于每个组件，分别发牌
		case DEALER:
			return scg.Dealer.get(dealerIndex++);
		case CARD_HEAP_1:
			return scg.CardHeap[0].get(heapIndexes[0]++);
		case CARD_HEAP_2:
			return scg.CardHeap[1].get(heapIndexes[1]++);
		case CARD_HEAP_3:
			return scg.CardHeap[2].get(heapIndexes[2]++);
		case CARD_HEAP_4:
			return scg.CardHeap[3].get(heapIndexes[3]++);
		case CARD_HEAP_5:
			return scg.CardHeap[4].get(heapIndexes[4]++);
		case CARD_HEAP_6:
			return scg.CardHeap[5].get(heapIndexes[5]++);
		case CARD_HEAP_7:
			return scg.CardHeap[6].get(heapIndexes[6]++);
		default:
			return null;
		}
		
	}

}