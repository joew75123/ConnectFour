
public class Utilities
{


    public static int findBottomEmptyRow(int[][] board, int columnNumber, int boardRows )
    {
        for( int i = boardRows - 1; i >= 0; i-- )
        {
            if ( board[columnNumber][i] == 0 )
                return i;
        }

        return -1;
    }

    public static boolean isValidColumn(int column, int boardColumns)
    {
        if ( !( 0 <= column ) || !( column < boardColumns ) )
            return false;
        return true;
    }

    public static boolean isValidRow(int row, int boardRow)
    {
        if ( !( 0 <= row ) || !( row < boardRow ) )
            return false;
        return true;
    }

    public static void checkValidColumn( int column, int boardColumns ) throws InvalidMoveException
    {
        if ( !isValidColumn(column,boardColumns))
        {
            throw new InvalidMoveException(String.format("Column number out of range. Has to be between 1 - %d",boardColumns));
        }
    }




    public static boolean fourInARow(int[][] board, int column, int row, int columnDelta, int rowDelta, int boardRows, int boardCols)
    {
        int startingCell = board[column][row];

        if ( startingCell == 0)
            return false;

        for ( int i = 1; i < 4; i++ )
        {
            if ( !isValidColumn(column + columnDelta * i, boardCols) || !isValidRow(row + rowDelta * i, boardRows) ||
            board[column + columnDelta * i][row + rowDelta * i] != startingCell)
                return false;
        }
        return true;
    }

/*
We passed two offset numbers for column and row, called columnDelta and rowDelta,
which will manipulate the function in the fourInRow functions to get the the coordinates
around the position that the coin was placed. The fourInRow will check 8 directions from
where the coin was placed. For example, since the upper-left corner is the original point,
the (0,1) will check the 3 slots downward to the coin. And (1, -1) will check the up-right direction,
because each time in the loop, the column+1, the row-1.

 */
    public static boolean checkWinningSequence(int[][] board, int column, int row, int boardRows, int boardColumns)
    {
        return (fourInARow(board,column,row,0,1,boardRows,boardColumns) || fourInARow(board,column,row,0,-1,boardRows,boardColumns) ||
                fourInARow(board,column,row,1,0,boardRows,boardColumns) || fourInARow(board,column,row,1,-1,boardRows,boardColumns) ||
                fourInARow(board,column,row,1,1,boardRows,boardColumns) || fourInARow(board,column,row,-1,0,boardRows,boardColumns) ||
                fourInARow(board,column,row,-1,-1,boardRows,boardColumns) || fourInARow(board,column,row,-1,1, boardRows,boardColumns) );

    }

}


class InvalidMoveException extends Exception
{
    public InvalidMoveException()
    {
        super();
    }

    public InvalidMoveException(String message)
    {
        super(message);
    }

}

class GameOverException extends Exception
{
    public GameOverException()
    {
        super();
    }

    public GameOverException(String message)
    {
        super(message);
    }
}
