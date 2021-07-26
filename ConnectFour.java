
public class ConnectFour
{

    private final int EMPTY = 0;
    private final int RED = 1;
    private final int YELLOW = 2;

    private final int BOARD_COLUMNS = 7;
    private final int BOARD_ROWS = 6;

    private int turn;
    private int winner;
    private boolean gameOver;
    private int[][] board;



    public ConnectFour()
    {
        this.board = new int[BOARD_COLUMNS][BOARD_ROWS];
        this.gameOver = false;
        this.turn = RED;
        this.winner = EMPTY;

    }

    public int drop(int columnNumber ) throws InvalidMoveException, GameOverException
    {

        // Check valid column (i.e check in bounds, check column not full)
        // place piece in bottom most row
        // check for winner? exception


            Utilities.checkValidColumn(columnNumber,BOARD_COLUMNS);
            int bottomMost = Utilities.findBottomEmptyRow(this.board,columnNumber,BOARD_ROWS);
            if( bottomMost != -1)
            {
                this.board[columnNumber][bottomMost] = this.turn;
                if( Utilities.checkWinningSequence(board,columnNumber, bottomMost, BOARD_ROWS,BOARD_COLUMNS) )
                {
                    this.winner = this.turn;
                    throw new GameOverException(String.format("%d %d",columnNumber,bottomMost));
                }

            }
            else
                throw new InvalidMoveException("Column Full, try different one.");

            return bottomMost;


    }

    public void switchTurns()
    {
        if (this.turn == RED)
            this.turn = YELLOW;
        else
            this.turn = RED;
    }



    public int returnWinner()

    {
        return this.winner;
    }

    public int whosTurn()
    {
        return this.turn;
    }

    public int returnColumns()
    {
        return BOARD_COLUMNS;
    }

    public int returnRows()
    {
        return BOARD_ROWS;
    }

    public int[][] returnBoard()
    {
        return board;
    }


    public void reset()
    {
        for( int i = 0; i < this.BOARD_COLUMNS; i++)
        {
            for( int j = 0; j < this.BOARD_ROWS; j++)
            {
                this.board[i][j] = 0;
            }
        }
        this.turn = RED;
    }


}
