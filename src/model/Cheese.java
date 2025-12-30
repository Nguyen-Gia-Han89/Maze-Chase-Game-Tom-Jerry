package model;

/**
 * Lớp Cheese đại diện cho miếng phô mai trong mê cung.
 * Jerry có thể nhặt để tăng điểm hoặc đạt điều kiện thắng.
 */
public class Cheese {
    private Position position; // Vị trí miếng phô mai
    private static int number = 5;        // Số lượng phô mai (có thể dùng để tính điểm)

    public Cheese(Position position) {
        super();
        this.position = position;
    }

    /** Lấy vị trí phô mai */
    public Position getPosition() { return position; }

    public static int getNumber() {
		return number;
	}

	public static void setNumber(int number) {
		Cheese.number = number;
	}

	/** Chuẩn hóa in ra thông tin phô mai (debug / log) */
    @Override
    public String toString() {
        return "Cheese position: " + position + ", number: " + number;
    }

    
    /**
     * Tạo bản sao của phô mai.
     */
	public Cheese clone() {
		return new Cheese(position.clone());
	}
}
