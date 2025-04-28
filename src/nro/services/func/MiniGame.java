package nro.services.func;

public class MiniGame {

    private static MiniGame instance;
    public Game MiniGame_S1 = new Game(); // XoSo
    public Game2 MiniGame_S2 = new Game2(); // gi cung duoc
//    public Game MiniGame_S3 = new Game(); // gi cung duoc
//    public Game MiniGame_S4 = new Game(); // gi cung duoc

    public static MiniGame gI() {
        if (instance == null) {
            instance = new MiniGame();
        }
        return instance;
    }
}
