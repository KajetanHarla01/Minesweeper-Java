public class GameManager
{
    public volatile static GameState gameState = GameState.MAINMENU;
    public GameManager()
    {

    }
    public static void main(String[] args) throws InterruptedException {
        MainMenu menu = new MainMenu();
        menu.start();
        while (gameState == GameState.MAINMENU)
        {
            try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException ignored) {}
        }
        GameWindow game = new GameWindow(menu.getChosenWidth(), menu.getChosenHeigth(), menu.getChosenMines());
        game.start();
        while (gameState == GameState.PLAYING)
        {
            try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException ignored) {}
        }
        Thread.sleep(5000);
        game.frame.dispose();
        Results results = new Results(game.gameWin());
        results.start();
    }
    public enum GameState
    {
        MAINMENU, PLAYING, RESULTS
    }
}
