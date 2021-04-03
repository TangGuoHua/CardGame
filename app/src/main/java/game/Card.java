package game;

public class Card {
    private int point;

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Card(int point, String name) {
        this.point = point;
        this.name = name;
    }

    public String toString() {// we can extends this method to meeting different display requirements
        return name;
    }
}
