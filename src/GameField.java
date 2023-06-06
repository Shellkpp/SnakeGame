import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320; //размер поля
    private final int DOT_SIZE = 16; //размер в пикселях змейки и яблочка (16 на 16)

    // 320/16=20 - т.е. количество точек/ячеек будет 20 в ширину и 20 в высоту
    private final int ALL_DOTS = 400; //20 на 20
    private Image dot;
    private Image apple;
    private int appleX;
    private int appleY;
    private int[] x = new int[ALL_DOTS]; //положение и размер змейки
    private int[] y = new int[ALL_DOTS];
    private int dots; //размер змейки
    private Timer timer;

    //пять полей, отвечающих за текущее направление движения змейки
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

    public GameField(){
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);         //чтобы при нажатии печатных клавиш фокус оставался на игровом поле
    }
    
    public void initGame(){
        dots = 3; //начальное количество точек
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i*DOT_SIZE;  //начальные координаты змейки
            y[i] = 48;
        }
        timer = new Timer(250,this); //будет тикать каждые 250мс и заставлять делать действия каждый раз
        timer.start();
        createApple();
    }

    public void createApple(){
        appleX = new Random().nextInt(20)*DOT_SIZE; //создаем координату по оси Х, в пределах 20 (длина одной строки)
        appleY = new Random().nextInt(20)*DOT_SIZE;
    }

    public void loadImages(){
        ImageIcon iia = new ImageIcon("apple.png"); //если не в корневой папке, то указываем путь до файла
        apple = iia.getImage();
        ImageIcon iit = new ImageIcon("dot.png");
        dot = iit.getImage();
    }

    @Override   //переопределяем метод
    protected void paintComponent(Graphics g) { //перерисовка нашей игры
        super.paintComponent(g);              //перерисовываются внутренние компоненты
        //а тут мы пишем че будет перерисовываться непосредственно в нашей игре
        if(inGame){
            //перерисовываем (яблоко, с такими координатами, перерисовывать будет этот компонент)
            g.drawImage(apple,appleX,appleY,this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot,x[i],y[i],this);
            }
        } else {
            String str = "Game over";
//            Font f = new Font("Arial", 14, Font.BOLD);
            g.setColor(Color.white);
//            g.setFont(f);
            g.drawString(str, 125, SIZE/2);
        }
    }

    public void move(){
        for (int i = dots; i > 0 ; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];

        }
        if(left){
            x[0] -= DOT_SIZE;
        }
        if(right){
            x[0] += DOT_SIZE;
        }
        if(up){
            y[0] -= DOT_SIZE;
        }
        if(down){
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple(){
        if (x[0] == appleX && y[0] == appleY){
            dots++;
            createApple();
        }
    }

    public void checkCollisions(){
        for (int i = dots; i > 0; i--) {
            if (i>4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }

        if(x[0]>SIZE){
            inGame = false;
        }
        if(x[0]<0){
            inGame = false;
        }
        if(y[0]>SIZE){
            inGame = false;
        }
        if(y[0]<0){
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {  //метод из интерфейса ActionListener. Отвечает за то что будет происходить в каждый тик таймера
        if(inGame){
            checkApple();
            checkCollisions();
            move();
        }
        repaint(); //вызывает PaintComponent, который отвечает за перерисовку элементов в Swing
    }

    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !right){
                left = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_DOWN && !up){
                down = true;
                left = false;
                right = false;
            }
            if(key == KeyEvent.VK_UP && !down){
                up = true;
                right = false;
                left = false;
            }
        }
    }

}
