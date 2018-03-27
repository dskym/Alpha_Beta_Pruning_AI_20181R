/*
Game
- 게임 상태 관리(시간)
- 누구 턴인지 관리
- board 관리
*/

public class Game {
    public static int BOARD_SIZE = 19;

    public static int PLAYER_A = 1;
    public static int PLAYER_B = 2;

    public static Board board = new Board();

    private int limitTime;
    private int player;

    public Game(int player, int limitTime) {
        this.player = player;
        this.limitTime = limitTime;
    }
}
