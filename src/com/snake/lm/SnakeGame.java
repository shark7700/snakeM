package com.snake.lm;

import java.util.ArrayList;

public class SnakeGame {

	// класс определяющий позицию
	public class pos {
		int x;
		int y;
		
		//конструктор 
		pos(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	// Константы направления
	public static final int DIR_NORTH = 1;
	public static final int DIR_EAST = 2;
	public static final int DIR_SOUTH = 3;
	public static final int DIR_WEST = 4;
	
	
	// ширина и высота игрового поля
	// подбиралось исходя из пропорций экрана моего девайса
	public static int mFieldX = 18;
	public static int mFieldY = 30;
	
	// Очки в игре
	public int mScore=0;

	// Матрица - игровое поле
	private int mField[][] = new int[mFieldX][mFieldY];
	
	// Сама змея - массив двумерных координат каждого сегмента
	public ArrayList<pos> mSnake = new ArrayList<pos>();

	// Текущее напраление движения змеи
	int mDirection = SnakeGame.DIR_EAST;
	 
	// Собственно конструктор
	SnakeGame() {
		// очищаем игровое поле
		for (int i = 0; i < mFieldX; i++)
			for (int j = 0; j < mFieldY; j++) {
				mField[i][j] = 0;
			}
		// создаем змею
		mSnake.add(new pos(8, 10));
		// каждая клетка поля в которой 
		// находится змея - отмечается -1
		mField[2][2] = -1;
		mSnake.add(new pos(8, 11));
		mField[2][3] = -1;
		mSnake.add(new pos(8, 12));
		mField[2][4] = -1;
		
		//стенки
		mField[3][3] = 1;
		mField[3][4] = 1;
		mField[3][5] = 1;
		mField[3][6] = 1;
		mField[3][7] = 1;
		mField[3][8] = 1;
		
		mField[14][3] = 1;
		mField[14][4] = 1;
		mField[14][5] = 1;
		mField[14][6] = 1;
		mField[14][7] = 1;
		mField[14][8] = 1;
		
		mField[3][27] = 1;
		mField[3][26] = 1;
		mField[3][25] = 1;
		mField[3][24] = 1;
		mField[3][23] = 1;
		mField[3][22] = 1;
		
		mField[14][27] = 1;
		mField[14][26] = 1;
		mField[14][25] = 1;
		mField[14][24] = 1;
		mField[14][23] = 1;
		mField[14][22] = 1;
		// добавляем на игровое поле фрукт
		addFruite();
	}

	// метод добавляет фрукт на игровое поле 
	// поскольку игра у нас самая простая то
	// и фрукт только один а его код на поле - 2
	private void addFruite() {
		boolean par = false;
		while (!par) {
			int x = (int) (Math.random() * mFieldX);
			int y = (int) (Math.random() * mFieldY);
			if (mField[x][y] == 0) {
				mField[x][y] = 2;
				par = true;
			}
		}
		//mField[9][7] = 2;
		//mField[9][5] = 2;
		//mField[9][3] = 2;
		//mField[9][1] = 2;
	}


	// Этот метод соержит в себе всю логику игры
		// здесь опиываются все действия которые должны происходить
		// при каждом перемещении змеи
		// при этом, учитывается текущее направление и 
		// проверяется, может ли змея ходить в указанном направлении
		// собственно вся игровая логика заключена в этом методе
		public boolean nextMove() {
			// смотрим, куда у нас направлена змея сейчас
			switch (this.mDirection) {
			// если на север
			case DIR_NORTH: {
				// тогда рассчитываем координаты в которые попадет
				// голова змеи на следуюзщем ходу
				int nextX = mSnake.get(mSnake.size() - 1).x;
				int nextY = mSnake.get(mSnake.size() - 1).y - 1;
				//мы на экране
				if (nextY >= 0){
				
					if( mField[nextX][nextY] == 0) {
						mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
						mSnake.remove(0);
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						return true;
					
					} else if (mField[nextX][nextY] == 1) {
						return false;
					
					} else if (mField[nextX][nextY] > 1) {
						mScore=mScore+10;
						mField[nextX][nextY] = 0;
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						addFruite();
						return true;
					}
				}  
			
				//мы за экраном			
				if ((nextY > mFieldY)||(nextY <0)) {
					nextY = mFieldY-1;
				
					if(mField[nextX][nextY] == 0){
						mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
						mSnake.remove(0);
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						return true;
						
					}else if(mField[nextX][nextY] == 1) {
						return false;
					
					}else if(mField[nextX][nextY] > 1) {
						mScore=mScore+10;
						mField[nextX][nextY] = 0;
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						addFruite();
						return true;
					}
				}
				//любая другая херня=)
				else {
					return false;
					}
				
			}

			//pravo
			case DIR_EAST: {
				int nextX = mSnake.get(mSnake.size() - 1).x + 1;
				int nextY = mSnake.get(mSnake.size() - 1).y;
				if (nextX < mFieldX) {
					if( mField[nextX][nextY] == 0) {
						mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
						mSnake.remove(0);
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						return true;
					} else if (mField[nextX][nextY] == 1) {
						return false;
					} else if (mField[nextX][nextY] > 1) {
						mScore=mScore+10;
						mField[nextX][nextY] = 0;
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						addFruite();
						return true;
					}
				} 
				
				if((nextX >= mFieldX)||(nextX < 0)) {
					nextX=0;
					if (mField[nextX][nextY] == 0) {
						mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
						mSnake.remove(0);
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						return true;
					} else if ((nextX < mFieldX) && mField[nextX][nextY] == 1) {
						return false;
					} else if ((nextX < mFieldX) && mField[nextX][nextY] > 1) {
						mScore=mScore+10;
						mField[nextX][nextY] = 0;
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						addFruite();
					return true;
					}
				}
				else{
					return false;
				}
			}
			case DIR_SOUTH: {
				int nextX = mSnake.get(mSnake.size() - 1).x;
				int nextY = mSnake.get(mSnake.size() - 1).y + 1;
				
				if (nextY < mFieldY) {
					if(mField[nextX][nextY] == 0) {
						mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
						mSnake.remove(0);
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						return true;
					} else if (mField[nextX][nextY] == 1) {
						return false;
					} else if (mField[nextX][nextY] > 1) {
						mScore=mScore+10;
						mField[nextX][nextY] = 0;
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						addFruite();
						return true;
						}
				} 
				if((nextY >= mFieldY)||(nextY < 0)){
					nextY=0;
					if ( mField[nextX][nextY] == 0) {
						mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
						mSnake.remove(0);
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						return true;
					} else if (mField[nextX][nextY] == 1) {
						return false;
					} else if (mField[nextX][nextY] > 1) {
						mScore=mScore+10;
						mField[nextX][nextY] = 0;
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						addFruite();
						return true;
					}
				}
				else{
					return false;
				}
			}
			//levo
			case DIR_WEST: {
				int nextX = mSnake.get(mSnake.size() - 1).x - 1;
				int nextY = mSnake.get(mSnake.size() - 1).y;
				if (nextX >= 0) {
					if (mField[nextX][nextY] == 0) {
						mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
						mSnake.remove(0);
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
					return true;
				} else if (mField[nextX][nextY] == 1) {
					return false;
				} else if (mField[nextX][nextY] > 1) {
					mScore=mScore+10;
					mField[nextX][nextY] = 0;
					mSnake.add(new pos(nextX, nextY));
					mField[nextX][nextY] = -1;
					addFruite();
					return true;
					}
				} 
				
				if((nextX < 0)||(nextX >= mFieldX)) {
					nextX=mFieldX-1;
					if (mField[nextX][nextY] == 0) {
						mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
						mSnake.remove(0);
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						return true;
					} else if ((nextX >= 0) && mField[nextX][nextY] == 1) {
						return false;
					} else if ((nextX >= 0) && mField[nextX][nextY] > 1) {
						mScore=mScore+10;
						mField[nextX][nextY] = 0;
						mSnake.add(new pos(nextX, nextY));
						mField[nextX][nextY] = -1;
						addFruite();
						return true;
					} 
					return true;
				}
				else{
					return false;
				}
			  }
			}
			return false;
		}
		
		// здесь и нижу всякие геттеры и сеттеры
		// думаю тут и объяснять нечего
		public int getDirection() {
			return mDirection;
		}
		
		public void clearScore(){
			this.mScore=0;
		}

		public void setDirection(int direction) {
			this.mDirection = direction;
		}

		public int[][] getmField() {
			return mField;
		}

		public int getSnakeLength() {
			return  mSnake.size();
		}

		public ArrayList<pos> getmSnake() {
			return mSnake;
		}
	}
