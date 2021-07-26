import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class RunGame
{
    public final int TWO_PLAYERS = 0;
    public final int MACHINE = 1;

    private final JFrame frame;
    private final JPanel panel;
    private final JButton[][] buttons;
    private final GridLayout grid;

    private ArrayList<int[]> undoStack = new ArrayList<>();
    private ArrayList<int[]> redoStack = new ArrayList<>();


    public static void main(String[] args) {


        int choiceMode = getGameMode();
        RunGame mainGame = new RunGame(choiceMode);


    }


    //When the player launches the game, it will prompt a JOptionPane, asking for an input from the player to get the game mode.
    public static int getGameMode()
    {
        int gameMode = Integer.parseInt(JOptionPane.showInputDialog("0 for two player game || 1 for computer opponent"));
        //if invalid input
        while ( gameMode != 0 && gameMode != 1)
        {
            gameMode = Integer.parseInt(JOptionPane.showInputDialog("0 for two player game || 1 for computer opponent"));
        }
        return gameMode;

    }

    ImageIcon emptySlot;

    {
        emptySlot = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Resources/emptycell.png")));
    }

    ImageIcon player1Icon;

    {
        player1Icon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Resources/player1.png")));
    }

    ImageIcon player2Icon;

    {
        player2Icon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Resources/player2.jpeg")));
    }

    ImageIcon backgroundIcon;
    {
        backgroundIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Resources/bkg.png")));
    }

    private ConnectFour game;
    private int mode;


    public void setMode(int mode)
    {
        if ( mode == 0)
            this.mode = TWO_PLAYERS;
        else
            this.mode = MACHINE;
    }


//Then set up the game based on the game mode, using JFrame, JPanel, and JButton.
    public RunGame(int mode)
    {
        frame = new JFrame("Connect Four");
        panel = new JPanel();

        this.game = new ConnectFour();
        setMode(mode);

        buttons = new JButton[this.game.returnColumns()][this.game.returnRows()+1];
        grid = new GridLayout(this.game.returnRows() + 1,this.game.returnColumns());
        panel.setLayout(grid);

        frame.setContentPane(panel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(900,800);
        frame.isResizable();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initGrid();
    }

//The initGrid will add the bottom option menu bar and add actionListener to each empty slot and the option menu bar.
    public void initGrid()
    {
        for ( int i = 0; i < this.game.returnRows(); i++ ){
            for ( int j = 0; j < this.game.returnColumns(); j++)
            {
                buttons[j][i] = new JButton(emptySlot);


                // Add action listener to buttons based on game MODE
                if(this.mode == MACHINE)
                    buttons[j][i].addActionListener(new onePlayerListener());

               if(this.mode == TWO_PLAYERS)
                    buttons[j][i].addActionListener(new twoPlayerListener());

                panel.add(buttons[j][i]);
            }
        }

        for( int i = 0; i < this.game.returnColumns(); i++)
        {
            if (i == 3) {
                buttons[i][6] = new JButton("RESET");
                buttons[i][6].addActionListener(new ResetAction());
                panel.add(buttons[i][6]);
            }
            else if (i == 2)
            {
                buttons[i][6] = new JButton("EXIT");
                buttons[i][6].addActionListener(new exitAction());
                panel.add(buttons[i][6]);
            }
            else if(i == 4)
            {
                buttons[i][6] = new JButton("UNDO");
                buttons[i][6].addActionListener(new undoAction());
                panel.add(buttons[i][6]);

            }
            else if ( i == 5)
            {
                buttons[i][6] = new JButton("REDO");
                buttons[i][6].addActionListener(new redoAction());
                panel.add(buttons[i][6]);

            }
            else if ( i == 6)
            {
                buttons[i][6] = new JButton("FINISH TURN");
                buttons[i][6].addActionListener(new finishAction());
                panel.add(buttons[i][6]);
            }
            else {
                buttons[i][6] = new JButton(backgroundIcon);
                panel.add(buttons[i][6]);
            }


        }

    }






    public int machineChooseColumn()
    {
        Random rand = new Random();
        int columns = game.returnColumns();
        return rand.nextInt(columns);
    }

    public void machineMakeDrop() throws InvalidMoveException, GameOverException
    {
        try {
            int columnChosen = machineChooseColumn();

            int bottomRow = dropAction(columnChosen); // Exception will happen here
            printCurrentGame();
            System.out.println();
            buttons[columnChosen][bottomRow].setIcon(player2Icon);
            game.switchTurns();
        }
        catch(InvalidMoveException e){
            machineMakeDrop();
        }


    }


    public int dropAction(int column) throws InvalidMoveException, GameOverException
    {
        return this.game.drop(column);
    }

//prints text version of game in terminal
    public void printCurrentGame()
    {

        for (int i = 0; i < this.game.returnRows(); i++)
        {
            for (int j = 0; j < this.game.returnColumns(); j++)
            {
                if (this.game.returnBoard()[j][i] == 0 ) {
                    System.out.print(" X ");
                }
                else if ( this.game.returnBoard()[j][i] == 1)
                    System.out.print(" R ");
                else
                    System.out.print(" Y ");
            }
            System.out.println();
        }


    }


    public void invalidMessage()
    {
        JFrame invalidFrame = new JFrame();
        JOptionPane.showMessageDialog(
                invalidFrame, "Invalid Move Please Try again","INVALID",
                JOptionPane.WARNING_MESSAGE
        );

    }

    public void winnerMessage(int winner)
    {
        JFrame winnerFrame = new JFrame();
        JOptionPane.showMessageDialog(
                winnerFrame, String.format("Player %d won!",winner),"GAME OVER",
                JOptionPane.WARNING_MESSAGE
        );

    }

    public void makeMoveMessage(int player)
    {
        JFrame winnerFrame = new JFrame();
        JOptionPane.showMessageDialog(
                winnerFrame, String.format("Player %d must make a move first",player),"Cannot Continue",
                JOptionPane.WARNING_MESSAGE
        );

    }

    public void resetGame()
    {
        for ( int i = 0; i < this.game.returnRows(); i++ )
        {
            for ( int j = 0; j < this.game.returnColumns(); j++)
            {
                buttons[j][i].setIcon(emptySlot);
            }
        }
        undoStack.clear();
        redoStack.clear();
        game.reset();
    }


/*
When the player clicks on any column, the action will be performed,
if there is no move made, it will check which one is the most bottom
available empty slot and set that slot to the player’s icon, and also
record this position into the undo stack.

 */

    private class onePlayerListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            int playersTurn = game.whosTurn();


            try
            {
                for (int i = game.returnRows() - 1; i >= 0; i--)
                {
                    for (int j = 0; j < game.returnColumns(); j++)
                    {
                        if (buttons[j][i] == e.getSource())
                        {
                            if( undoStack.isEmpty()) {

                                int bottomRow = dropAction(j);
                                int[] colRow = new int[2];
                                colRow[0] = j;
                                colRow[1] = bottomRow;
                                undoStack.add(colRow);
                                redoStack.clear();
                                printCurrentGame();
                                System.out.println();
                                if (playersTurn == 1)
                                    buttons[j][bottomRow].setIcon(player1Icon);
                            }

                        }
                    }

                }
            }
            //check if the winner appears, if yes, throw the gameOverException.

            catch (GameOverException g)
            {
                playersTurn = game.whosTurn();
                printCurrentGame();
                String message = g.getMessage();
                String[] columnRow = message.split(" ");
                int col = Integer.parseInt(columnRow[0]);
                int row = Integer.parseInt(columnRow[1]);


                if (playersTurn == 1)
                    buttons[col][row].setIcon(player1Icon);
                else
                    buttons[col][row].setIcon(player2Icon);


                winnerMessage(playersTurn);
                resetGame();


            }
            catch (InvalidMoveException iE)
            {
                invalidMessage();

            }


        }
    }
