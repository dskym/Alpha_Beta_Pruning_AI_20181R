/*
1. 오목을 구현한다.
- 시간 제한을 입력받을 수 있다.
- 누가 먼저 시작하는 지 입력받을 수 있다.
- 놓을 돌의 위치는 좌표를 입력 받는다.
- 오목을 어떻게 체크할 지 아이디어를 생각한다(놓은 돌 주변만?)
- 오목판은 19x19 배열

2. Alpha-Beta Pruning을 위한 틀을 구현한다.
- 기존에 구현한 거 활용해도 될 거 같다.

3. Iterative Deepning Search를 구현한다.
- 어느 정도의 depth로 어떻게 Search할 것인가?

4. Evaluation Function을 구현한다.
- 점수를 어떻게 측정할 것인가?

< process >
1) 보드와 데이터를 초기화한다.
2) 시작하는 플레이어부터 돌을 둔다.
3) 오목이 되었는 지 아닌 지 체크한다.

Tree
- Iterative Deepening
- Alpha-Beta Pruning
*/

public class Main {
    public static void main(String args[]) {
        Game game = new Game();

        game.play();
    }
}
