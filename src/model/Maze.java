package model;

import java.util.ArrayList;
import java.util.List;

public class Maze {
	private int[][] layout;
	private List<Item> listItem;
	private List<Trap> listTrap;
	private List<Cheese> cheese;
	private List<Position> denZone;
	private Position tomStart;
	private Position jerryStart;
	private Position exitPosition;

	// ===== Trạng thái gốc =====
	private List<Item> originalItems;
	private List<Trap> originalTraps;
	private List<Cheese> originalCheeses;
	private final Position jerryOrigin;
	private final Position tomOrigin;

	public Maze(int[][] layout, List<Item> listItem, List<Trap> listTrap, List<Cheese> cheese, Position tomStart,
			Position jerryStart, Position exitPosition) {
		super();
		this.layout = layout;
		this.listItem = listItem;
		this.listTrap = listTrap;
		this.cheese = cheese;
		this.tomStart = tomStart;
		this.jerryStart = jerryStart;
		this.exitPosition = exitPosition;
		this.denZone = new ArrayList<>();
		initDenZone(); 
		this.jerryOrigin = new Position(jerryStart.getX(), jerryStart.getY());
		this.tomOrigin   = new Position(tomStart.getX(), tomStart.getY());

		
		// Lưu trạng thái gốc để reset
        saveOriginalItems();
	}

	// ====== Getter ======
	public int[][] getLayout() {
		return layout;
	}

	public List<Item> getListItem() {
		return listItem;
	}

	public List<Trap> getListTrap() {
		return listTrap;
	}

	public List<Cheese> getCheese() {
		return cheese;
	}

	public Position getJerryStart() {
	    return new Position(jerryOrigin.getX(), jerryOrigin.getY());
	}

	public Position getTomStart() {
	    return new Position(tomOrigin.getX(), tomOrigin.getY());
	}

	public Position getExitPosition() {
		return exitPosition;
	}

	public void setLayout(int[][] layout) {
		this.layout = layout;
	}

	public void setListItem(List<Item> listItem) {
		this.listItem = listItem;
	}

	public void setListTrap(List<Trap> listTrap) {
		this.listTrap = listTrap;
	}

	public void setCheese(List<Cheese> cheese) {
		this.cheese = cheese;
	}

	public void setTomStart(Position tomStart) {
		this.tomStart = tomStart;
	}

	public void setJerryStart(Position jerryStart) {
		this.jerryStart = jerryStart;
	}

	public void setExitPosition(Position exitPosition) {
		this.exitPosition = exitPosition;
	}
	
	public boolean isDenZone(Position pos) {
        return denZone.contains(pos);
    }

	/**
	 * Kiểm tra vị trí có thể đi được (không phải tường, trong giới hạn)
	 * 
	 * @param position
	 * @return
	 */
	public boolean canMove(Position position) {
		if (position.getX() < 0 || position.getX() >= layout.length || position.getY() < 0
				|| position.getY() >= layout[0].length) {
			return false;
		}
		return layout[position.getX()][position.getY()] == 0;
	}

	// Kiểm tra vị trí là tường
	public boolean isWall(Position position) {
	    int x = position.getX();
	    int y = position.getY();
	    
	    if (x < 0 || x >= layout.length || y < 0 || y >= layout[0].length) {
	        return true; 
	    }
	    
	    return layout[x][y] == 1; // 1 = tường
	}

	// Kiểm tra vị trí có SpeedBoost
	public boolean hasSpeedBoost(Position position) {
		for (Item item : listItem) {
			// Kiểm tra item có phải là SpeedBoost và trùng vị trí
			if (item instanceof SpeedBoostItem && item.getPosition().equals(position)) {
				return true;
			}
		}
		return false;
	}

	// Kiểm tra vị trí có ShieldItem
	public boolean isShieldItem(Position position) {
		for (Item item : listItem) {
			// Kiểm tra item có phải là ShieldItem và trùng vị trí
			if (item instanceof ShieldItem && item.getPosition().equals(position)) {
				return true;
			}
		}
		return false;
	}

	// Kiểm tra vị trí có Trap
	public boolean hasTrap(Position position) {
		for (Trap trap : listTrap) {
			if (trap.getPosition().equals(position))
				return true;
		}
		return false;
	}

	// Kiểm tra vị trí có Cheese
	public boolean hasCheese(Position position) {
		for (Cheese c : cheese) {
			if (c.getPosition().equals(position))
				return true;
		}
		return false;
	}