/*
When the player clicks on any column, the action will be performed,
if there is no move made, it will check which one is the most bottom
available empty slot and set that slot to the player’s icon, and also
record this position into the undo stack.

 */
    private class twoPlayerListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            int playersTurn = game.whosTurn();


            try
            {
                for (int i = game.returnRows() - 1; i >= 0; i--)
                {
                    for (int j = 0; j < game.returnColumns(); j++)
                    {
                        if (buttons[j][i] == e.getSource())
                        {


                            if(undoStack.isEmpty()) {
                                int bottomRow = dropAction(j);
                                printCurrentGame();
                                System.out.println();
                                if (playersTurn == 1)
                                    buttons[j][bottomRow].setIcon(player1Icon);
                                else
                                    buttons[j][bottomRow].setIcon(player2Icon);

                                int[] colRow = new int[2];
                                colRow[0] = j;
                                colRow[1] = bottomRow;
                                undoStack.add(colRow);
                                redoStack.clear();
                            }

                        }
                    }

                }

            }
            //check if the winner appears, if yes, throw the gameOverException.
            catch (GameOverException g)
            {
                playersTurn = game.whosTurn();
                printCurrentGame();
                String message = g.getMessage();
                String[] columnRow = message.split(" ");
                int col = Integer.parseInt(columnRow[0]);
                int row = Integer.parseInt(columnRow[1]);


                if (playersTurn == 1)
                    buttons[col][row].setIcon(player1Icon);
                else
                    buttons[col][row].setIcon(player2Icon);


                winnerMessage(playersTurn);
                resetGame();


            }
            catch (InvalidMoveException iE)
            {
                invalidMessage();

            }


        }
    }





    private class ResetAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            resetGame();

        }
    }

    private class exitAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);

        }
    }

    private class undoAction implements ActionListener
    {
        int restoreSlotCol = 0,restoreSlotRow = 0;


        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(!undoStack.isEmpty()) {
                restoreSlotCol = undoStack.get(0)[0];
                restoreSlotRow = undoStack.get(0)[1];
                redoStack.clear();
                redoStack.add(undoStack.get(0));
                undoStack.clear();
                buttons[restoreSlotCol][restoreSlotRow].setIcon(emptySlot);
                game.returnBoard()[restoreSlotCol][restoreSlotRow] = 0;
            }


        }
    }

    private class redoAction implements ActionListener
    {
        int redoSlotCol = 0,redoSlotRow = 0;



        @Override
        public void actionPerformed(ActionEvent e)
        {
            int playerTurn = game.whosTurn();
            System.out.println(playerTurn);
            if(!redoStack.isEmpty()) {
                redoSlotCol = redoStack.get(0)[0];
                redoSlotRow = redoStack.get(0)[1];
                undoStack.add(redoStack.get(0));
                redoStack.clear();
                if(playerTurn == 1) {
                    buttons[redoSlotCol][redoSlotRow].setIcon(player1Icon);
                    game.returnBoard()[redoSlotCol][redoSlotRow] = 1;
                }
                else {
                    buttons[redoSlotCol][redoSlotRow].setIcon(player2Icon);
                    game.returnBoard()[redoSlotCol][redoSlotRow] = 2;
                }

            }


        }
    }

    private class finishAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
// make sure the play made move
            if( undoStack.isEmpty())
            {
                makeMoveMessage(game.whosTurn());
                return;
            }


            game.switchTurns();
            redoStack.clear();
            undoStack.clear();
            //machine move
            if ( mode == MACHINE ) {
                try {
                    machineMakeDrop();
                } catch (GameOverException g) {
                    int playersTurn = game.whosTurn();
                    printCurrentGame();
                    String message = g.getMessage();
                    String[] columnRow = message.split(" ");
                    int col = Integer.parseInt(columnRow[0]);
                    int row = Integer.parseInt(columnRow[1]);


                    if (playersTurn == 1)
                        buttons[col][row].setIcon(player1Icon);
                    else
                        buttons[col][row].setIcon(player2Icon);


                    winnerMessage(playersTurn);
                    resetGame();
                }
                catch (InvalidMoveException iE)
                {
                    invalidMessage();
                }
            }


        }
    }

}
