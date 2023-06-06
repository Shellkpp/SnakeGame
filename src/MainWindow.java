import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow(){
        setTitle("Змейка"); //название окна
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Крестик в правом углу экрана теперь отвечает за закрытие окна
        setSize(320,345); //размеры окна
        setLocation(400,400); //координаты окна
        add(new GameField());
        setVisible(true); //окно видимое
    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
    }
}