	// Xóa Cheese trên map
	public void removeCheese(Position po) {
		Cheese toRemove = null;

		// Tìm cheese có vị trí trùng với po
		for (Cheese ch : cheese) {
			if (ch.getPosition().equals(po)) {
				toRemove = ch;
				break; // chỉ xóa 1 cheese trùng vị trí
			}
		}
		// Xóa cheese nếu tìm thấy
		if (toRemove != null) {
			cheese.remove(toRemove);
		}
	}

	// Xóa Item SpeedBoost trên map
	public void removeSpeedBoost(Position po) {
		Item toRemove = null;

		// Tìm item có vị trí trùng với po
		for (Item it : listItem) {
			if (it instanceof SpeedBoostItem && it.getPosition().equals(po)) {
				toRemove = it;
				break; // chỉ xóa 1 item trùng vị trí
			}
		}
		// Xóa item nếu tìm thấy
		if (toRemove != null) {
			listItem.remove(toRemove);
		}
	}

	// Xóa Item Shield trên map
	public void removeShieldItem(Position po) {
		Item toRemove = null;

		// Tìm item có vị trí trùng với po và là ShieldItem
		for (Item it : listItem) {
			if (it instanceof ShieldItem && it.getPosition().equals(po)) {
				toRemove = it;
				break; // chỉ xóa 1 item trùng vị trí
			}
		}

		// Xóa item nếu tìm thấy
		if (toRemove != null) {
			listItem.remove(toRemove);
		}
	}

	// Xóa Trap trên map
	public void removeTrap(Position po) {
		Trap toRemove = null;

		// Tìm item có vị trí trùng với po và là Trap
		for (Trap t : listTrap) {
			if (t instanceof Trap && t.getPosition().equals(po)) {
				toRemove = t;
				break; // chỉ xóa 1 item trùng vị trí
			}
		}

		// Xóa item nếu tìm thấy
		if (toRemove != null) {
			listTrap.remove(toRemove);
		}
	}

	/**
	 * Lấy danh sách vị trí Cheese
	 */
	public List<Position> getCheesePositions() {
		List<Position> positions = new ArrayList<>();
		for (Cheese c : this.cheese) {
			positions.add(c.getPosition());
		}
		return positions;
	}
	
	public void initDenZone() {
	    denZone.clear();

	    for (Cheese ch : cheese) {
	        Position p = ch.getPosition();

	        for (int dx = -1; dx <= 1; dx++) {
	            for (int dy = -1; dy <= 1; dy++) {
		            	int nx = p.getX() + dx;
		            	int ny = p.getY() + dy;
	
		            	if (nx >= 0 && nx < layout.length &&
		            	    ny >= 0 && ny < layout[0].length) {
		            	    denZone.add(new Position(nx, ny));
		            	}
	            }
	        }
	    }
	}



	/**
	 * Tạo bản sao Maze (clone layout và tất cả items)
	 */
	public Maze clone() {
		// Clone layout
		int[][] newLayout = new int[layout.length][];
		for (int i = 0; i < layout.length; i++)
			newLayout[i] = layout[i].clone();

		// Clone lists
		List<Item> newItems = new ArrayList<>();
		for (Item it : listItem)
			newItems.add(it.clone());

		List<Trap> newTraps = new ArrayList<>();
		for (Trap tr : listTrap)
			newTraps.add(tr.clone());

		List<Cheese> newCheeses = new ArrayList<>();
		for (Cheese ch : cheese)
			newCheeses.add(ch.clone());

		return new Maze(newLayout, newItems, newTraps, newCheeses, tomStart.clone(), jerryStart.clone(),
				exitPosition.clone());
	}

	 /** 
	  * Lưu trạng thái gốc (clone) 
	  */
   private void saveOriginalItems() {
       originalItems = new ArrayList<>();
       for(Item it : listItem) originalItems.add(it.clone());

       originalTraps = new ArrayList<>();
       for(Trap tr : listTrap) originalTraps.add(tr.clone());

       originalCheeses = new ArrayList<>();
       for(Cheese ch : cheese) originalCheeses.add(ch.clone());
   }

   /** 
    * Reset items, traps, cheese về trạng thái gốc 
    */
   public void resetItems() {
       listItem = new ArrayList<>();
       for(Item it : originalItems) listItem.add(it.clone());

       listTrap = new ArrayList<>();
       for(Trap tr : originalTraps) listTrap.add(tr.clone());

       cheese = new ArrayList<>();
       for(Cheese ch : originalCheeses) cheese.add(ch.clone());
   }

}
