package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Cheese;
import model.Item;
import model.Maze;
import model.Position;
import model.ShieldItem;
import model.SpeedBoostItem;
import model.Trap;

/**
 * Lớp MazeLoader chịu trách nhiệm đọc file và tạo Maze từ file.
 * File mô tả layout của mê cung với các giá trị:
 * 0 -> ô trống
 * 1 -> tường
 * 2 -> Cheese
 * 3 -> Jerry Start
 * 4 -> Tom Start
 * 5 -> Exit
 * 6 -> Trap
 * 7 -> SpeedBoostItem
 * 8 -> ShieldItem
 */
public class MazeLoader {
	/**
     * Load Maze từ file text
     * @param filename đường dẫn file
     * @return Maze được tạo
     * @throws IOException nếu file không đọc được
     */
	public static Maze loadFromFile(String filename) throws IOException {
		Maze maze;
		List<int[]> tempLayout = new ArrayList<>();
		List<Cheese> cheeses = new ArrayList<>();
        List<Trap> traps = new ArrayList<>();
        List<Item> items = new ArrayList<>();
		Position jerryStart = null;
		Position tomStart = null;
		Position exitPos = null;

		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		int row = 0;
		
		while ((line = br.readLine()) != null) {
			String[] tokens = line.trim().split("\\s+");
			int[] rowArr = new int[tokens.length];
			
			for (int col = 0; col < tokens.length; col++) {
				int val = Integer.parseInt(tokens[col]);
				
				// Lưu giá trị vào layout (vật phẩm cũng là ô trống)
                if (val != 1)
                    rowArr[col] = 0;
                else
                    rowArr[col] = val;

				switch (val) {
				case 2:
                    cheeses.add(new Cheese(new Position(row, col)));
                    break;

                case 3:
                    jerryStart = new Position(row, col);
                    break;

                case 4:
                    tomStart = new Position(row, col);
                    break;

                case 5:
                    exitPos = new Position(row, col);
                    break;

                case 6:
                    traps.add(new Trap(new Position(row, col)));
                    break;

                case 7: // SpeedBoost
                    items.add(new SpeedBoostItem(new Position(row, col))); // vd: hiệu lực 3 lượt
                    break;

                case 8: // Shield
                    items.add(new ShieldItem(new Position(row, col)));
                    break;
                }
			}
			tempLayout.add(rowArr);
			row++;
		}
		br.close();

		int[][] layout = tempLayout.toArray(new int[0][]);
		maze = new Maze(layout, items, traps, cheeses, tomStart, jerryStart, exitPos);
		Cheese.setNumber(maze.getCheese().size());
		return maze;
	}

}